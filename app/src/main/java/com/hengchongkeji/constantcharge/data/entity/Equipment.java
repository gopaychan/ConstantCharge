package com.hengchongkeji.constantcharge.data.entity;

/**
 * Created by gopayChan on 2017/5/10.
 */

public class Equipment {
    private String equipmentId;
    private String equipmentLng;
    private String equipmentLat;
    private String parkNo;
    private String status;
    private String parkStatus;
    private String lockStatus;
    private String equipmentType;//1：直流设备 2：交流设备 3：交直流一体设备

    private String startTime;
    private String chargeCurrent;
    private String chargeVoltage;
    private String electricityS;

    private String percent;


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getChargeCurrent() {
        return chargeCurrent;
    }

    public void setChargeCurrent(String chargeCurrent) {
        this.chargeCurrent = chargeCurrent;
    }

    public String getChargeVoltage() {
        return chargeVoltage;
    }

    public void setChargeVoltage(String chargeVoltage) {
        this.chargeVoltage = chargeVoltage;
    }

    public String getElectriciryS() {
        return electricityS;
    }

    public void setElectriciryS(String electriciryS) {
        this.electricityS = electriciryS;
    }

    public String getEquipmentId() {
        return equipmentId;
    }

    public void setEquipmentId(String equipmentId) {
        this.equipmentId = equipmentId;
    }

    public String getEquipmentLng() {
        return equipmentLng;
    }

    public void setEquipmentLng(String equipmentLng) {
        this.equipmentLng = equipmentLng;
    }

    public String getEquipmentLat() {
        return equipmentLat;
    }

    public void setEquipmentLat(String equipmentLat) {
        this.equipmentLat = equipmentLat;
    }

    public String getParkNo() {
        return parkNo;
    }

    public void setParkNo(String parkNo) {
        this.parkNo = parkNo;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getParkStatus() {
        return parkStatus;
    }

    public void setParkStatus(String parkStatus) {
        this.parkStatus = parkStatus;
    }

    public String getLockStatus() {
        return lockStatus;
    }

    public void setLockStatus(String lockStatus) {
        this.lockStatus = lockStatus;
    }

    public String getEquipmentType() {
        return equipmentType;
    }

    public void setEquipmentType(String equipmentType) {
        this.equipmentType = equipmentType;
    }

    public String getPercent() {
        return percent;
    }

    public void setPercent(String percent) {
        this.percent = percent;
    }
}
