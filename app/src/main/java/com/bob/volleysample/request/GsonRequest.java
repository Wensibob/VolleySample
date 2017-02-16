package com.bob.volleysample.request;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;
import com.google.gson.Gson;

import java.io.UnsupportedEncodingException;

/**
 * com.bob.volleysample.request
 * Created by BOB on 2017/2/11.
 * 描述：自定义的GsonRequest，同理可以自定义FastJson
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */

public class GsonRequest<T> extends Request<T> {

    private final Response.Listener<T> mListener;
    private Gson mGson;
    private Class<T> mClazz;

    public GsonRequest(int method, String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mGson = new Gson();
        mClazz = clazz;
        mListener = listener;
    }

    public GsonRequest(String url, Class<T> clazz, Response.Listener<T> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, clazz, listener, errorListener);
    }


    @Override
    protected Response<T> parseNetworkResponse(NetworkResponse response) {
        try {
            response.headers.put("HTTP.CONTENT_TYPE", "utf-8");
            String jsonString = new String(response.data,"utf-8");
            //加上这两行可以解决乱码的问题，尤其是对于中文的json接口，由于每个网页的编码格式不同，所以获取原编码格式之后再转换为utf-8，即可。
            return Response.success(mGson.fromJson(jsonString, mClazz),
                    HttpHeaderParser.parseCacheHeaders(response)
            );
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        }
    }

    @Override
    protected void deliverResponse(T response) {
        mListener.onResponse(response);
    }
}



