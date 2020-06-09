package com.example.projet_final;

public class resulttReq {
    private boolean accpet;
    private boolean watched;
    private String accID;

    public boolean isAccpet() {
        return accpet;
    }

    public void setAccpet(boolean accpet) {
        this.accpet = accpet;
    }

    public boolean iswatched() {
        return watched;
    }

    public void setwatched(boolean booleanwatched) {
        this.watched = booleanwatched;
    }

    public String getAccID() {
        return accID;
    }

    public void setAccID(String accID) {
        this.accID = accID;
    }
}
