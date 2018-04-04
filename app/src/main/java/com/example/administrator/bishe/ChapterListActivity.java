package com.example.administrator.bishe;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Message;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bishe.entities.Chapter;
import com.example.administrator.bishe.handler.HttpHandler;
import com.example.administrator.bishe.util.HttpUtil;
import com.example.administrator.bishe.util.MessageUtil;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;
import android.os.Handler;

public class ChapterListActivity extends AppCompatActivity {
    @BindView(R.id.chapterList) public ListView chapterList;
    @BindView(R.id.toolbar) public Toolbar toolbar;
    @BindView(R.id.fab) public FloatingActionButton fab;
    private Handler handler = new HttpHandler(ChapterListActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chapter_list);
        ButterKnife.bind(this);
        setSupportActionBar(toolbar);
        final ArrayList<Chapter> chapters = (ArrayList<Chapter>)getIntent().getSerializableExtra("chapters");
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        chapterList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                return chapters == null?0:chapters.size();
            }

            @Override
            public Object getItem(int i) {
                return chapters == null?null:chapters.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView textView = new TextView(ChapterListActivity.this);
                final Chapter chapter = (Chapter)getItem(i);
                if(chapter == null){
                    return  textView;
                }
                textView.setText(chapter.getChapterName());
                textView.setClickable(true);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,String> requestData = new HashMap<>();
                        requestData.put("chapterId",chapter.getChapterId()+"");
                        HttpUtil.sendHttpRequest(getString(R.string.materials), requestData, new Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                handler.sendMessage(MessageUtil.createMessage(HttpHandler.REQUEST_FAIL));
                            }
                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String jsonData = response.body().string();
                                if(jsonData == null || jsonData.length() == 0){
                                    handler.sendMessage(MessageUtil.createMessage(HttpHandler.EMPTY_DATA));
                                    return;
                                }
                                Intent intent = new Intent(ChapterListActivity.this,Materials.class);
                                intent.putExtra("materialData",jsonData);
                                startActivity(intent);
                            }
                        });
                    }
                });
                return textView;

            }
        });
    }
}
