package com.example.konrad.collapsingtoolbaractivityforcopy;

/**
 * Created by Konrad on 03.04.2018.
 */

public class AirResultsStationsIds {
    public String pollutionName;
    public String pollutionsSignature;
    public String pollutionsIdKey;

    public AirResultsStationsIds(String pollutionName, String pollutionsSignature, String pollutionsIdKey) {
        this.pollutionName = pollutionName;
        this.pollutionsSignature = pollutionsSignature;
        this.pollutionsIdKey = pollutionsIdKey;
    }

    public String getPollutionName() {
        return pollutionName;
    }

    public void setPollutionName(String pollutionName) {
        this.pollutionName = pollutionName;
    }

    public String getPollutionsSignature() {
        return pollutionsSignature;
    }

    public void setPollutionsSignature(String pollutionsSignature) {
        this.pollutionsSignature = pollutionsSignature;
    }

    public String getPollutionsIdKey() {
        return pollutionsIdKey;
    }

    public void setPollutionsIdKey(String pollutionsIdKey) {
        this.pollutionsIdKey = pollutionsIdKey;

    }
}
