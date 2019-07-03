package com.ljy.mrg;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.inject.Inject;

/**
 * Author:liujinyong
 * Date:2019/7/2
 * Time:18:06
 */
public class JsonMrg {
    private Gson gson = (new GsonBuilder()).setPrettyPrinting().create();
    @Inject
    public JsonMrg() {
    }

    public <T> String serialize(T o){
        return gson.toJson(o);
    }

    public <T> T unSerialize(String string, Class<T> clazz){
        return gson.fromJson(string, clazz);
    }
}
