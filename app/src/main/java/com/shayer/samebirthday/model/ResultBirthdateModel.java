package com.shayer.samebirthday.model;

import java.io.Serializable;

/**
 * Created by Shreeya Patel on 3/8/2016.
 */
public class ResultBirthdateModel implements Serializable {

    private static final long serialVersionUID = 1;
    private String UserName;
    private String UserGender;
    private String UserBirthdate;
    private String UserId;

    public String getUserId() {
        return this.UserId;
    }
    public void setUserId(String UserId) {
        this.UserId = UserId;
    }

    public String getUserName() {
        return this.UserName;
    }
    public void setUserName(String UserName) {
        this.UserName = UserName;
    }

    public String getUserGender() {
        return this.UserGender;
    }
    public void setUserGender(String UserGender) {
        this.UserGender = UserGender;
    }

    public String getUserBirthdate() {
        return this.UserBirthdate;
    }
    public void setUserBirthdate(String UserBirthdate) {
        this.UserBirthdate = UserBirthdate;
    }
}
