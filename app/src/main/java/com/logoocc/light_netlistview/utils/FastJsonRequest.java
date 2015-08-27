package com.logoocc.light_netlistview.utils;

import android.text.TextUtils;
import android.util.Log;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.TypeReference;
import com.android.volley.NetworkResponse;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.HttpHeaderParser;

import java.io.UnsupportedEncodingException;
import java.lang.reflect.Type;
import java.util.Map;

/**
 * Created by samchen on 8/26/15.
 */
public class FastJsonRequest<T> extends Request<T> {


    private Response.Listener<T> listener;
    private String params;
    private Type type;
    private TypeReference<T> reference;


    public FastJsonRequest(String url, String params, Type type, Response.Listener listener, Response.ErrorListener errorlistener) {
        this(Method.POST, url, params, listener, errorlistener);
        this.type = type;
    }

    public FastJsonRequest(String url, String params, TypeReference<T> reference, Response.Listener listener, Response.ErrorListener errorlistener) {
        this(Method.POST, url, params, listener, errorlistener);
        this.reference = reference;
    }

    public FastJsonRequest(int method, String url, String params, Response.Listener listener, Response.ErrorListener errorlistener) {
        super(method, url, errorlistener);
        this.params = params;
        this.listener = listener;
    }

    @Override
    protected Response parseNetworkResponse(NetworkResponse networkResponse) {
        T t = null;

        try {
            String json = new String(networkResponse.data,
                    HttpHeaderParser.parseCharset(networkResponse.headers));

            if (!TextUtils.isEmpty(json)) {

                if (json.matches("^\\{.*\\}$")) {
                    if (type != null) {
                        t = JSON.parseObject(json, type);
                    } else if (reference != null) {
                        t = JSON.parseObject(json, reference);
                    }
                    return Response.success(t, HttpHeaderParser.parseCacheHeaders(networkResponse));
                } else if (json.matches("^\\[\\{.*\\}\\]$") && reference != null) {


                } else {
                    //                throw new IllegalArgumentException("无法匹配json 格式");
                    return Response.error(new VolleyError("无法匹配json 格式"));
                }

            }
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();

        }
        return Response.error(new VolleyError("json 为空"));

    }


    protected void deliverResponse(T t) {
        listener.onResponse(t);
    }



    @Override
    public Map<String, String> getParams() {
        if (!TextUtils.isEmpty(params) && params.matches("^\\{.*\\}$")) {
            return JSON.parseObject(params, new TypeReference<Map<String, String>>() {
            });
        }
        Log.w("FastJsonRequest", "------params format error--------");
        return null;
    }


}
