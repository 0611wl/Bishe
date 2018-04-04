package com.example.administrator.bishe.handler;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.widget.Toast;

import com.example.administrator.bishe.LoginSuccessActivity;


/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class HttpHandler extends Handler{
    public static final int REQUEST_FAIL = 1;//请求失败;
    public static final int EMPTY_DATA = 2;//没有此课程数据
    private Context context;
    public HttpHandler(Context context){
        this.context = context;
    }
    @Override
    public void handleMessage(Message msg){
        switch (msg.what){
            case REQUEST_FAIL:
                Toast toast = Toast.makeText(context,"请求失败",Toast.LENGTH_LONG);
                toast.show();
                break;
            case EMPTY_DATA:
                Toast toast1 = Toast.makeText(context,"没有此课程数据",Toast.LENGTH_LONG);
                toast1.show();
                break;
        }
    }
}
