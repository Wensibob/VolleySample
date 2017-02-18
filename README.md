# VolleySample

**欢迎访问我的[个人博客](http://www.wensibo.top)**    
## 配置Gradle
使用如下命令导入Volley库：
```
compile 'com.mcxiaoke.volley:library:1.0.19'
```
* 如果你还是使用Eclipse进行开发的话，可以下载[volley的jar包](http://download.csdn.net/detail/sinyu890807/7152015)导入工程。

## 添加联网许可
在AndroidManifest.xml文件中添加联网的请求
```xml
<uses-permission android:name="android.permission.INTERNET"/>
```

## 使用StringRequest
如果你需要通过网络访问的资源属于String字符串的资源，那么使用StringRequest就最为简单了，只需按照如下步骤就行了。

**① 获取一个RequestQueue**
```java
RequestQueue mQueue = Volley.newRequestQueue(this);
```
**② 构造一个StringRequest对象**
```java
 StringRequest stringRequest=new StringRequest("http://www.wensibo.top"
         , new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                //正常情况下的逻辑处理
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
                //出错的时候做的一些处理
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, StringRequestActivity.class);
                intent.putExtra("string_request", getResources().getString(R.string.error_message));
                startActivity(intent);
            }
        }
        );
```
> 由于不同的网页的编码格式不同，为了防止中文乱码情况的出现，代码中将其设置为utf-8即可得到解决。其他的Request如果要避免乱码的出现，也应该采取类似的方法进行处理。

**③ 将StringRequest对象add进RequestQueue**
```java
mQueue.add(stringRequest);
```
**看看截图**  
![StringRequest](/screenshot/1.jpg)

## 使用JsonRequest
有的时候我们不仅需要简单的String资源，还需要获取Json数据，当然Volley也帮我们提供了获取Json的接口，这个就是JsonRequest，使用JsonRequest与StringRequest类似。这里作为例子接收的是北京的天气预报的json对象，获取到的json对象直接打印出来，当然我们还可以获取里面的内容，我将会在自定义的GsonRequest中作介绍。

**① 获取一个RequestQueue**
```java
RequestQueue mQueue = Volley.newRequestQueue(this);
```
**② 构造一个JsonRequest对象**
```java
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
```
**③ 将JsonRequest对象add进RequestQueue**
```java
mQueue.add(jsonObjectRequest);
```
**看看截图**  
![JsonRequest](/screenshot/2.png)

## 使用ImageRequest
获取网络上的图片资源是一个很普通的需求，虽然Volley有这个功能，但是放到现在来看都已不是什么稀奇事了，而且功能比较单一，所以这里就简单介绍一下。

**① 获取一个RequestQueue**
```java
RequestQueue mQueue = Volley.newRequestQueue(this);
```
**② 构造一个ImageRequest对象**
```java
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
            }
        }
        );
```
>可以看到，ImageRequest的构造函数接收六个参数，第一个参数就是图片的URL地址，这个没什么需要解释的。第二个参数是图片请求成功的回调，这里我们把返回的Bitmap参数设置到ImageView中。第三第四个参数分别用于指定允许图片最大的宽度和高度，如果指定的网络图片的宽度或高度大于这里的最大值，则会对图片进行压缩，指定成0的话就表示不管图片有多大，都不会进行压缩。第五个参数用于指定图片的颜色属性，Bitmap.Config下的几个常量都可以在这里使用，其中ARGB_8888可以展示最好的颜色属性，每个图片像素占据4个字节的大小，而RGB_565则表示每个图片像素占据2个字节大小。第六个参数是图片请求失败的回调，这里我们当请求失败时在ImageView中显示一张默认图片。

**③ 将JsonRequest对象add进RequestQueue**
```java
mQueue.add(jsonObjectRequest);
```

**看看截图**  
![ImageRequest加载成功](/screenshot/3.jpg)
![ImageRequest加载失败](/screenshot/3.1.gif)

## 使用ImageLoader
Volley在请求网络图片方面除了ImageRequest之外，还有另外一个更加高效的ImageLoader。ImageLoader用于加载网络上的图片，并且它的内部也是使用ImageRequest来实现的，不过ImageLoader明显要比ImageRequest更加高效，因为它不仅可以帮我们对图片进行缓存，还可以过滤掉重复的链接，避免重复发送请求。
由于ImageLoader已经不是继承自Request的了，所以它的用法也和我们之前学到的内容有所不同，总结起来大致可以分为以下四步：

**① 获取一个RequestQueue**
```java
RequestQueue mQueue = Volley.newRequestQueue(this);
```
**② 构造一个ImageLoader对象**
```java
ImageLoader imageLoader = new ImageLoader(mQueue, new ImageCache() {  
    @Override  
    public void putBitmap(String url, Bitmap bitmap) {  
    }  
  
    @Override  
    public Bitmap getBitmap(String url) {  
        return null;  
    }  );
```
>ImageLoader的构造函数接收两个参数，第一个参数就是RequestQueue对象，第二个参数是一个ImageCache对象，这里我们先new出一个空的ImageCache的实现即可。具体的ImageCache待会讲解。

**③构造一个ImageListener对象**
```java
ImageListener listener = imageLoader.getImageListener(iv_image_loader, R.drawable.failed_image, R.drawable.failed_image);
```
>getImageListener()方法接收三个参数，第一个参数指定用于显示图片的ImageView控件，第二个参数指定加载图片的过程中显示的图片，第三个参数指定加载图片失败的情况下显示的图片。

**④调用ImageLoader的get()方法加载网络上的图片**
```java
imageLoader.get("http://wensibo.top/img/avatar.jpg", listener);
```
>最后，调用ImageLoader的get()方法来加载图片，get()方法接收两个参数，第一个参数就是图片的URL地址，第二个参数则是刚刚获取到的ImageListener对象。当然，如果你想对图片的大小进行限制，也可以使用get()方法的重载，指定图片允许的最大宽度和高度，如下所示：

```java
imageLoader.get("http://wensibo.top/img/avatar.jpg", listener,200,200);
```


### 关于ImageCache
刚才讲到ImageLoader的构造函数的第二个参数是一个ImageCache对象，刚才介绍的ImageLoader的优点也在于此。他能起到图片缓存的作用。接下来我们就自己写一个呗！
这里我们新建一个BitmapCache并实现了ImageCache接口：
```java
public class BitmapCache implements ImageCache{

    private LruCache<String, Bitmap> mCache;

    public BitmapCache() {
        //将缓存图片的大小设置为8M
        int maxSize = 8 * 1024 * 1024;
        mCache = new LruCache<String, Bitmap>(maxSize){
            @Override
            protected int sizeOf(String key, Bitmap bitmap) {
                return bitmap.getRowBytes() * bitmap.getHeight();
            }
        };
    }


    @Override
    public Bitmap getBitmap(String url) {
        return mCache.get(url);
    }

    @Override
    public void putBitmap(String url, Bitmap bitmap) {
        mCache.put(url, bitmap);
    }
}
```
>这里我们将缓存图片的大小设置为8M。接着修改创建ImageLoader实例的代码，第二个参数传入BitmapCache的实例：

```java
ImageLoader imageLoader = new ImageLoader(mQueue, new BitmapCache());
```
**看看效果吧！**  
![ImageLoader](/screenshot/5.gif)


## 从StringRequest开始讲讲思路
先上最基本的StringRequest源码
```java
public class StringRequest extends Request<String>{
    private final Response.Listener<String> mListener;

    /** 根据给定的METHOD设置对应的request. */
    public StringRequest(int method, String url, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        super(method, url, errorListener);
        mListener = listener;
    }

    /** 默认为GET请求的request. */
    public StringRequest(String url, Response.Listener<String> listener,
                         Response.ErrorListener errorListener) {
        this(Method.GET, url, listener, errorListener);
    }

    /** 将HTTP请求结果转换为String. */
    @Override
    protected Response<String> parseNetworkResponse(NetworkResponse response) {
        String parsed;

        try {
            parsed = new String(response.data, HttpHeaderParser.parseCharset(response.headers));
        } catch (UnsupportedEncodingException e) {
            parsed = new String(response.data);
        }
        return Response.success(parsed, HttpHeaderParser.parseCacheHeaders(response));
    }

    /** 将解析的String结果传递给用户的回调接口. */
    @Override
    protected void deliverResponse(String response) {
        mListener.onResponse(response);
    }
}
```
我们应该可以提炼出如下几点：
* StringRequest继承自Request类，并制定其泛型为String，那么当我们自定义XMLRequest时就应该指定类型为XmlPullParser。
* 有两个构造函数，默认使用的GET请求。
* 由于Request类中的deliverResponse()和parseNetworkResponse()是两个抽象方法，因此自定义Request中需要对这两个方法进行实现。
* deliverResponse()方法中仅仅是调用了mListener中的onResponse()方法，并将response内容传入即可，这样就可以将服务器响应的数据进行回调。
* parseNetworkResponse()方法中则是对服务器响应的数据进行解析，其中数据是以字节的形式存放在NetworkResponse的response变量中的，这里将数据取出然后组装成一个String，并传入Response的success()方法中即可。  
既然知道内部实现的逻辑，那就开始动手吧！

## 自定义XMLRequest
**① 直接上代码啦！**  
按照刚才我们解析StringRequest得到的几点结论，我们应该不难得到XMLRequest：
```java
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
```
>这里需要注意的一点我在[上一篇](http://wensibo.top/2017/02/17/%E4%B8%80%E5%8F%A3%E4%B8%80%E5%8F%A3%E5%90%83%E6%8E%89Volley%EF%BC%88%E4%BA%8C%EF%BC%89/)文章中也已经提到了，就是在解析数据的时候如果出现乱码应该转换编码为utf-8。

**② 测试接口**  
作为测试，我使用的XML接口是：http://flash.weather.com.cn/wmaps/xml/guangdong.xml ，它返回的是广东省各个城市的天气预报，你也可以将`guangdong`改为你所在的省份就可以显示其他数据了：
```xml
<guangdong dn="day">
<city cityX="137.7" cityY="385.95" cityname="湛江" centername="湛江" fontColor="FFFFFF" pyName="zhanjiang" state1="1" state2="1" stateDetailed="多云" tem1="23" tem2="16" temNow="24" windState="微风" windDir="东南风" windPower="2级" humidity="50%" time="15:30" url="101281001"/>
<city cityX="170.65" cityY="317.55" cityname="茂名" centername="茂名" fontColor="FFFFFF" pyName="maoming" state1="1" state2="1" stateDetailed="多云" tem1="26" tem2="14" temNow="26" windState="微风" windDir="西南风" windPower="1级" humidity="47%" time="15:30" url="101282001"/>
<city cityX="225" cityY="245" cityname="云浮" centername="云浮" fontColor="FFFFFF" pyName="yunfu" state1="1" state2="1" stateDetailed="多云" tem1="26" tem2="13" temNow="26" windState="微风" windDir="东风" windPower="2级" humidity="43%" time="15:30" url="101281401"/>
<city cityX="226.55" cityY="304.5" cityname="阳江" centername="阳江" fontColor="FFFFFF" pyName="yangjiang" state1="0" state2="1" stateDetailed="晴转多云" tem1="24" tem2="15" temNow="23" windState="微风" windDir="东南风" windPower="3级" humidity="63%" time="15:30" url="101281801"/>
<city cityX="275.35" cityY="214.65" cityname="肇庆" centername="肇庆" fontColor="FFFFFF" pyName="zhaoqing" state1="1" state2="1" stateDetailed="多云" tem1="26" tem2="14" temNow="26" windState="微风" windDir="西北风" windPower="1级" humidity="43%" time="15:30" url="101280901"/>
<city cityX="291" cityY="285" cityname="江门" centername="江门" fontColor="FFFFFF" pyName="jiangmen" state1="0" state2="0" stateDetailed="晴" tem1="26" tem2="15" temNow="26" windState="微风" windDir="东风" windPower="2级" humidity="38%" time="15:30" url="101281101"/>
<city cityX="313.3" cityY="160.45" cityname="清远" centername="清远" fontColor="FFFFFF" pyName="qingyuan" state1="1" state2="1" stateDetailed="多云" tem1="25" tem2="15" temNow="26" windState="微风" windDir="南风" windPower="2级" humidity="44%" time="15:30" url="101281301"/>
<city cityX="308.7" cityY="225" cityname="佛山" centername="佛山" fontColor="FFFFFF" pyName="foshan" state1="0" state2="1" stateDetailed="晴转多云" tem1="26" tem2="14" temNow="26" windState="微风" windDir="北风" windPower="1级" humidity="42%" time="15:30" url="101280800"/>
<city cityX="342.7" cityY="255" cityname="中山" centername="中山" fontColor="FFFFFF" pyName="zhongshan" state1="1" state2="1" stateDetailed="多云" tem1="25" tem2="14" temNow="26" windState="微风" windDir="西北风" windPower="2级" humidity="41%" time="15:30" url="101281701"/>
<city cityX="340.55" cityY="300" cityname="珠海" centername="珠海" fontColor="FFFFFF" pyName="zhuhai" state1="1" state2="1" stateDetailed="多云" tem1="23" tem2="17" temNow="23" windState="微风" windDir="东风" windPower="2级" humidity="48%" time="15:30" url="101280701"/>
<city cityX="352.6" cityY="80" cityname="韶关" centername="韶关" fontColor="FFFFFF" pyName="shaoguan" state1="1" state2="1" stateDetailed="多云" tem1="25" tem2="14" temNow="27" windState="微风" windDir="西风" windPower="1级" humidity="34%" time="15:30" url="101280201"/>
<city cityX="353" cityY="196" cityname="广州" centername="广州" fontColor="FFFF00" pyName="guangzhou" state1="0" state2="0" stateDetailed="晴" tem1="26" tem2="14" temNow="26" windState="微风" windDir="南风" windPower="1级" humidity="40%" time="15:20" url="101280101"/>
<city cityX="377" cityY="234" cityname="东莞" centername="东莞" fontColor="FFFFFF" pyName="dongguan" state1="0" state2="1" stateDetailed="晴转多云" tem1="25" tem2="15" temNow="26" windState="微风" windDir="北风" windPower="1级" humidity="37%" time="15:30" url="101281601"/>
<city cityX="409" cityY="257" cityname="深圳" centername="深圳" fontColor="FFFFFF" pyName="shenzhen" state1="0" state2="0" stateDetailed="晴" tem1="24" tem2="15" temNow="24" windState="微风" windDir="南风" windPower="3级" humidity="49%" time="15:30" url="101280601"/>
<city cityX="423.85" cityY="214.65" cityname="惠州" centername="惠州" fontColor="FFFFFF" pyName="huizhou" state1="1" state2="0" stateDetailed="多云转晴" tem1="26" tem2="13" temNow="26" windState="微风" windDir="南风" windPower="1级" humidity="38%" time="15:20" url="101280301"/>
<city cityX="442.55" cityY="141.6" cityname="河源" centername="河源" fontColor="FFFFFF" pyName="heyuan" state1="0" state2="1" stateDetailed="晴转多云" tem1="25" tem2="15" temNow="27" windState="微风" windDir="西南风" windPower="1级" humidity="37%" time="15:30" url="101281201"/>
<city cityX="492" cityY="217" cityname="汕尾" centername="汕尾" fontColor="FFFFFF" pyName="shanwei" state1="1" state2="0" stateDetailed="多云转晴" tem1="24" tem2="14" temNow="24" windState="东南风3-4级转微风" windDir="东风" windPower="2级" humidity="45%" time="15:30" url="101282101"/>
<city cityX="522.55" cityY="110.45" cityname="梅州" centername="梅州" fontColor="FFFFFF" pyName="meizhou" state1="1" state2="0" stateDetailed="多云转晴" tem1="27" tem2="12" temNow="26" windState="微风" windDir="东风" windPower="1级" humidity="36%" time="15:30" url="101280401"/>
<city cityX="526.8" cityY="182" cityname="揭阳" centername="揭阳" fontColor="FFFFFF" pyName="jieyang" state1="0" state2="0" stateDetailed="晴" tem1="26" tem2="13" temNow="26" windState="微风" windDir="东风" windPower="2级" humidity="34%" time="15:30" url="101281901"/>
<city cityX="579" cityY="137.45" cityname="潮州" centername="潮州" fontColor="FFFFFF" pyName="chaozhou" state1="0" state2="0" stateDetailed="晴" tem1="25" tem2="12" temNow="26" windState="微风" windDir="东南风" windPower="2级" humidity="38%" time="15:30" url="101281501"/>
<city cityX="566.45" cityY="179.25" cityname="汕头" centername="汕头" fontColor="FFFFFF" pyName="shantou" state1="0" state2="1" stateDetailed="晴转多云" tem1="24" tem2="13" temNow="24" windState="东北风转东风小于3级" windDir="东风" windPower="2级" humidity="54%" time="15:30" url="101280501"/>
</guangdong>
```

**③ 调用XMLRequest，并加入RequestQueue**  
代码中我将解析XML数据的城市展示在List View中：
```java
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
                                        }
                                        break;
                                }
                                eventType = response.next();
                            }

                            ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(XMLRequestActivity.this, android.R.layout.simple_list_item_1, citys);
                            lv_xml_request.setAdapter(arrayAdapter);
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
```

**人品爆发，看截图**  
![XMLRequest](/screenshot/5.png)



## 自定义GsonRequest
虽然Volley提供了JsonRequest为我们解析Json，但是使用JSONObject还是太麻烦了，还有很多方法可以让JSON数据解析变得更加简单，比如说GSON，所以何不如自定义一个GsonRequest呢？思路也是大同小异！

**① 直接上代码啦！**  
```java
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
```
>同样需要注意编码的问题。在parseNetworkResponse()方法中，先是将服务器响应的数据解析出来，然后通过调用Gson的fromJson方法将数据组装成对象。在deliverResponse方法中仍然是将最终的数据进行回调。  

* 记得为工程加入gson库

```
compile 'com.google.code.gson:gson:2.8.0'
```
**② json接口**  
使用之前使用的json接口：http://www.weather.com.cn/data/sk/101010100.html ，它返回的数据是一个叫做weatherinfo的jsonObject：
```
{"weatherinfo":{"city":"北京","cityid":"101010100","temp":"19","WD":"南风","WS":"2级","SD":"43%","WSE":"2","time":"19:45","isRadar":"1","Radar":"JC_RADAR_AZ9010_JB"}} 
```
**③ 新建bean类**  
由于返回的jsonObject名字叫做weatherinfo，所以我们新建一个WeatherInfo类，定义了几个最基本的属性就行了：
```java
public class WeatherInfo{
    private String city;
    private String temp;
    private String time;

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getTemp() {
        return temp;
    }

    public void setTemp(String temp) {
        this.temp = temp;
    }

    public String gettime() {
        return time;
    }

    public void settime(String time) {
        this.time = time;
    }
}
```
另外还需要再定义一个Weather类，来获取WeatherInfo对象：
```java
public class Weather {
    //此处的weatherinfo不可以修改为weatherInfo，因为从json获取的数据格式为
    // {"weatherinfo":{"city":"北京","cityid":"101010100","temp":"19","WD":"南风","WS":"2级","SD":"43%","WSE":"2","time":"19:45","isRadar":"1","Radar":"JC_RADAR_AZ9010_JB"}}
    private WeatherInfo weatherinfo;

    public WeatherInfo getWeatherInfo() {
        return weatherinfo;
    }

    public void setWeatherInfo(WeatherInfo weatherinfo) {
        this.weatherinfo = weatherinfo;
    }
}
```  
>**此处的weatherinfo不可以修改为weatherInfo**

**④调用GsonRequest**   
我们将获取的数据显示在Text View上就行了。
```java
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
```
**看截图啦！**  
![GsonRequest](/screenshot/6.png)
