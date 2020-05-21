package com.example.projet_final;

public class User {
    private String stat,
            birthday,
            phone,
            sex,
            user_name,
            email="no email"
            ;
    private boolean Builder;
    private boolean painter;
    private boolean Moving;
    private boolean air_conditioner;
    private boolean electrician;
    private boolean gardening;
    private boolean housework;
    private boolean plumber;
    private double Latitude,Longitude;

    public String getStat() {
        return stat;
    }

    public void setStat(String stat) {
        this.stat = stat;
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

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
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

    public double getLatitude() {
        return Latitude;
    }

    public void setLatitude(double latitude) {
        Latitude = latitude;
    }

    public double getLongitude() {
        return Longitude;
    }

    public void setLongitude(double longitude) {
        Longitude = longitude;
    }

    //public  jabs()



}


