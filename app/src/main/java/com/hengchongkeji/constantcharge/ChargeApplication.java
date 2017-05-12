package com.hengchongkeji.constantcharge;

import android.app.Application;

import com.baidu.location.BDLocation;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.hengchongkeji.constantcharge.common.MyAppExceptions;
import com.hengchongkeji.constantcharge.data.entity.DaoMaster;
import com.hengchongkeji.constantcharge.data.entity.DaoSession;
import com.hengchongkeji.constantcharge.data.entity.User;
import com.hengchongkeji.constantcharge.utils.PreferenceUtils;

import org.greenrobot.greendao.database.Database;

import java.util.List;

/**
 * Created by gopaychan on 2017/3/26.
 */

public class ChargeApplication extends Application {
    private static ApplicationComponent mApplicationComponent;
    private BDLocation mLastLocation;
    private static ChargeApplication mInstance;
    private boolean isLogin = false;
    //    public static final boolean ENCRYPTED = BuildConfig.DEBUG;
    private DaoSession mDaoSession;

    public User mUser;

    public static ChargeApplication getInstance() {
        return mInstance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        SDKInitializer.initialize(getApplicationContext());
        SDKInitializer.setCoordType(CoordType.GCJ02);//默认为BD09LL坐标
        initializeInjector();
        initGreendao();
        Thread.setDefaultUncaughtExceptionHandler(MyAppExceptions
                .getAppExceptionHandler());
        mInstance = this;
        isLogin = PreferenceUtils.getLoginState(this);
//        if (BuildConfig.DEBUG) {
//            BlockCanary.install(this, new AppBlockCanaryContext()).start();
//            if (LeakCanary.isInAnalyzerProcess(this)) {
//                // This process is dedicated to LeakCanary for heap analysis.
//                // You should not init your app in this process.
//                return;
//            }
//            LeakCanary.install(this);
//        }
    }

    public User getUser() {
        if (mUser == null) {
            List<User> userList = getDaoSession().getUserDao().queryBuilder().list();
            if (userList.size() > 0) {
                mUser = getDaoSession().getUserDao().queryBuilder().list().get(userList.size() - 1);
            }else{
                logout();
            }
        }
        return mUser;
    }

    public void login() {
        isLogin = true;
        PreferenceUtils.setLoginState(this, isLogin);
    }

    public void logout() {
        isLogin = false;
        PreferenceUtils.setLoginState(this, isLogin);
    }

    public boolean getIsLogin() {
        return isLogin;
    }

    private void initializeInjector() {
        mApplicationComponent = DaggerApplicationComponent.builder().applicationModule(new ApplicationModule(this)).build();
    }

    private void initGreendao() {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(this, "constantcharge-db");
        Database db = helper.getWritableDb();
        mDaoSession = new DaoMaster(db).newSession();
    }

    public DaoSession getDaoSession() {
        return mDaoSession;
    }

    public void saveUserEntity(User user) {
        mDaoSession.getUserDao().insertOrReplace(user);
        mUser = user;
    }

    public static ApplicationComponent getApplicationComponent() {
        return mApplicationComponent;
    }

    public void setCurLocation(BDLocation location) {
        mLastLocation = location;
    }

    public boolean isLocationNull() {
        return mLastLocation == null;
    }

    public BDLocation getCurLocation() {
        return mLastLocation;
    }
}
