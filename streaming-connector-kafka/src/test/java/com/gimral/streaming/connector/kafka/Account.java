package com.gimral.streaming.connector.kafka;

public class Account {
    public String getAcid() {
        return acid;
    }

    public void setAcid(String acid) {
        this.acid = acid;
    }

    private String acid;

    public Account(String acid) {
        this.acid = acid;
    }
}
