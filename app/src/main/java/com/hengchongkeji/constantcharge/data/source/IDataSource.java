package com.hengchongkeji.constantcharge.data.source;

import com.baidu.mapapi.model.LatLng;
import com.hengchongkeji.constantcharge.data.domain.ChargeDetailData;
import com.hengchongkeji.constantcharge.data.domain.MapMarkerInfo;

import java.util.List;

/**
 * Created by gopayChan on 2017/4/20.
 */

public interface IDataSource {
    ChargeDetailData getChargeDetailData();
    String getChargeBalance();
    List<MapMarkerInfo> getLatLngNearby(LatLng curLatLng);
    int[] getFoundAdImgUrl();
    int[] getIntroductionImgUrl();
}
