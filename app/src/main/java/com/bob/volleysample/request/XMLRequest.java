package com.bob.volleysample.request;

import android.util.Log;

import com.android.volley.NetworkResponse;
import com.android.volley.ParseError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.toolbox.HttpHeaderParser;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.StringReader;
import java.io.UnsupportedEncodingException;

/**
 * com.bob.volleysample.request
 * Created by BOB on 2017/2/11.
 * 描述：自定义的XMLRequest
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */

public class XMLRequest extends Request<XmlPullParser> {

    private final Response.Listener<XmlPullParser> mListener;

    public XMLRequest(int method, String url, Response.Listener<XmlPullParser> listener, Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    public XMLRequest( String url, Response.Listener<XmlPullParser> listener, Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    @Override
    protected Response<XmlPullParser> parseNetworkResponse(NetworkResponse response) {
        try {
            response.headers.put("HTTP.CONTENT_TYPE", "utf-8");
            String xmlString = new String(response.data,"utf-8");
            //加上这两行可以解决乱码的问题，尤其是对于中文的xml接口，由于每个xml的编码格式不同，所以获取原编码格式之后再转换为utf-8，即可。
            XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
            XmlPullParser xmlPullParser = factory.newPullParser();
            xmlPullParser.setInput(new StringReader(xmlString));
            Log.d("SUCCESS", "xmlString的内容为" + xmlString);
            return Response.success(xmlPullParser, HttpHeaderParser.parseCacheHeaders(response));
        } catch (UnsupportedEncodingException e) {
            return Response.error(new ParseError(e));
        } catch (XmlPullParserException e) {
            return Response.error(new ParseError(e));
        }

    }

    @Override
    protected void deliverResponse(XmlPullParser response) {
        mListener.onResponse(response);
    }

}

