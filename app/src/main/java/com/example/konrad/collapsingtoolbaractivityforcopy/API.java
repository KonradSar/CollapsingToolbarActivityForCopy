package com.example.konrad.collapsingtoolbaractivityforcopy;

import android.os.AsyncTask;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.ArrayList;
import java.util.List;

import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.cities;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.finalCityStations;

/**
 * Created by Konrad on 21.03.2018.
 */

public class API extends AsyncTask<Object, Object, List<AirResults>> {

    @Override
    protected List<AirResults> doInBackground(Object... params) {
        URL url = null;
        cities.clear();
        finalCityStations.clear();
        try {
            url = new URL("http://api.gios.gov.pl/pjp-api/rest/station/findAll");
            HttpURLConnection http = (HttpURLConnection) url.openConnection();
            http.setRequestMethod("GET");
            http.connect();
            int responseCode = http.getResponseCode();
            if (responseCode == 200) { //jeżeli ok TO:
                BufferedReader bufferedReader = new BufferedReader(
                        new InputStreamReader(http.getInputStream()));
                String line = null;
                StringBuilder stringBuilder = new StringBuilder();
            while ((line = bufferedReader.readLine()) != null) {
                stringBuilder.append(line);
            }
            JSONArray jsonArray = new JSONArray(stringBuilder.toString());
                List<AirResults> preParse = new ArrayList<>();
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject obj = (JSONObject) jsonArray.get(i);
                JSONObject singleObjId = (JSONObject) obj;
                // definiujemy osobno zmienne lokalnie w celu ominięcia błędu castowania na JSON Object
                int id = Integer.parseInt(String.valueOf(singleObjId.get("id")));
                JSONObject singleObjAddress = (JSONObject) obj;
                String address;
                address = singleObjAddress.getString("addressStreet");
                JSONObject cityName = (JSONObject) obj.get("city");
                JSONObject o = (JSONObject) cityName;
                String cityName1;
                cityName1 = o.getString("name");
                preParse.add(i, new AirResults(String.valueOf(id), cityName1, address));
                cities.add(i, preParse.get(i));
//                return preParse;
            }
            preParse = cities;
            } else {
                System.out.println("tabla nie obsluzona");
            }
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return cities;}}

