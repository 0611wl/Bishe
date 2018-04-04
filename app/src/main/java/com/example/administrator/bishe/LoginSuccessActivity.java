package com.example.administrator.bishe;

import android.content.Intent;
import android.os.Bundle;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ListAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.example.administrator.bishe.data.DataCenter;
import com.example.administrator.bishe.entities.Chapter;
import com.example.administrator.bishe.entities.Course;
import com.example.administrator.bishe.handler.HttpHandler;
import com.example.administrator.bishe.util.GsonUtil;
import com.example.administrator.bishe.util.HttpUtil;
import com.example.administrator.bishe.util.MessageUtil;
import com.google.gson.Gson;

import android.os.Handler;
import android.widget.Toast;

import java.io.IOException;
import java.io.Serializable;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class LoginSuccessActivity extends AppCompatActivity {
    @BindView(R.id.courseList) public ListView courseList;
    @BindView(R.id.notChooseCourseList) public  ListView notChooseCourseList;
    private Handler handler = new HttpHandler(LoginSuccessActivity.this);
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_success);
        ButterKnife.bind(this);
        courseList.setAdapter(new BaseAdapter() {
            @Override
            public int getCount() {
                List<Course> courses = DataCenter.getInstance().getChooseCourse();
                if(courses != null){
                    return courses.size();
                }
                return 0;
            }

            @Override
            public Object getItem(int i) {
                List<Course> courses = DataCenter.getInstance().getChooseCourse();
                if(courses == null) return null;
                return courses.get(i);
            }

            @Override
            public long getItemId(int i) {
                return i;
            }

            @Override
            public View getView(int i, View view, ViewGroup viewGroup) {
                TextView textView = new TextView(LoginSuccessActivity.this);
                final Course course  = (Course)getItem(i);
                if(course == null) return null;
                String courseName = course.getCourseName();
                textView.setText(courseName+"--->"+course.getTeacherName());
                textView.setClickable(true);
                textView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Map<String,String> courseRequestData = new HashMap<>();
                        courseRequestData.put("courseId",course.getCourseId()+"");
                        //HttpUtil.sendHttpRequest("");
                        HttpUtil.sendHttpRequest(getString(R.string.chapters),courseRequestData,new okhttp3.Callback(){

                            @Override
                            public void onFailure(Call call, IOException e) {
                                MessageUtil.createMessage(HttpHandler.REQUEST_FAIL);
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                String responseData = response.body().string();
                                if(responseData == null || responseData.length() <= 0) {
                                    MessageUtil.createMessage(HttpHandler.EMPTY_DATA);
                                    return;
                                }
                                Map<String,Object> objectMap = GsonUtil.toMap(responseData);
                                if(objectMap == null){
                                    handler.sendMessage(MessageUtil.createMessage(HttpHandler.EMPTY_DATA));
                                    return;
                                }
                                Object chapterData = objectMap.get("chaptersInfo");
                                if(chapterData == null){
                                    handler.sendMessage(MessageUtil.createMessage(HttpHandler.EMPTY_DATA));
                                    return;
                                }
                                Log.d("章节信息",chapterData.toString());
                                List<Chapter> chapters = GsonUtil.toList(chapterData.toString(),Chapter.class);
                                Intent intent = new Intent(LoginSuccessActivity.this,ChapterListActivity.class);
                                intent.putExtra("chapters",(Serializable) chapters);
                                startActivity(intent);
                            }
                        });
                    }
                });
                return textView;
            }
        });
        setListViewHeightBasedOnChildren(courseList);
        notChooseCourseList.setAdapter(
                new BaseAdapter() {
                    @Override
                    public int getCount() {
                        List<Course> courses = DataCenter.getInstance().getNodeChooseCourse();
                        if(courses == null){
                            return 0;
                        }
                        return courses.size();
                    }

                    @Override
                    public Object getItem(int i) {
                        List<Course> courses = DataCenter.getInstance().getNodeChooseCourse();
                        if(courses == null){
                            return null;
                        }
                        return courses.get(i);
                    }

                    @Override
                    public long getItemId(int i) {
                        return i;
                    }

                    @Override
                    public View getView(int i, View view, ViewGroup viewGroup) {
                        TextView textView = new TextView(LoginSuccessActivity.this);
                        Course course = (Course)getItem(i);
                        if(course == null){
                            return null;
                        }
                        textView.setText(course.getCourseName());
                        return textView;
                    }
                }
        );
    }
    public void setListViewHeightBasedOnChildren(ListView listView) {

        // 获取ListView对应的Adapter

        ListAdapter listAdapter = listView.getAdapter();

        if (listAdapter == null) {

            return;

        }

        int totalHeight = 0;

        for (int i = 0; i < listAdapter.getCount(); i++) { // listAdapter.getCount()返回数据项的数目

            View listItem = listAdapter.getView(i, null, listView);

            listItem.measure(0, 0); // 计算子项View 的宽高

            totalHeight += listItem.getMeasuredHeight(); // 统计所有子项的总高度

        }

        ViewGroup.LayoutParams params = listView.getLayoutParams();

        params.height = totalHeight
                + (listView.getDividerHeight() * (listAdapter.getCount() - 1));

        // listView.getDividerHeight()获取子项间分隔符占用的高度

        // params.height最后得到整个ListView完整显示需要的高度

        listView.setLayoutParams(params);

    }
}
