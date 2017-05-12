package com.hengchongkeji.constantcharge.data.source;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.hengchongkeji.constantcharge.data.entity.ChargeDetailData;
import com.hengchongkeji.constantcharge.data.entity.Station;
import com.hengchongkeji.constantcharge.http.IHttpRequest;

import java.util.List;

/**
 * Created by gopayChan on 2017/4/20.
 */

public interface IDataSource {
    void getChargeDetailData(Context context, IHttpRequest.OnResponseListener<ChargeDetailData> listener);
    void getChargeBalance(IHttpRequest.OnResponseListener<String> listener);
    void getLatLngNearby(Context context,LatLng curLatLng,IHttpRequest.OnResponseListener<List<Station>> listener);
    void getFoundAdImgUrl(IHttpRequest.OnResponseListener<int[]> listener);
    void getIntroductionImgUrl(IHttpRequest.OnResponseListener<int[]> listener);
}
