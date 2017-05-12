package com.hengchongkeji.constantcharge.http;

import android.content.Context;

/**
 * Created by gopayChan on 2017/5/10.
 */

public abstract class BaseAction {


    protected static String BASE_URL = "http://www.everest-energy.cn/app/";

    protected static IHttpRequest getRequest(Context context) {
        return HttpRequestFactory.getInstance(context).getRequest();
    }
}
