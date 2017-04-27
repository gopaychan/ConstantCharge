package com.gopaychan.constantcharge.charge.data;

import android.support.annotation.NonNull;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;

/**
 * Created by gopayChan on 2017/4/22.
 */

public class MapMarkerInfo implements Serializable, Comparable<MapMarkerInfo> {
    private static final long serialVersionUID = 8688209845892189L;
    public LatLng latLng;
    public String totalPile;
    public String freePile;
    public String predictReachTime;
    public String predictFreePileTime;
    public String address;
    public String distance;

    @Override
    public int compareTo(@NonNull MapMarkerInfo o) {
        return Double.valueOf(distance).compareTo(Double.valueOf(o.distance));
    }
}
