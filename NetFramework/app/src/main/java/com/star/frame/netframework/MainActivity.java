package com.star.frame.netframework;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.star.frame.netframework.net.Apis;
import com.star.frame.netframework.net.OkGoManager;

import java.util.HashMap;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 主页面
 * Created by suxing on 2018/1/17.
 */

public class MainActivity extends Activity {

    @BindView(R.id.main_button1)
    Button mButton1;
    @BindView(R.id.main_button2)
    Button mButton2;
    @BindView(R.id.main_text)
    TextView mText;
    @BindView(R.id.main_edit)
    EditText mEdit;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.main_button1, R.id.main_button2})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.main_button1:
                HashMap<String, String> params = new HashMap<>();
                params.put("verson", "1.0.0");
                params.put("os", "android");
                OkGoManager.INSTANCE.post(this,
                        Apis.API_URL_CONFIG,
                        params,
                        false,
                        new OkGoManager.OnRequestCallBack<String>() {
                            @Override
                            public void onSuccess(String response) {
                                mText.setText(response);
                            }

                            @Override
                            public void onError(String error, Exception e) {
                                mText.setText(error);
                            }

                            @Override
                            public void onLoginOut() {

                            }
                        });
                break;
            case R.id.main_button2:
                HashMap<String, String> params2 = new HashMap<>();
                params2.put("phone", "17600100517");
                params2.put("passwd", "123456");
                OkGoManager.INSTANCE.post(this,
                        Apis.API_URL_LOGIN,
                        params2,
                        false,
                        new OkGoManager.OnRequestCallBack<String>() {
                            @Override
                            public void onSuccess(String response) {
                                mText.setText(response);
                            }

                            @Override
                            public void onError(String error, Exception e) {
                                mText.setText(error);
                            }

                            @Override
                            public void onLoginOut() {

                            }
                        });
                break;
        }
    }
}
