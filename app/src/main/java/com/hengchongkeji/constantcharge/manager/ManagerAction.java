package com.hengchongkeji.constantcharge.manager;

import android.content.Context;

import com.google.gson.reflect.TypeToken;
import com.hengchongkeji.constantcharge.data.entity.User;
import com.hengchongkeji.constantcharge.http.BaseAction;
import com.hengchongkeji.constantcharge.http.BaseResponse;
import com.hengchongkeji.constantcharge.http.IHttpRequest;

/**
 * Created by gopayChan on 2017/5/1.
 */

public class ManagerAction extends BaseAction {

    private void ManagerAction() {
    }

    private static final String GET_VER_CODE_URL = BASE_URL + "getIdentifyCode";
    private static final String REGISTER_URL = BASE_URL + "register";
    private static final String LOGIN_URL = BASE_URL + "login";

    private static final String FORGET_PSW_GET_VER_CODE = BASE_URL + "identifyPhone";
    private static final String FORGET_PSW_CHECK_VER_CODE = BASE_URL + "phone";
    private static final String FORGET_PSW_CHANGE_PSW = BASE_URL + "findPassword";

    private static final String CHANGE_PSW = BASE_URL + "updatePassword";

    static void getVerCode(Context context, String phoneNum, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(GET_VER_CODE_URL, new String[]{"phone"}, new String[]{phoneNum}, listener);
    }

    static void register(Context context, String phone, String password, String identifyCode, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(REGISTER_URL, new String[]{"password", "phone", "identifyCode"}, new String[]{password, phone, identifyCode}, TypeToken.get(LoginResponse.class), listener);
    }

    static void login(Context context, String phone, String password, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(LOGIN_URL, new String[]{"phone", "password"}, new String[]{phone, password}, TypeToken.get(LoginResponse.class), listener);
    }

    static void changePsw(Context context, String phone, String oldPsw, String newPsw, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(CHANGE_PSW, new String[]{"oldPassword", "password", "confirmPassword"}, new String[]{oldPsw, newPsw, newPsw}, listener);
    }

    static void forgetPswGetVerCode(Context context, String phone, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(FORGET_PSW_GET_VER_CODE, new String[]{"phone"}, new String[]{phone}, listener);
    }

    static void forgetPswCheckVerCode(Context context, String phone, String verCode, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(FORGET_PSW_CHECK_VER_CODE, new String[]{"phone", "identifyCode"}, new String[]{phone, verCode}, listener);
    }

    static void forgetPswChangePsw(Context context, String newPsw, String confirmPsw, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(FORGET_PSW_CHANGE_PSW, new String[]{"password", "confirmPassword"}, new String[]{newPsw, confirmPsw}, listener);
    }

    static class LoginResponse extends BaseResponse {

        public User getAppCustomer() {
            return appCustomer;
        }

        public void setAppCustomer(User appCustomer) {
            this.appCustomer = appCustomer;
        }

        @Override
        public String toString() {
            return "LoginResponse{" +
                    "appCustomer=" + appCustomer +
                    '}';
        }

        public User appCustomer;
    }
}
