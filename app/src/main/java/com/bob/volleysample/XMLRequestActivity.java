package com.bob.volleysample;

import android.os.Bundle;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bob.volleysample.request.XMLRequest;

import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

/**
 * com.bob.volleysample
 * Created by BOB on 2017/2/11.
 * 描述：展示XMLRequest的界面
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */

public class XMLRequestActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ListView lv_xml_request;
    private List<String> citys;
    private RequestQueue mQueue;
    private TextView mTextView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.xml_request);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        lv_xml_request = (ListView) findViewById(R.id.lv_xml_request);
        mTextView = (TextView) findViewById(R.id.tv_gone);
        mQueue = Volley.newRequestQueue(this);
        citys = new ArrayList<String>();

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.xml_request));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        XMLRequest xmlRequest = new XMLRequest(
                "http://flash.weather.com.cn/wmaps/xml/guangdong.xml",
                new Response.Listener<XmlPullParser>() {
                    @Override
                    public void onResponse(XmlPullParser response) {
                        try {
                            int eventType = response.getEventType();
                            while (eventType != XmlPullParser.END_DOCUMENT) {
                                switch (eventType) {
                                    case XmlPullParser.START_TAG:
                                        String nodeName = response.getName();
                                        if ("city".equals(nodeName)) {
                                            String pname = response.getAttributeValue(2);
                                            Log.d("CITY", "PName is" + pname);
                                            citys.add(pname);
                                            Log.d("SUCCESS", "遍历的时候citys集合的大小为" + citys.size());
                                        }
                                        break;
                                }
                                eventType = response.next();
                            }

                            Log.d("SUCCESS", "最终citys集合的大小为" + citys.size());
                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(XMLRequestActivity.this, android.R.layout.simple_list_item_1, citys);
                            lv_xml_request.setAdapter(arrayAdapter);
                            Log.d("SUCCESS", "UI更新完毕！");

                        } catch (XmlPullParserException e) {
                            e.printStackTrace();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        lv_xml_request.setVisibility(View.GONE);
                        mTextView.setVisibility(View.VISIBLE);
                        Log.d("ERROR", "返回XMLRequest失败");
                    }
                }
        );
        mQueue.add(xmlRequest);
    }

}

