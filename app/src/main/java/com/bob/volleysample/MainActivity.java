package com.bob.volleysample;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
/**
 * com.bob.volleysample.bean
 * Created by BOB on 2017/2/11.
 * 描述：MainActivity
 * 博客园：http://www.cnblogs.com/ghylzwsb/
 * 个人网站：www.wensibo.top
 */
public class MainActivity extends AppCompatActivity {
    private Toolbar mToolbar;
    private Button button1;
    private Button button2;
    private Button button3;
    private Button button4;
    private Button button5;
    private Button button6;
    private TextView mTextView;

    private RequestQueue mQueue;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mQueue = Volley.newRequestQueue(this);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        button1 = (Button) findViewById(R.id.button1);
        button2 = (Button) findViewById(R.id.button2);
        button3 = (Button) findViewById(R.id.button3);
        button4 = (Button) findViewById(R.id.button4);
        button5 = (Button) findViewById(R.id.button5);
        button6 = (Button) findViewById(R.id.button6);

        setSupportActionBar(mToolbar);
        mToolbar.setTitle("Volley");

    }

    /**
     * 使用volley的string request
     * @param view
     */
    public void button1(View view) {
        StringRequest stringRequest=new StringRequest("http://www.wensibo.top"
         , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                String result="";
                try {
                     result= new String(response.getBytes("ISO_8859_1"), "utf-8");
                    //由于访问string的时候可能会出现乱码情况，所以保险起见，可以将其转换为utf-8格式
                    //对于其他的自定义Requet都是同理的，均需要在重写parseNetworkResponse方法时设置编码格式，避免乱码的出现
                } catch (UnsupportedEncodingException e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StringRequestActivity.class);
                intent.putExtra("string_request", result);
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StringRequestActivity.class);
                intent.putExtra("string_request", getResources().getString(R.string.error_message));
                startActivity(intent);
            }
        }
        );
        mQueue.add(stringRequest);
    }

    /**
     * 使用volley的json request
     * @param view
     */
    public void button2(View view) {
        JsonObjectRequest jsonObjectRequest=new JsonObjectRequest("http://www.weather.com.cn/data/cityinfo/101010100.html"
                , new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                Log.d("JsonObject", response.toString());
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, JsonRequestActivity.class);
                intent.putExtra("json_request", response.toString());
                startActivity(intent);
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("TAG", error.getMessage(), error);
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, JsonRequestActivity.class);
                intent.putExtra("json_request", getResources().getString(R.string.error_message));
                startActivity(intent);
            }
        });
        mQueue.add(jsonObjectRequest);
    }

    /**
     * 使用Volley的Image Request
     * @param view
     */
    public void button3(View view) {
        startActivity(new Intent(MainActivity.this, ImageRequestActivity.class));
    }

    /**
     * 使用Volley的ImageLoader
     * @param view
     */
    public void button4(View view) {
        startActivity(new Intent(MainActivity.this, ImageLoaderActivity.class));
    }

    /**
     * 自定义的xml request
     * @param view
     */
    public void button5(View view) {
        startActivity(new Intent(MainActivity.this, XMLRequestActivity.class));
    }

    /**
     * 自定义的gson request
     * @param view
     */
    public void button6(View view) {
        startActivity(new Intent(MainActivity.this, GsonRequestActivity.class));
    }

}
