package com.hengchongkeji.constantcharge.data.entity;

/**
 * Created by gopayChan on 2017/5/10.
 */

public class Equipment {
    public String equipmentId;
    public String equipmentLng;
    public String equipmentLat;
    public String parkNo;
    public String status;
    public String parkStatus;

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

    public String lockStatus;

}
