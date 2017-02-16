package com.bob.volleysample;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.View;
import android.widget.TextView;

/**
 * com.bob.volleysample
 * Created by BOB on 2017/2/11.
 * 描述：展示JsonRequest的界面
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */

public class JsonRequestActivity extends AppCompatActivity {

    private Toolbar mToolbar;
    private TextView tv_json_request;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.json_request);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        tv_json_request = (TextView) findViewById(R.id.tv_json_request);

        setSupportActionBar(mToolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle(getResources().getString(R.string.json_request));
        mToolbar.setNavigationOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        Intent intent = getIntent();
        Bundle bundle = intent.getExtras();
        String str = bundle.getString("json_request");
        tv_json_request.setText(str);
    }
}
