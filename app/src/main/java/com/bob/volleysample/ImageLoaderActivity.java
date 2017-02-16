package com.bob.volleysample;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.ImageView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.ImageLoader.ImageListener;
import com.android.volley.toolbox.Volley;

/**
 * com.bob.volleysample
 * Created by BOB on 2017/2/11.
 * 描述：展示ImageLoader的界面，如果网络不可用时，会加载默认的图片
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */

public class ImageLoaderActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private ImageView iv_image_loader;
    private RequestQueue mQueue;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.image_loader);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        iv_image_loader = (ImageView) findViewById(R.id.iv_image_loader);
        mQueue = Volley.newRequestQueue(this);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.image_loader));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
        ImageListener listener = imageLoader.getImageListener(iv_image_loader, R.drawable.failed_image, R.drawable.failed_image);
        imageLoader.get("http://wensibo.top/img/avatar.jpg", listener);
    }
}
