package com.bob.volleysample;

import android.graphics.Bitmap;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;

/**
 * com.bob.volleysample
 * Created by BOB on 2017/2/11.
 * 描述：用于展示ImageRequest的界面，不同于ImageLoader，
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */

public class ImageRequestActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private ImageView iv_image_request;
    private RequestQueue mQueue;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_request);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_image_request = (ImageView) findViewById(R.id.iv_image_request);
        mQueue = Volley.newRequestQueue(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.image_request));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageRequest imageRequest=new ImageRequest("http://wensibo.top/img/avatar.jpg"
                , new Response.Listener<Bitmap>() {
            @Override
            public void onResponse(Bitmap response) {
                Log.d("SUCCESS", "成功返回ImageRequest");
                iv_image_request.setImageBitmap(response);
            }
        }, 0, 0, Bitmap.Config.RGB_565, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.d("ERROR", "返回ImageRequest失败");
                iv_image_request.setImageResource(R.drawable.failed_image);
//                drawable与bitmap互相转换的方法，详见：http://blog.csdn.net/hezhipin610039/article/details/7899248/
            }
        }
        );

        mQueue.add(imageRequest);
    }
}
