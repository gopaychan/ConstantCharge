package com.gopaychan.constantcharge.charge.data.source;

import com.gopaychan.constantcharge.charge.data.MapMarkerInfo;

import java.util.Comparator;

/**
 * Created by gopayChan on 2017/4/26.
 */

public class MapMarkerInfoComparator implements Comparator<MapMarkerInfo> {
    @Override
    public int compare(MapMarkerInfo o1, MapMarkerInfo o2) {
        return o1.compareTo(o2);
    }
}
