package com.example.projet_final;

public class User {
    private String userID,
        birthday,
        phone,
        sex,
        user_name;
    private boolean Builder,
            painter,
            Moving,
            air_conditioner,
            electrician,
            gardening,
            housework,
            plumber;
    private Long Latitude,
            Longitude;

    public Long getLatitude() {
        return Latitude;
    }

    public void setLatitude(Long latitude) {
        Latitude = latitude;
    }

    public Long getLongitude() {
        return Longitude;
    }

    public void setLongitude(Long longitude) {
        Longitude = longitude;
    }

    public String getUserID() {
        return userID;
    }

    public void setUserID(String userID) {
        this.userID = userID;
    }

    public String getBirthday() {
        return birthday;
    }

    public void setBirthday(String birthday) {
        this.birthday = birthday;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSex() {
        return sex;
    }

    public void setSex(String sex) {
        this.sex = sex;
    }

    public String getUser_name() {
        return user_name;
    }

    public void setUser_name(String user_name) {
        this.user_name = user_name;
    }

    public boolean isBuilder() {
        return Builder;
    }

    public void setBuilder(boolean builder) {
        Builder = builder;
    }

    public boolean isPainter() {
        return painter;
    }

    public void setPainter(boolean painter) {
        this.painter = painter;
    }

    public boolean isMoving() {
        return Moving;
    }

    public void setMoving(boolean moving) {
        Moving = moving;
    }

    public boolean isAir_conditioner() {
        return air_conditioner;
    }

    public void setAir_conditioner(boolean air_conditioner) {
        this.air_conditioner = air_conditioner;
    }

    public boolean isElectrician() {
        return electrician;
    }

    public void setElectrician(boolean electrician) {
        this.electrician = electrician;
    }

    public boolean isGardening() {
        return gardening;
    }

    public void setGardening(boolean gardening) {
        this.gardening = gardening;
    }

    public boolean isHousework() {
        return housework;
    }

    public void setHousework(boolean housework) {
        this.housework = housework;
    }

    public boolean isPlumber() {
        return plumber;
    }

    public void setPlumber(boolean plumber) {
        this.plumber = plumber;
    }
}


