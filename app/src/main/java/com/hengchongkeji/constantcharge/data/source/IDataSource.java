package com.hengchongkeji.constantcharge.data.source;

import android.content.Context;

import com.baidu.mapapi.model.LatLng;
import com.hengchongkeji.constantcharge.data.entity.Equipment;
import com.hengchongkeji.constantcharge.data.entity.Station;
import com.hengchongkeji.constantcharge.http.IHttpRequest;

import java.util.List;

/**
 * Created by gopayChan on 2017/4/20.
 */

public interface IDataSource {
    void getChargeBalance(IHttpRequest.OnResponseListener<String> listener);

    void getLatLngNearby(Context context, LatLng curLatLng, IHttpRequest.OnResponseListener<List<Station>> listener);

    void getFoundAdImgUrl(IHttpRequest.OnResponseListener<int[]> listener);

    void getIntroductionImgUrl(IHttpRequest.OnResponseListener<int[]> listener);

    void getStationFeeByIDd(Context context, String stationId, IHttpRequest.OnResponseListener<Station> listener);

    void getEquipmentData(Context context, String pileId, IHttpRequest.OnResponseListener<Equipment> listener);

    void startEquipment(Context context, String pileId, IHttpRequest.OnResponseListener listener);

    void stopEquipment(Context context, IHttpRequest.OnResponseListener listener);
}
