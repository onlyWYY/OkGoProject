package com.star.frame.netframework.activity;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.star.frame.netframework.R;

/**
 * Created by suxing on 2018/1/30.
 */

public class LoginActivity extends Activity{

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
    }
}
