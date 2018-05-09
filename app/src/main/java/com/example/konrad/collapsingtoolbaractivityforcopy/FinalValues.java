package com.example.konrad.collapsingtoolbaractivityforcopy;

import android.icu.text.SimpleDateFormat;

import java.sql.Time;
import java.util.Date;

/**
 * Created by Konrad on 20.04.2018.
 */

public class FinalValues {
    String value;
    String dateFormat;
    String key;

    public FinalValues(String value, String dateFormat, String key) {
        this.value = value;
        this.dateFormat = dateFormat;
        this.key = key;
    }
    public FinalValues(String value, String dateFormat) {
        this.value = value;
        this.dateFormat = dateFormat;

    }
    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getDateFormat() {
        return dateFormat;
    }

    public void setDateFormat(String dateFormat) {
        this.dateFormat = dateFormat;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;

    }
}
