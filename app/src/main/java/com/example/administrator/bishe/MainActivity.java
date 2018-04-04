package com.example.administrator.bishe;

import android.content.Intent;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;


import com.example.administrator.bishe.data.DataCenter;
import com.example.administrator.bishe.entities.Course;
import com.example.administrator.bishe.entities.Student;
import com.example.administrator.bishe.util.GsonUtil;
import com.example.administrator.bishe.util.HttpUtil;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import android.os.Handler;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Call;
import okhttp3.Response;


public class MainActivity extends AppCompatActivity {
    @BindView(R.id.button) public Button btLogin;
    @BindView(R.id.userName) public EditText etUserName;
    @BindView(R.id.password) public EditText etPassword;
    private Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg){
            Toast toast = Toast.makeText(MainActivity.this,"用户名密码错误",Toast.LENGTH_LONG);
            toast.show();
        }
    };
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);


        btLogin.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View view) {
                String studentId = etUserName.getText().toString();
                String password = etPassword.getText().toString();
                if(TextUtils.isEmpty(studentId) || TextUtils.isEmpty(password)){
                    Toast toast = Toast.makeText(MainActivity.this,"用户名密码都不能为空",Toast.LENGTH_LONG);
                    toast.show();
                    return;
                }
                Map<String,String> data = new HashMap<>();
                data.put("studentId",studentId);
                data.put("password",password);
                HttpUtil.sendHttpRequest(getString(R.string.login),data,new okhttp3.Callback(){

                    @Override
                    public void onFailure(Call call, IOException e) {
                        Log.e("错误",e.toString());
                    }

                    @Override
                    public void onResponse(Call call, Response response) throws IOException {
                        String responseData = response.body().string();
                        Student student = null;
                        //返回json为空
                        if(responseData == null || responseData.length() <= 0){
                            handler.sendEmptyMessage(0);
                            return;
                        }
                        Map<String,Object> objectMap = GsonUtil.toMap(responseData);
                        //json解析后map为空
                        if(objectMap == null){
                            handler.sendEmptyMessage(0);
                            return;
                        }
                        Object studentData = objectMap.get("student");
                        //学生json数据为空
                        if(studentData == null){
                            handler.sendEmptyMessage(0);
                            return;
                        }
                        student = (Student)GsonUtil.toObject(studentData.toString().toString(),Student.class);
                        //学生对象为空
                        if(student == null){
                            handler.sendEmptyMessage(0);
                            return;
                        }

                        Log.d("登录信息",student.getName()+":登录成功");
                        DataCenter.getInstance()
                                .setStudent(student);
                        Object courseData = objectMap.get("coursesInfo");
                        if(courseData != null){
                            List<Course> courses = GsonUtil.toList(courseData.toString(),Course.class);
                            DataCenter.getInstance()
                                    .setChooseCourse(courses);
                        }
                        Object notChooseCourses = objectMap.get("notChooseCourses");
                        if(notChooseCourses != null){
                            List<Course> course = GsonUtil.toList(notChooseCourses.toString(),Course.class);
                            DataCenter.getInstance()
                                    .setNotChooseCourses(course);
                        }
                        Object waitForCheckInfo = objectMap.get("waitForCheckInfo");
                        if(waitForCheckInfo != null){
                            List<Course> courses = GsonUtil.toList(waitForCheckInfo.toString(),Course.class);
                            DataCenter.getInstance().setWaitForCheckCourse(courses);
                        }
                        Intent intent  = new Intent(MainActivity.this,LoginSuccessActivity.class);
                        startActivity(intent);
                    }
                });
            }
        });

    }
    @Override
    public void onStart(){
        super.onStart();
        if(DataCenter.getInstance().getStudent() != null){
            Intent intent  = new Intent(MainActivity.this,LoginSuccessActivity.class);
            startActivity(intent);
        }
    }
}
