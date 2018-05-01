package com.example.administrator.bishe;

import android.Manifest;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.ContentObserver;
import android.database.Cursor;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.os.StrictMode;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.FileProvider;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.administrator.bishe.data.DataCenter;
import com.example.administrator.bishe.entities.Material;
import com.example.administrator.bishe.entities.MultipleChoice;
import com.example.administrator.bishe.handler.HttpHandler;
import com.example.administrator.bishe.util.DownloadUtil;
import com.example.administrator.bishe.util.GsonUtil;
import com.tencent.smtt.sdk.TbsReaderView;

import java.io.File;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.internal.Utils;

public class Materials extends AppCompatActivity implements TbsReaderView.ReaderCallback{
    @BindView(R.id.materialList) public ListView materialList;
    @BindView(R.id.multiples) public ListView multipleList;

    private ProgressDialog progressDialog;
    private DownloadManager mDownloadManager;
    private long mRequestId;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_materials);
        ButterKnife.bind(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        //将chapter传来的json数据解析成List<Material>
        String materialData = getIntent().getStringExtra("materialData");
        Map<String,Object> map = GsonUtil.toMap(materialData);
        //final List<Material> materials = GsonUtil.toList(materialData,Material.class);
        final List<Material> materials = GsonUtil.toList(map.get("materials").toString(),Material.class);
        final List<MultipleChoice> multiples = GsonUtil.toList(map.get("multiples").toString(),MultipleChoice.class);
        materialList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                int materialSize = materials == null?0:materials.size();
                int chapterSize = multiples == null?0:multiples.size();
                return materialSize+chapterSize+2;
            }

            @Override
            public Object getItem(int i) {
                if(i == 0){
                    return "资料列表";
                }
                else if(i <= materials.size()){
                    return materials.get(i - 1 );
                }
                else if(i <= materials.size() + 1){
                    return "课后习题";
                }
                else{
                    return multiples.get(i - materials.size() - 2);
                }
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView textView = new TextView(Materials.this);
                textView.setClickable(true);
                if(i == 0){
                    textView.setText("资料列表");
                    return textView;
                }
                else if(i == materials.size() + 1){
                    textView.setText("课后习题");
                    return textView;
                }
                else if(i > materials.size() + 1){
                    final MultipleChoice multipleChoice = (MultipleChoice)getItem(i);
                    final int index = i-materials.size() - 2;
                    textView.setText(index+1 +": "+multipleChoice.getMultipleChoiceQuestion());
                    textView.setClickable(true);
                    Log.d("multiple",i+"");
                    textView.setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View view) {
                            Intent intent = new Intent(Materials.this,Multiple.class);
                            int chapterId = DataCenter.getInstance().getChapterData().getCurChapterId();
                            String studentId = DataCenter.getInstance().getStudent().getStudentId();
                            intent.putExtra("chapterId",chapterId+"");
                            intent.putExtra("index",index+"");
                            intent.putExtra("studentId",studentId);
                            startActivity(intent);
                        }
                    });
                    return textView;
                }
                final Material material = (Material)getItem(i);
                if(material == null) return null;
                textView.setText(material.getMaterialName());
                Log.d("开始查找","找找找!!!!");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        //todo 如果本地存在该文件,直接展示
                        if(isLocalExist(material.getMaterialName())){
                            //todo 展示
                            Log.d("查找结果","找到了!!!!");
                            File file = getLocalFile(material.getMaterialName());
                            openFile(Materials.this,file);
                        }
                        //todo 如果本地不存在,就下载
                        else{
                            Log.d("下载路径",getString(R.string.material)+"/"+material.getMaterialName());
                            DownloadUtil.get().download(getString(R.string.material)+"/" + material.getMaterialName(), Materials.this.getFilesDir().toString(),material.getMaterialName(),new DownloadUtil.OnDownloadListener() {
                                @Override
                                public void onDownloadSuccess(File file) {
                                    Log.d("下载结果","下完了!!!!!!!!");
                                }
                                @Override
                                public void onDownloading(int progress) {
                                    //progressBar.setProgress(progress);
                                    Log.d("本地路径:",Materials.this.getFilesDir().toString());
                                    Log.d("下载结果","下载中!!!!!!!!");
                                }
                                @Override
                                public void onDownloadFailed(Exception e) {
                                    Log.d("下载结果","下载失败!!!!!!!!");
                                    e.printStackTrace();
                                }
                            });
                        }

                    }
                });
                return textView;
            }
        });
        multipleList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return multiples != null?multiples.size():0;
            }

            @Override
            public Object getItem(int i) {
                return multiples.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView textView = new TextView(Materials.this);
                final MultipleChoice multipleChoice = (MultipleChoice)getItem(i);
                textView.setText(i+": "+multipleChoice.getMultipleChoiceQuestion());
                textView.setClickable(true);
                final int index = i;
                Log.d("multiple",i+"");
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(Materials.this,Multiple.class);
                        intent.putExtra("chapterId",DataCenter.getInstance().getChapterData().getCurChapterId());
                        intent.putExtra("index",index);
                        intent.putExtra("studentId",DataCenter.getInstance().getStudent().getStudentId());
                        startActivity(intent);
                    }
                });
                return textView;
            }
        });
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
    }

    @Override
    public void onCallBackAction(Integer integer, Object o, Object o1) {

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    private boolean isLocalExist(String fileName){
        return getLocalFile(fileName).exists();
    }
    private File getLocalFile(String mFileName) {
        //File filePath = new File(Materials.this.getFilesDir(), "material");
        File newFile = new File(Materials.this.getFilesDir(), mFileName);
        Log.d("查找路径:",newFile.getName());
        return newFile;
    }
    public void openFile(Context context,File file){
        try{
            Intent intent = new Intent();
            //设置intent的Action属性
            intent.setAction(Intent.ACTION_VIEW);
            Uri contentUri = FileProvider.getUriForFile(context.getApplicationContext(),"com.example.administrator.bishe.fileprovider",file);
            intent.addCategory(Intent.CATEGORY_DEFAULT);
            intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            intent.setDataAndType(contentUri,"application/vnd.android.package-archive");
            //intent.setDataAndType(contentUri,"application/pdf");
            //获取文件file的MIME类型
            String type = getMIMEType(file);
            //设置intent的data和Type属性。
            intent.setDataAndType(contentUri, type);
            //跳转
            context.startActivity(intent);
            Intent.createChooser(intent, "请选择对应的软件打开该附件！");
        }catch (ActivityNotFoundException e) {
            // TODO: handle exception
            Toast.makeText(context, "sorry附件不能打开，请下载相关软件！", Toast.LENGTH_LONG).show();
        }
    }
    private String getMIMEType(File file) {

        String type="*/*";
        String fName = file.getName();
        //获取后缀名前的分隔符"."在fName中的位置。
        int dotIndex = fName.lastIndexOf(".");
        if(dotIndex < 0){
            return type;
        }
        /* 获取文件的后缀名*/
        String end=fName.substring(dotIndex,fName.length()).toLowerCase();
        if(end=="")return type;
        //在MIME和文件类型的匹配表中找到对应的MIME类型。
        for(int i=0;i<MIME_MapTable.length;i++){if(end.equals(MIME_MapTable[i][0]))
            type = MIME_MapTable[i][1];
        }
        return type;
    }
    private String [][]  MIME_MapTable={
            //{后缀名，MIME类型}
            {".3gp",    "video/3gpp"},
            {".apk",    "application/vnd.android.package-archive"},
            {".asf",    "video/x-ms-asf"},
            {".avi",    "video/x-msvideo"},
            {".bin",    "application/octet-stream"},
            {".bmp",    "image/bmp"},
            {".c",  "text/plain"},
            {".class",  "application/octet-stream"},
            {".conf",   "text/plain"},
            {".cpp",    "text/plain"},
            {".doc",    "application/msword"},
            {".docx",   "application/vnd.openxmlformats-officedocument.wordprocessingml.document"},
            {".xls",    "application/vnd.ms-excel"},
            {".xlsx",   "application/vnd.openxmlformats-officedocument.spreadsheetml.sheet"},
            {".exe",    "application/octet-stream"},
            {".gif",    "image/gif"},
            {".gtar",   "application/x-gtar"},
            {".gz", "application/x-gzip"},
            {".h",  "text/plain"},
            {".htm",    "text/html"},
            {".html",   "text/html"},
            {".jar",    "application/java-archive"},
            {".java",   "text/plain"},
            {".jpeg",   "image/jpeg"},
            {".jpg",    "image/jpeg"},
            {".js", "application/x-javascript"},
            {".log",    "text/plain"},
            {".m3u",    "audio/x-mpegurl"},
            {".m4a",    "audio/mp4a-latm"},
            {".m4b",    "audio/mp4a-latm"},
            {".m4p",    "audio/mp4a-latm"},
            {".m4u",    "video/vnd.mpegurl"},
            {".m4v",    "video/x-m4v"},
            {".mov",    "video/quicktime"},
            {".mp2",    "audio/x-mpeg"},
            {".mp3",    "audio/x-mpeg"},
            {".mp4",    "video/mp4"},
            {".mpc",    "application/vnd.mpohun.certificate"},
            {".mpe",    "video/mpeg"},
            {".mpeg",   "video/mpeg"},
            {".mpg",    "video/mpeg"},
            {".mpg4",   "video/mp4"},
            {".mpga",   "audio/mpeg"},
            {".msg",    "application/vnd.ms-outlook"},
            {".ogg",    "audio/ogg"},
            {".pdf",    "application/pdf"},
            {".png",    "image/png"},
            {".pps",    "application/vnd.ms-powerpoint"},
            {".ppt",    "application/vnd.ms-powerpoint"},
            {".pptx",   "application/vnd.openxmlformats-officedocument.presentationml.presentation"},
            {".prop",   "text/plain"},
            {".rc", "text/plain"},
            {".rmvb",   "audio/x-pn-realaudio"},
            {".rtf",    "application/rtf"},
            {".sh", "text/plain"},
            {".tar",    "application/x-tar"},
            {".tgz",    "application/x-compressed"},
            {".txt",    "text/plain"},
            {".wav",    "audio/x-wav"},
            {".wma",    "audio/x-ms-wma"},
            {".wmv",    "audio/x-ms-wmv"},
            {".wps",    "application/vnd.ms-works"},
            {".xml",    "text/plain"},
            {".z",  "application/x-compress"},
            {".zip",    "application/x-zip-compressed"},
            {"",        "*/*"}
    };
    public void setListViewHeight(ListView listView) {
        ListAdapter listAdapter = listView.getAdapter();
        if (listAdapter == null) {
            return;
        }
        int totalHeight = 0;
        for (int i = 0; i < listAdapter.getCount(); i++) {
            View listItem = listAdapter.getView(i, null, listView);   //获得每个子item的视图
            listItem.measure(0, 0);   //先判断写入的widthMeasureSpec和heightMeasureSpec是否和当前的值相等，如果不等，重新调用onMeasure()
            totalHeight += listItem.getMeasuredHeight();   //累加不解释
        }
        ViewGroup.LayoutParams params = listView.getLayoutParams();
        params.height = totalHeight + (listView.getDividerHeight() * (listAdapter.getCount() - 1));   //加上每个item之间的距离
        listView.setLayoutParams(params);
    }

}
