package com.bob.volleysample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.bob.volleysample.bean.Weather;
import com.bob.volleysample.bean.WeatherInfo;
import com.bob.volleysample.request.GsonRequest;

/**
 * com.bob.volleysample
 * Created by BOB on 2017/2/11.
 * 描述：展示GsonRequest的界面
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */

public class GsonRequestActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView tv_gson_request;
    private RequestQueue mQueue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.gson_request);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_gson_request = (TextView) findViewById(R.id.tv_gson_request);
        mQueue = Volley.newRequestQueue(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.gson_request));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        GsonRequest<Weather> gsonRequest=new GsonRequest<Weather>("http://www.weather.com.cn/data/sk/101010100.html"
                , Weather.class,
                new Response.Listener<Weather>() {
                    @Override
                    public void onResponse(Weather weather) {
                        WeatherInfo weatherInfo = weather.getWeatherInfo();
                        if (weatherInfo != null) {
                            StringBuffer str = new StringBuffer();
                            str.append("城市：" + weatherInfo.getCity() + "\n");
                            str.append("温度：" + weatherInfo.getTemp() + "\n");
                            str.append("时间：" + weatherInfo.gettime());
                            tv_gson_request.setText(str);
                            Log.d("SUCCESS", "成功返回GsonRequest  " + str.toString());
                        } else {
                            Log.d("ERROR", "weatherinfo对象为空");
                        }
                    }
                }
                , new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "返回GsonRequest失败");
                tv_gson_request.setText(getResources().getString(R.string.error_message));
            }
        }
        );

        mQueue.add(gsonRequest);
    }
}
