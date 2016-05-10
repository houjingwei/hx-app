package com.huixiang.live.service;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonElement;
import com.google.gson.JsonPrimitive;
import com.google.gson.JsonSerializationContext;
import com.google.gson.JsonSerializer;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by hjw on 16/5/5.
 */
public class BaseResponse {

    public int code;
    public int err_code;
    public String msg;
    public String err_msg;
    public Object data;
    private Object realData;

    public void dailData(Class c) {
        if (c == String.class) {
            realData = gson.toJson(data);
            return;
        }
    }

    public String toString() {
        return gson.toJson(this);
    }


    public boolean isDataArray() {
        return data != null && data instanceof List;
    }

    public boolean isSuccess() {
        return getRealCode() == 0 || getRealCode() == 200;
    }

    public String getRealMsg() {
        return msg == null ? err_msg : msg;
    }

    public int getRealCode() {
        if (code != 0) return code;
        if (err_code != 0) return err_code;
        return 0;
    }

    public <T> T getRealData(Class<T> c) {
        if (isDataArray()) return null;
        if (data != null) {
            return gson.fromJson(gson.toJson(data), c);
        }
        return null;
    }

    private <T> List<T> getRealDataList(Class<T> c) {
        if (!isDataArray()) return null;
        if (data != null) {
            if (data instanceof List) {
                List<T> ds = new ArrayList<>(((List) data).size());
                for (Object o : (List) data) {
                    T object = gson.fromJson(gson.toJson(o), c);
                    ds.add(object);
                }
                return ds;
            }
        }
        return null;
    }


    private final static Gson gson;

    static {
        gson = new GsonBuilder().registerTypeAdapter(Double.class, new JsonSerializer<Double>() {
            @Override
            public JsonElement serialize(Double src, Type typeOfSrc, JsonSerializationContext context) {
                if (src == src.longValue())
                    return new JsonPrimitive(src.longValue());
                return new JsonPrimitive(src);
            }
        }).create();
    }
}
