package com.hengchongkeji.constantcharge.http;

import com.google.gson.reflect.TypeToken;

/**
 * Created by gopayChan on 2017/5/1.
 */

public interface IHttpRequest {

    void post(String url, String[] keys, String[] values, OnResponseListener listener);


    void post(String url, String[] keys, String[] values, TypeToken token, OnResponseListener listener);


    void get();

    public static interface OnResponseListener<T> {

        void onSuccess(T t);

        void onFail(String errorMsg);
    }
}
