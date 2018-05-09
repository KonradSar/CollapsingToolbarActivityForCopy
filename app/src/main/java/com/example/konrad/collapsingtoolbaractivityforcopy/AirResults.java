package com.example.konrad.collapsingtoolbaractivityforcopy;

/**
 * Created by Konrad on 20.03.2018.
 */

public class AirResults {
    public String cityNumber;
    public String cityName;
    public String address;

    public AirResults(String cityNumber, String cityName, String address) {
        this.cityNumber = cityNumber;
        this.cityName = cityName;
        this.address = address;
    }
    public AirResults(String cityNumber, String address) {
        this.cityNumber = cityNumber;
        this.address = address;
    }
    public String getCityNumber() {
        return cityNumber;
    }

    public void setCityNumber(String cityNumber) {
        this.cityNumber = cityNumber;
    }

    public String getCityName() {
        return cityName;
    }

    public void setCityName(String cityName) {
        this.cityName = cityName;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }
}
