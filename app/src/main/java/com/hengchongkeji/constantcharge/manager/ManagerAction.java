package com.hengchongkeji.constantcharge.manager;

import android.content.Context;

import com.hengchongkeji.constantcharge.http.HttpRequestFactory;
import com.hengchongkeji.constantcharge.http.IHttpRequest;

/**
 * Created by gopayChan on 2017/5/1.
 */

public class ManagerAction {

    private void ManagerAction() {
    }

    private static final String BASE_URL = "http://www.everest-energy.cn/app/";
    private static final String GET_VER_CODE_URL = BASE_URL + "getIdentifyCode";
    private static final String REGISTER_URL = BASE_URL + "register";
    private static final String LOGIN_URL = BASE_URL + "login";

    static void getVerCode(Context context, String phoneNum, IHttpRequest.OnResponseListener listener) {
        HttpRequestFactory.getInstance(context).getRequest().post(GET_VER_CODE_URL, new String[]{"phone"}, new String[]{phoneNum}, listener);
    }

    static void register(Context context, String nick,String phone, String password, String identifyCode, IHttpRequest.OnResponseListener listener) {
        HttpRequestFactory.getInstance(context).getRequest().post(REGISTER_URL, new String[]{"username", "password", "phone", "identifyCode"}, new String[]{nick, password, phone, identifyCode}, listener);
    }

    static void login(Context context, String username, String password, IHttpRequest.OnResponseListener listener) {
        HttpRequestFactory.getInstance(context).getRequest().post(LOGIN_URL, new String[]{"username", "password"}, new String[]{username, password}, listener);
    }
}
