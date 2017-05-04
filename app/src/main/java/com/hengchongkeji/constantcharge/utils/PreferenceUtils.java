package com.hengchongkeji.constantcharge.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

/**
 * Created by gopayChan on 2017/4/29.
 */

public class PreferenceUtils {

    private final static String LOGIN_STATE = "login_state";
    private final static String HAS_OPEN_APP = "has_open_app";
    private final static String USER_NUMBER = "user_number";
    private final static String USER_PSW = "user_psw";
    private final static String USER_NICK = "user_nick";

    private PreferenceUtils() {

    }

    private static SharedPreferences getSharedDefaultPreferences(Context context) {
        return PreferenceManager.getDefaultSharedPreferences(context.getApplicationContext());
    }

    public static boolean getLoginState(Context context) {
        return getSharedDefaultPreferences(context).getBoolean(LOGIN_STATE, false);
    }

    public static void setLoginState(Context context, boolean loginState) {
        getSharedDefaultPreferences(context).edit().putBoolean(LOGIN_STATE, loginState).apply();
    }

    public static boolean getHasOpenApp(Context context) {
        return getSharedDefaultPreferences(context).getBoolean(HAS_OPEN_APP, false);
    }

    public static void setHasOpenApp(Context context) {
        getSharedDefaultPreferences(context).edit().putBoolean(HAS_OPEN_APP, true).apply();
    }

    public static void setUserNumber(Context context, String userNumber) {
        getSharedDefaultPreferences(context).edit().putString(USER_NUMBER, userNumber).apply();
    }

    public static String getUserNumber(Context context) {
        return getSharedDefaultPreferences(context).getString(USER_NUMBER, "");
    }

    public static void setUserPsw(Context context, String userPsw) {
        getSharedDefaultPreferences(context).edit().putString(USER_PSW, userPsw).apply();
    }

    public static String getUserPsw(Context context) {
        return getSharedDefaultPreferences(context).getString(USER_PSW, "");
    }

    public static void setUserNick(Context context, String userNick) {
        getSharedDefaultPreferences(context).edit().putString(USER_NICK, userNick).apply();
    }

    public static String getUserNick(Context context) {
        return getSharedDefaultPreferences(context).getString(USER_NICK, "");
    }

    public static void saveUserInfo(Context context, String userNumber, String userPsw, String userNick) {
        setUserPsw(context, userPsw);
        setUserNumber(context, userNumber);
        setUserNick(context, userNick);
    }
}