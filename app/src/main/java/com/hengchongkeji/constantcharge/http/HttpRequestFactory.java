package com.hengchongkeji.constantcharge.http;

import android.content.Context;

/**
 * Created by gopayChan on 2017/5/1.
 */

public class HttpRequestFactory {

    private static HttpRequestFactory mInstance;
    private IHttpRequest mRequest;

    private HttpRequestFactory(Context context) {
        mRequest = new OkHttpRequest(context);
    }

    public static HttpRequestFactory getInstance(Context context) {
        if (mInstance == null) {
            synchronized (HttpRequestFactory.class){
                if(mInstance == null){
                    mInstance = new HttpRequestFactory(context);
                }
            }
        }
        return mInstance;
    }

    public IHttpRequest getRequest() {
        return mRequest;
    }
}
