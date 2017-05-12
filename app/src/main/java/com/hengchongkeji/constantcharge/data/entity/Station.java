package com.hengchongkeji.constantcharge.data.entity;

import android.support.annotation.NonNull;

import com.baidu.mapapi.model.LatLng;

import java.io.Serializable;
import java.util.List;

/**
 * Created by gopayChan on 2017/5/10.
 */

public class Station implements Serializable, Comparable<Station> {
    private static final long serialVersionUID = 8688209845892189L;

    //服务器返回的
    private String stationId;
    private String stationName;
    private String areaCode;
    private String address;
    private String stationType;
    private String stationStatus;
    private String stationLng;
    private String stationLat;
    private String construction;
    private String matchCars;
    private String busineHours;
    private String remark;

    //客户端自己计算的
    private String totalPile;
    private LatLng latLng;
    private String freePile;
    private String distance;
    private String predictReachTime;
    private String predictFreePileTime = " 暂无 ";

    private List<Equipment> equipmentInfos;

    public String getStationId() {
        return stationId;
    }

    public void setStationId(String stationId) {
        this.stationId = stationId;
    }

    public String getStationName() {
        return stationName;
    }

    public void setStationName(String stationName) {
        this.stationName = stationName;
    }

    public String getAreaCode() {
        return areaCode;
    }

    public void setAreaCode(String areaCode) {
        this.areaCode = areaCode;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getStationType() {
        return stationType;
    }

    public void setStationType(String stationType) {
        this.stationType = stationType;
    }

    public String getStationStatus() {
        return stationStatus;
    }

    public void setStationStatus(String stationStatus) {
        this.stationStatus = stationStatus;
    }

    public String getStationLng() {
        return stationLng;
    }

    public void setStationLng(String stationLng) {
        this.stationLng = stationLng;
    }

    public String getStationLat() {
        return stationLat;
    }

    public void setStationLat(String stationLat) {
        this.stationLat = stationLat;
    }

    public String getConstruction() {
        return construction;
    }

    public void setConstruction(String construction) {
        this.construction = construction;
    }

    public String getMatchCars() {
        return matchCars;
    }

    public void setMatchCars(String matchCars) {
        this.matchCars = matchCars;
    }

    public String getBusineHours() {
        return busineHours;
    }

    public void setBusineHours(String busineHours) {
        this.busineHours = busineHours;
    }

    public String getRemark() {
        return remark;
    }

    public void setRemark(String remark) {
        this.remark = remark;
    }

    public String getTotalPile() {
        return totalPile;
    }

    public void setTotalPile(String totalPile) {
        this.totalPile = totalPile;
    }

    public LatLng getLatLng() {
        return latLng;
    }

    public void setLatLng(LatLng latLng) {
        this.latLng = latLng;
    }

    public String getFreePile() {
        return freePile;
    }

    public void setFreePile(String freePile) {
        this.freePile = freePile;
    }

    public String getDistance() {
        return distance;
    }

    public void setDistance(String distance) {
        this.distance = distance;
    }

    public String getPredictReachTime() {
        return predictReachTime;
    }

    public void setPredictReachTime(String predictReachTime) {
        this.predictReachTime = predictReachTime;
    }

    public String getPredictFreePileTime() {
        return predictFreePileTime;
    }

    public void setPredictFreePileTime(String predictFreePileTime) {
        this.predictFreePileTime = predictFreePileTime;
    }

    public List<Equipment> getList() {
        return equipmentInfos;
    }

    public void setList(List<Equipment> list) {
        this.equipmentInfos = list;
    }

    @Override
    public int compareTo(@NonNull Station o) {
        return Double.valueOf(distance).compareTo(Double.valueOf(o.distance));
    }

}
