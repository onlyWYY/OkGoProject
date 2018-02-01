package com.star.frame.netframework.net;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.cache.CacheEntity;
import com.lzy.okgo.cache.CacheMode;
import com.lzy.okgo.cookie.CookieJarImpl;
import com.lzy.okgo.cookie.store.SPCookieStore;
import com.lzy.okgo.https.HttpsUtils;
import com.lzy.okgo.interceptor.HttpLoggingInterceptor;
import com.star.frame.netframework.MainApplication;

import java.util.concurrent.TimeUnit;
import java.util.logging.Level;

import okhttp3.OkHttpClient;

/**
 * OkGoClient 初始化 配置
 * Created by suxing on 2018/1/29.
 */

public class OkGoClient {

    private static OkGoClient INSTANCE;
    private MainApplication mApplication;

    public static synchronized OkGoClient getINSTANCE() {
        if (null == INSTANCE)
            return new OkGoClient();
        else
            return INSTANCE;
    }

    public OkGoClient() {
        mApplication = MainApplication.mApplication;
        initOkHttpClient();
    }

    public void initOkHttpClient() {
        OkHttpClient.Builder builder = new OkHttpClient.Builder();

        /* 配置 日志 信息*/
        HttpLoggingInterceptor loggingInterceptor = new HttpLoggingInterceptor(Constant.OKGO);
        //log打印级别，决定了log显示的详细程度
        //NONE:不打印log || BASIC:只打印 请求首行和响应首行 || HEADERS:打印请求和响应的所有Header || BODY:所有数据全部打印
        loggingInterceptor.setPrintLevel(HttpLoggingInterceptor.Level.BODY);
        //log颜色级别，决定了log在控制台显示的颜色
        loggingInterceptor.setColorLevel(Level.INFO);
        builder.addInterceptor(loggingInterceptor);

        /*配置 请求时间 信息*/
        //全局的读取超时时间
        builder.readTimeout(Constant.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的写入超时时间
        builder.writeTimeout(Constant.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);
        //全局的连接超时时间
        builder.connectTimeout(Constant.DEFAULT_MILLISECONDS, TimeUnit.MILLISECONDS);

        //使用sp保持cookie，如果cookie不过期，则一直有效
        builder.cookieJar(new CookieJarImpl(new SPCookieStore(mApplication)));

        /*Https 配置*/
        //方法一：信任所有证书,不安全有风险
        HttpsUtils.SSLParams sslParams1 = HttpsUtils.getSslSocketFactory();
//        //方法二：自定义信任规则，校验服务端证书
//        HttpsUtils.SSLParams sslParams2 = HttpsUtils.getSslSocketFactory(new SafeTrustManager());
//        //方法三：使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams3 = HttpsUtils.getSslSocketFactory(getAssets().open("srca.cer"));
//        //方法四：使用bks证书和密码管理客户端证书（双向认证），使用预埋证书，校验服务端证书（自签名证书）
//        HttpsUtils.SSLParams sslParams4 = HttpsUtils.getSslSocketFactory(getAssets().open("xxx.bks"), "123456", getAssets().open("yyy.cer"));
        builder.sslSocketFactory(sslParams1.sSLSocketFactory, sslParams1.trustManager);
        initOkgo(builder);
    }

    public void initOkgo(OkHttpClient.Builder builder) {
        OkGo.getInstance().init(mApplication)                      //必须调用初始化
                .setOkHttpClient(builder.build())                   //建议设置OkHttpClient，不设置将使用默认的
                .setCacheMode(CacheMode.NO_CACHE)                   //全局统一缓存模式，默认不使用缓存，可以不传
                .setCacheTime(CacheEntity.CACHE_NEVER_EXPIRE)       //全局统一缓存时间，默认永不过期，可以不传
                .setRetryCount(3)                                   //全局统一超时重连次数，默认为三次，那么最差的情况会请求4次(一次原始请求，三次重连请求)，不需要可以设置为0
//                .addCommonHeaders(headers)                          //全局公共头
//                .addCommonParams(params)                            //全局公共参数
        ;
    }
}
