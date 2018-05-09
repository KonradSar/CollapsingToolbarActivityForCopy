package com.example.konrad.collapsingtoolbaractivityforcopy;

import android.view.View;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Konrad on 20.03.2018.
 */

public class NaszeMEtody {
    public static List<AirResults> cities = new ArrayList<>();
    // ponizej lista gdy dane miasto ma na liscie pobranej JSONem kilka lokalizacji i jest ona wyswietlona w AlertDialog w celu wyboru najblizszej lokalizacji
    public static List<AirResults> finalCityStations = new ArrayList<>();
    public static double longitude;
    public static double latitude;
    // ponizej iD wybranego miasta gdy lista JSON nie posiada tego samego miasta na kilku pozycjach i po tym iD szukamy w bazie danych API
    public static String cityNameId;
    public static String cityName;
    public static String address;
    public static List<String> stringsForAlertDialogSearcher = new ArrayList<>();
    public static List<String> stringsForAlertDialog = new ArrayList<>();
    public static List<AirResultsStationsIds> pollutionsIds = new ArrayList<>();
    public  static List<FinalValues> finalValuesList = new ArrayList<>();
    public static float[] listOfValuesForCO = new float[14];
    public static String[] listOfDataAndTimeValForCO = new String[14];
    public static float[] listOfValuesForBenzene = new float[14];
    public static String[] listOfDataAndTimeValForBenzene = new String[14];
    public static float[] listOfValuesForPM10 = new float[14];
    public static String[] listOfDataAndTimeValForPM10 = new String[14];
    public static float[] listOfValuesForPM25 = new float[14];
    public static String[] listOfDataAndTimeValForPM25 = new String[14];
    public static float[] listOfValuesForNO = new float[14];
    public static String[] listOfDataAndTimeValForNO = new String[14];
    public static float[] listOfValuesForSO = new float[14];
    public static String[] listOfDataAndTimeValForSO = new String[14];
    public static boolean conditionForDataCO = true;
    public static boolean conditionForDataSO = true;
    public static boolean conditionForDataNO = true;
    public static boolean conditionForDataBenzene = true;
    public static boolean conditionForDataPM10 = true;
    public static boolean conditionForDataPM25 = true;
    public static int[] listOfColors = new int[14];
    public static List<FinalValues> pm10NM = new ArrayList<>();
    public static List<FinalValues> pm25NM = new ArrayList<>();
    public static List<FinalValues> benzeneNM = new ArrayList<>();
    public static List<FinalValues> carbonOxideNM = new ArrayList<>();
    public static List<FinalValues> sulfurDioxideNM = new ArrayList<>();
    public static List<FinalValues> nitroDioxideNM = new ArrayList<>();
    public static View v;
    public static String manuallySelectedCityIdOne;

}
