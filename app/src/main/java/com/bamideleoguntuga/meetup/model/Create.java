package com.bamideleoguntuga.meetup.model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class Create {

    @SerializedName("name")
    @Expose
    private String name;
    @SerializedName("phonenumber")
    @Expose
    private String phonenumber;
    @SerializedName("pin")
    @Expose
    private String pin;

    /**
     * No args constructor for use in serialization
     *
     */
    public Create() {
    }

    /**
     *
     * @param pin
     * @param name
     * @param phonenumber
     */
    public Create(String name, String phonenumber, String pin) {
        super();
        this.name = name;
        this.phonenumber = phonenumber;
        this.pin = pin;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhonenumber() {
        return phonenumber;
    }

    public void setPhonenumber(String phonenumber) {
        this.phonenumber = phonenumber;
    }

    public String getPin() {
        return pin;
    }

    public void setPin(String pin) {
        this.pin = pin;
    }

}
