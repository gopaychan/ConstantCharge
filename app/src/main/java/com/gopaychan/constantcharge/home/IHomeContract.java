package com.gopaychan.constantcharge.home;

import com.gopaychan.constantcharge.base.IBasePresenter;
import com.gopaychan.constantcharge.base.IBaseView;

/**
 * Created by gopaychan on 2017/3/26.
 */

public interface IHomeContract {

    interface IView extends IBaseView<IPresenter>{
    }

    interface IPresenter extends IBasePresenter{
    }
}
