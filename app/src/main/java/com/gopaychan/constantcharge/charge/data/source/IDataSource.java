package com.gopaychan.constantcharge.charge.data.source;

import com.baidu.mapapi.model.LatLng;
import com.gopaychan.constantcharge.charge.data.ChargeDetailData;
import com.gopaychan.constantcharge.charge.data.MapMarkerInfo;

import java.util.List;

/**
 * Created by gopayChan on 2017/4/20.
 */

public interface IDataSource {
    ChargeDetailData getChargeDetailData();
    String getChargeBalance();
    List<MapMarkerInfo> getLatLngNearby(LatLng curLatLng);



}
