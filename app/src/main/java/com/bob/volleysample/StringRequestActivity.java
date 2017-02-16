package com.bob.volleysample;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.text.method.ScrollingMovementMethod;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

/**
 * com.bob.volleysample
 * Created by BOB on 2017/2/10.
 * 描述：展示StringRequest的界面
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */

public class StringRequestActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private TextView tv_string_request;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.string_request);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_string_request = (TextView) findViewById(R.id.tv_string_request);
        tv_string_request.setMovementMethod(ScrollingMovementMethod.getInstance());

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("String Request");
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String str = bundle.getString("string_request");
        tv_string_request.setText(str);
        Log.d("SUCCESS", "String Request内容为：" + str);

    }
}
