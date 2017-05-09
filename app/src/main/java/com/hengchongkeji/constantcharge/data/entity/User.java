package com.hengchongkeji.constantcharge.data.entity;

/**
 * Created by gopayChan on 2017/5/6.
 */


import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Property;
import org.greenrobot.greendao.annotation.Transient;

@Entity
public class User {

    @Id
    @Property(nameInDb = "user_phone")
    private String phone;

    @Property(nameInDb = "user_nick")
    private String username;

    @Property(nameInDb = "user_avatar")
    private String image;

    @Property(nameInDb = "user_psw")
    private String password;

    private String balance;

    @Property(nameInDb = "user_wechat")
    private String wechat;

    @Property(nameInDb = "user_email")
    private String email;

    @Property(nameInDb = "user_address")
    private String address;

    private String lastLoginTime;

    @Transient
    private int tempUsageCount;

    @Generated(hash = 1533602556)
    public User(String phone, String username, String image, String password,
            String balance, String wechat, String email, String address,
            String lastLoginTime) {
        this.phone = phone;
        this.username = username;
        this.image = image;
        this.password = password;
        this.balance = balance;
        this.wechat = wechat;
        this.email = email;
        this.address = address;
        this.lastLoginTime = lastLoginTime;
    }

    @Generated(hash = 586692638)
    public User() {
    }

    public String getPhone() {
        return this.phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getUsername() {
        return this.username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getImage() {
        return this.image;
    }

    public void setImage(String image) {
        this.image = image;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getBalance() {
        return this.balance;
    }

    public void setBalance(String balance) {
        this.balance = balance;
    }

    public String getWechat() {
        return this.wechat;
    }

    public void setWechat(String wechat) {
        this.wechat = wechat;
    }

    public String getEmail() {
        return this.email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getAddress() {
        return this.address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getLastLoginTime() {
        return this.lastLoginTime;
    }

    public void setLastLoginTime(String lastLoginTime) {
        this.lastLoginTime = lastLoginTime;
    }
}
