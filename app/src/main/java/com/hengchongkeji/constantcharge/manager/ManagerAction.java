package com.hengchongkeji.constantcharge.manager;

import android.content.Context;

import com.google.gson.annotations.SerializedName;
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

    private static final String FORGET_PSW_GET_VER_CODE_URL = BASE_URL + "identifyPhone";
    private static final String FORGET_PSW_CHECK_VER_CODE_URL = BASE_URL + "phone";
    private static final String FORGET_PSW_CHANGE_PSW_URL = BASE_URL + "findPassword";

    private static final String CHANGE_PSW_URL = BASE_URL + "updatePassword";

    private static final String ALI_PAY_URL = BASE_URL + "alipay/generatePay";

    private static final String WECHAT_PAY_URL = BASE_URL + "tenpay/prepay";


    static void getVerCode(Context context, String phoneNum, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(GET_VER_CODE_URL, new String[]{"phone"}, new String[]{phoneNum}, listener);
    }

    static void register(Context context, String phone, String password, String identifyCode, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(REGISTER_URL, new String[]{"password", "phone", "identifyCode"}, new String[]{password, phone, identifyCode}, TypeToken.get(LoginResponse.class), listener);
    }

    public static void login(Context context, String phone, String password, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(LOGIN_URL, new String[]{"phone", "password"}, new String[]{phone, password}, TypeToken.get(LoginResponse.class), listener);
    }

    static void changePsw(Context context, String phone, String oldPsw, String newPsw, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(CHANGE_PSW_URL, new String[]{"oldPassword", "password", "confirmPassword"}, new String[]{oldPsw, newPsw, newPsw}, listener);
    }

    static void forgetPswGetVerCode(Context context, String phone, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(FORGET_PSW_GET_VER_CODE_URL, new String[]{"phone"}, new String[]{phone}, listener);
    }

    static void forgetPswCheckVerCode(Context context, String phone, String verCode, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(FORGET_PSW_CHECK_VER_CODE_URL, new String[]{"phone", "identifyCode"}, new String[]{phone, verCode}, listener);
    }

    static void forgetPswChangePsw(Context context, String newPsw, String confirmPsw, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(FORGET_PSW_CHANGE_PSW_URL, new String[]{"password", "confirmPassword"}, new String[]{newPsw, confirmPsw}, listener);
    }

    static void getAliPayInfo(Context context, String totalAmount, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(ALI_PAY_URL, new String[]{"totalAmount"}, new String[]{totalAmount}, TypeToken.get(AliPayResponse.class), listener);
    }

    static void getWechatInfo(Context context, String total_fee, IHttpRequest.OnResponseListener listener) {
        getRequest(context).post(WECHAT_PAY_URL, new String[]{"total_fee"}, new String[]{total_fee}, TypeToken.get(WechatResponse.class), listener);
    }

    static class LoginResponse extends BaseResponse {

        public User getAppCustomer() {
            return appCustomer;
        }

        @Override
        public String toString() {
            return "LoginResponse{" +
                    "appCustomer=" + appCustomer +
                    '}';
        }

        public User appCustomer;
    }

    static class AliPayResponse extends BaseResponse {

        private String data;

        public String getData() {
            return data;
        }
    }

    static class WechatResponse extends BaseResponse {

        @SerializedName("noncestr")
        private String nonce_str;
        @SerializedName("package")
        private String package_;
        @SerializedName("timestamp")
        private String time_start;
        private String appid;
        private String sign;
        private String prepayid;
        private String partnerid;

        public String getSign() {
            return sign;
        }

        public void setSign(String sign) {
            this.sign = sign;
        }

        @Override
        public String toString() {
            return "WechatResponse{" +
                    "nonce_str='" + nonce_str + '\'' +
                    ", package_='" + package_ + '\'' +
                    ", time_start='" + time_start + '\'' +
                    ", appid='" + appid + '\'' +
                    ", sign='" + sign + '\'' +
                    ", prepayid='" + prepayid + '\'' +
                    ", partnerid='" + partnerid + '\'' +
                    '}';
        }

        public String getNonce_str() {
            return nonce_str;
        }

        public void setNonce_str(String nonce_str) {
            this.nonce_str = nonce_str;
        }

        public String getPackage_() {
            return package_;
        }

        public void setPackage_(String package_) {
            this.package_ = package_;
        }

        public String getPrepayid() {
            return prepayid;
        }

        public void setPrepayid(String prepayid) {
            this.prepayid = prepayid;
        }

        public String getPartnerid() {
            return partnerid;
        }

        public void setPartnerid(String partnerid) {
            this.partnerid = partnerid;
        }

        public String getTime_start() {
            return time_start;
        }

        public void setTime_start(String time_start) {
            this.time_start = time_start;
        }

        public String getAppid() {
            return appid;
        }

        public void setAppid(String appid) {
            this.appid = appid;
        }
    }
}
