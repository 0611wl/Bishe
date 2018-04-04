package com.example.administrator.bishe.util;

import android.os.Message;

/**
 * Created by Administrator on 2018/3/23 0023.
 */

public class MessageUtil {
    public static Message createMessage(int waht){
        Message message = new Message();
        message.what = waht;
        return message;
    }
}
