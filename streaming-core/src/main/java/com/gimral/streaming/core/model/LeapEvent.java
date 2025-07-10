package com.gimral.streaming.core.model;

public class LeapEvent<T> extends LeapEventHeader {
    private T data;

    public LeapEvent(){}

    public T getData() {
        return data;
    }

    public void setData(T data) {
        this.data = data;
    }
}
