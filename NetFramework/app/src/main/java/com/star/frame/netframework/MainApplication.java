package com.star.frame.netframework;

import android.app.Application;

import com.lzy.okgo.OkGo;
import com.star.frame.netframework.net.OkGoClient;

/**
 * Created by suxing on 2018/1/19.
 */

public class MainApplication extends Application {

    public static MainApplication mApplication;

    @Override
    public void onCreate() {
        super.onCreate();
        this.mApplication = this;
        initOkGo();
    }

    /**
     * 初始化 OkGo
     */
    private void initOkGo() {
        OkGo.getInstance().init(this);
        OkGoClient.getINSTANCE();
    }
}
