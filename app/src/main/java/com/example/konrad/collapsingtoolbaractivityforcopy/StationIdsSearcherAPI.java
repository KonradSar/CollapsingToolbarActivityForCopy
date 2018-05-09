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
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.cityNameId;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.finalCityStations;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.pollutionsIds;

/**
 * Created by Konrad on 31.03.2018.
 */

public class StationIdsSearcherAPI extends AsyncTask<Object, Object, List<AirResultsStationsIds>> {

    @Override
    protected List<AirResultsStationsIds> doInBackground(Object... params) {
        URL url = null;
        pollutionsIds.clear();
        String Id = cityNameId;

        try {
            url = new URL("http://api.gios.gov.pl/pjp-api/rest/station/sensors/"+Id);
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
                List<AirResultsStationsIds> preParse = new ArrayList<>();
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject obj = (JSONObject) jsonArray.get(i);
                    JSONObject singleObjId = (JSONObject) obj;
                    // definiujemy osobno zmienne lokalnie w celu ominięcia błędu castowania na JSON Object
                    String id = String.valueOf(singleObjId.get("id"));
//                    int id = Integer.parseInt(String.valueOf(singleObjId.get("id")));
                    JSONObject cityName = (JSONObject) obj.get("param");
                    JSONObject o = (JSONObject) cityName;
                    String paramName;
                    // pobieram metoda getString, poniewaz przypisuje do konkretnej zmiennej typu String
                    paramName = o.getString("paramName");
                    String paramSignature;
                    paramSignature = o.getString("paramFormula");
                    preParse.add(i, new AirResultsStationsIds(paramName, paramSignature, id));
                    pollutionsIds.add(i, preParse.get(i));
//                return preParse;
                }
//                preParse = pollutionsIds;
            } else {
                System.out.println("tabla nie obsluzona");}
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return pollutionsIds;}
}
