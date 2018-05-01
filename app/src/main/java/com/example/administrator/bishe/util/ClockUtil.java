package com.example.administrator.bishe.util;

public class ClockUtil {
    public static int getSecond(){
        return (int) (System.currentTimeMillis()/1000);
    }
}
