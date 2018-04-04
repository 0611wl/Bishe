package com.example.administrator.bishe.util;

import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonParser;
import com.google.gson.reflect.TypeToken;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by Administrator on 2018/2/1 0001.
 */

public class GsonUtil {
    private static final Gson gson = new Gson();
    public static Object toObject(String jsonData,Class clazz){
        return gson.fromJson(jsonData,clazz);
    }
    public static <T> Map<String,T> toMap(String jsonData){
        return gson.fromJson(jsonData,new TypeToken<Map<String,T>>(){}.getType());
    }
    public static <T> List<T> toList(String jsonData,Class<T> clazz){
        List <T> objectList = new ArrayList<>();
        JsonArray array = new JsonParser().parse(jsonData).getAsJsonArray();
        for(final JsonElement elem : array){
            objectList.add(gson.fromJson(elem,clazz));
        }
        return objectList;
    }
}
