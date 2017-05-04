package com.hengchongkeji.constantcharge.http;


import android.content.Context;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.hengchongkeji.constantcharge.BuildConfig;
import com.hengchongkeji.constantcharge.R;
import com.hengchongkeji.constantcharge.utils.HttpUtils;
import com.hengchongkeji.constantcharge.utils.ThreadUtils;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.FormBody;
import okhttp3.HttpUrl;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by gopayChan on 2017/5/1.
 */

public class OkHttpRequest implements IHttpRequest {
    private static final MediaType JSON
            = MediaType.parse("application/json; charset=utf-8");
    private static OkHttpClient mClient;

    private Context mContext;

    OkHttpRequest(final Context context) {
        mContext = context;
        mClient = new OkHttpClient.Builder().cookieJar(new CookieJar() {
            private final PersistentCookieStore cookieStore = new PersistentCookieStore(context.getApplicationContext());

            @Override
            public void saveFromResponse(HttpUrl url, List<Cookie> cookies) {
                if (cookies != null && cookies.size() > 0) {
                    for (Cookie item : cookies) {
                        cookieStore.add(url, item);
                    }
                }
            }

            @Override
            public List<Cookie> loadForRequest(HttpUrl url) {
                List<Cookie> cookies = cookieStore.get(url);
                return cookies;
            }
        }).build();
    }

    private void postInternal(String url, String[] keys, String[] values, final TypeToken token, final OnResponseListener listener) {
        if (!HttpUtils.hasConnectedNet(mContext)) {
            listener.onFail(mContext.getString(R.string.no_net_text));
            return;
        }
//        RequestBody body = RequestBody.create(JSON, json);
        FormBody.Builder bodyBuilder = new FormBody.Builder();
        for (int i = 0; i < keys.length; i++) {
            bodyBuilder.add(keys[i], values[i]);
        }
        RequestBody body = bodyBuilder.build();
        Request request = new Request.Builder()
                .url(url)
                .post(body)
                .build();
        mClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                ThreadUtils.runOnMainThread(new Runnable() {
                    @Override
                    public void run() {
                        listener.onFail(mContext.getString(R.string.net_io_exception));
                    }
                });
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                if (!response.isSuccessful()) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            listener.onFail(mContext.getString(R.string.net_server_error));
                        }
                    });
                    return;
                }
                String result = response.body().string();
                if ("".equals(result)) {
                    ThreadUtils.runOnMainThread(new Runnable() {
                        @Override
                        public void run() {
                            if (BuildConfig.DEBUG)
                                listener.onFail("返回结果为空");
                        }
                    });
                    return;
                }
                if (token == null) {
                    returnString(result, listener);
                } else {
                    returnObj(result, token, listener);
                }
            }
        });
    }

    private void returnString(final String result, final OnResponseListener listener) {
        JSONObject jsonObj = null;
        try {
            jsonObj = new JSONObject(result);
        } catch (JSONException e) {
            e.printStackTrace();
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (BuildConfig.DEBUG) {
                        listener.onFail("请求解析错误;" + result);
                    } else {
                        listener.onFail("请求解析错误");
                    }
                }
            });
            return;
        }
        try {
            final String resultCode = jsonObj.getString("code");
            final String resultInfo = jsonObj.getString("info");
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if ("0".equals(resultCode)) {
                        listener.onSuccess(resultInfo);
                    } else {
                        listener.onFail(resultInfo);
                    }
                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
            ThreadUtils.runOnMainThread(new Runnable() {
                @Override
                public void run() {
                    if (BuildConfig.DEBUG) {
                        listener.onFail("请求解析错误;" + result);
                    } else {
                        listener.onFail("请求解析错误");

                    }
                }
            });
        }
    }

    private void returnObj(String result, TypeToken token, final OnResponseListener listener) {
        Gson gson = new Gson();
        final BaseResponse response = gson.fromJson(result, token.getType());
        ThreadUtils.runOnMainThread(new Runnable() {
            @Override
            public void run() {
                if ("0".equals(response.code)) {
                    listener.onSuccess(response);
                } else {
                    listener.onFail(response.info);
                }
            }
        });
    }

    @Override
    public void post(String url, String[] keys, String[] values, OnResponseListener listener) {
        postInternal(url, keys, values, null, listener);
    }

    @Override
    public void post(String url, String[] keys, String[] values, TypeToken
            token, OnResponseListener listener) {
        postInternal(url, keys, values, token, listener);
    }

    @Override
    public void get() {

    }
}
