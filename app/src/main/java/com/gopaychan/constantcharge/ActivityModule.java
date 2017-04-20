package com.gopaychan.constantcharge;

import com.gopaychan.constantcharge.base.BaseActivity;
import com.gopaychan.constantcharge.base.PerActivity;

import dagger.Module;
import dagger.Provides;

/**
 * Created by gopayChan on 2017/4/20.
 */
@Module
public class ActivityModule {
    private final BaseActivity activity;

    public ActivityModule(BaseActivity activity) {
        this.activity = activity;
    }

    /**
     * Expose the activity to dependents in the graph.
     */
    @Provides
    @PerActivity
    BaseActivity activity() {
        return this.activity;
    }
}
