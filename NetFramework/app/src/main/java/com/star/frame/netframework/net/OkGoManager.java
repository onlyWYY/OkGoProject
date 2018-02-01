package com.star.frame.netframework.net;

import android.app.Dialog;
import android.content.Context;
import android.util.Log;

import com.lzy.okgo.OkGo;
import com.lzy.okgo.callback.StringCallback;
import com.lzy.okgo.request.base.Request;
import com.star.frame.netframework.R;
import com.star.frame.netframework.net.bean.BaseBean;

import java.util.Map;

/**
 * 网络请求管理类（get post upload download...）
 * Created by suxing on 2018/1/29.
 */

public enum OkGoManager {
    INSTANCE;

    private Dialog dialog;
    private Context mContext = null;

    public interface OnRequestCallBack<T> {
        // 请求成功
        void onSuccess(T response);

        // 请求错误
        void onError(String error, Exception e);

        // 未登录
        void onLoginOut();
    }

    public void get(Context context, String url, Map<String, String> params, boolean isNeedToken, OnRequestCallBack callBack) {
        this.mContext = context;
        getRequest(url, params, isNeedToken, callBack);
    }

    public void post(Context context, String url, Map<String, String> params, boolean isNeedToken, OnRequestCallBack callBack) {
        this.mContext = context;
        postRequest(url, params, isNeedToken, callBack);
    }

    private void getRequest(String apiUrl, Map<String, String> params, boolean isNeedToken, final OnRequestCallBack callBack) {
        Log.e(Constant.OKGO, "请求地址=" + apiUrl);
        Log.e(Constant.OKGO, "请求参数=" + params.toString());
        // 是否登录 拦截
//        if (UserHelper.getCurrentToken() != null && isNeedToken) {
//            params.put("key", UserHelper.getCurrentToken());
//        }
        OkGo.<String>get(Apis.HTTPS_URL + apiUrl).params(params).execute(new StringCallback() {
            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                success(response.body(), callBack);
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                error(callBack);
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                startRequestDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                finishRequestDialog();
            }
        });
    }

    private void postRequest(String apiUrl, Map<String, String> params, boolean isNeedToken, final OnRequestCallBack callBack) {
        Log.e(Constant.OKGO, "请求地址=" + apiUrl);
        Log.e(Constant.OKGO, "请求参数=" + params.toString());
        // 是否登录 拦截
//        if (UserHelper.getCurrentToken() != null) {
//            params.put("key", UserHelper.getCurrentToken());
//        }
        OkGo.<String>post(Apis.HTTPS_URL + apiUrl).params(params).execute(new StringCallback() {

            @Override
            public void onSuccess(com.lzy.okgo.model.Response<String> response) {
                success(response.body(), callBack);
            }

            @Override
            public void onError(com.lzy.okgo.model.Response<String> response) {
                error(callBack);
            }

            @Override
            public void onStart(Request<String, ? extends Request> request) {
                super.onStart(request);
                startRequestDialog();
            }

            @Override
            public void onFinish() {
                super.onFinish();
                finishRequestDialog();
            }
        });
    }

    private void success(String response, OnRequestCallBack callBack) {
        int responseCode = GsonUtil.GsonValueFromKey(response, "code");
        if (responseCode == 200) { // 成功
            callBack.onSuccess(response);
        } else if (401 == responseCode) { // 未登录
            callBack.onLoginOut();
        } else { // 失败
            BaseBean baseBean = GsonUtil.GsonToBean(response, BaseBean.class);
            callBack.onError(baseBean.msg, null);
        }
    }

    private void error(OnRequestCallBack callBack) {
        callBack.onError("网络连接异常", null);
    }

    /**
     * 开启 加载框
     */
    private void startRequestDialog() {
        dialog = new Dialog(mContext, R.style.netRequestDialog);
        dialog.setContentView(R.layout.wait_progress_dialog);
        dialog.setCanceledOnTouchOutside(false);
        dialog.show();
    }

    /**
     * 结束 加载框
     */
    private void finishRequestDialog() {
        if (null != dialog && dialog.isShowing())
            dialog.dismiss();
    }
}