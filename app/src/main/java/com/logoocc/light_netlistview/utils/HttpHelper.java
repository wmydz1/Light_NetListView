package com.logoocc.light_netlistview.utils;

import android.content.Context;
import android.graphics.Bitmap;
import android.support.v4.util.LruCache;
import android.util.Log;
import android.widget.Toast;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.Volley;
import com.logoocc.light_netlistview.bean.Person;
import com.logoocc.light_netlistview.bean.Result;

import java.util.List;

/**
 * Created by samchen on 8/26/15.
 */
public class HttpHelper {

    private static String TAG = "HttpHelper";
    public static final String BURL = "http://192.168.12.9:8080/json";
    private static RequestQueue queue;
    private static ImageLoader loader;
    private static HttpHelper helper;
    private static Context mContext;

    // 实例化 HttpHelper
    public static HttpHelper getInstance(Context context) {
        mContext = context;
        if (queue == null) {
            queue = Volley.newRequestQueue(context);
        }

        if (helper == null) {
            helper = new HttpHelper();
        }
        return helper;
    }


    // 处理数组
    public void loadPersons(String params, Response.Listener<Result<List<Person>>> listener, Response.ErrorListener errorListener) {

        FastJsonRequest<Result<List<Person>>> req = new FastJsonRequest<Result<List<Person>>>(BURL, params, new TypeReference<Result<List<Person>>>() {
        }, listener, errorListener);
        queue.add(req);
    }

    // 处理对象
    public void loadPerson(String params, Response.Listener<Result<Person>> listener) {

        FastJsonRequest<Result<Person>> req = new FastJsonRequest<Result<Person>>(BURL, params, new TypeReference<Result<Person>>() {
        }, listener, errorListener);
        queue.add(req);
    }

    // 把请求转换为 Json 字符串
    private String toParams(Person p, int action) {
        JSONObject jo = (JSONObject) JSON.toJSON(p);
        jo.put("action", action);
        return jo.toJSONString();
    }

    // 添加用户
    public void addPerson(Person p, Response.Listener<Result<Boolean>> listener) {
        FastJsonRequest<Result<Boolean>> req = new FastJsonRequest<Result<Boolean>>(BURL, toParams(p, 203), new TypeReference<Result<Boolean>>() {
        }, listener, errorListener);
        queue.add(req);
    }

    // 修改用户
    public void updatePerson(Person p, Response.Listener<Result<Boolean>> listener) {
        FastJsonRequest<Result<Boolean>> req = new FastJsonRequest<Result<Boolean>>(BURL, toParams(p, 204), new TypeReference<Result<Boolean>>() {
        }, listener, errorListener);
        queue.add(req);
    }

    // 删除用户
    public void delPerson(int id, Response.Listener listener) {
        String params = "{id:" + id + ",action:202}";
        FastJsonRequest<Result<Boolean>> req = new FastJsonRequest<Result<Boolean>>(BURL, params, new TypeReference<Result<Boolean>>() {
        }, listener, errorListener);
        queue.add(req);
    }





    // 获取图片类 Volley处理图片请求
    public ImageLoader getLoader() {
        if (queue == null) {
            throw new ExceptionInInitializerError("消息队列没有初始化或者创建");
        }

        if (loader == null) {
            loader = new ImageLoader(queue, new NetImgChe());
        }
        return loader;
    }


    // 请求失败监听器
    private Response.ErrorListener errorListener = new Response.ErrorListener() {
        @Override
        public void onErrorResponse(VolleyError volleyError) {
            Toast.makeText(mContext, "网络连接失败", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "---" + volleyError);
            if (volleyError.networkResponse != null) {
                Log.e(TAG, "---code----" + volleyError.networkResponse.statusCode);
                Log.e(TAG, "----msg---" + new String(volleyError.networkResponse.data));

            }
        }
    };

    // 图片缓存类
    class NetImgChe implements ImageLoader.ImageCache {
        LruCache<String, Bitmap> cache;

        NetImgChe() {
            if (cache == null) {
                int size = (int) (Runtime.getRuntime().maxMemory() / 8000);
                cache = new LruCache<>(size);
            }
        }

        @Override
        public Bitmap getBitmap(String s) {
            return cache.get(s);
        }

        @Override
        public void putBitmap(String s, Bitmap bitmap) {
            cache.put(s, bitmap);
        }

    }


}
