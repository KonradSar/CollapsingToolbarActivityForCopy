package com.example.konrad.collapsingtoolbaractivityforcopy;

import android.icu.text.SimpleDateFormat;
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
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.finalValuesList;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.pollutionsIds;


/**
 * Created by Konrad on 03.04.2018.
 */

public class FinalValuesAPI  extends AsyncTask<Object, Object, List<FinalValues>> {
    @Override
    protected List<FinalValues> doInBackground(Object... params) {
        finalValuesList.clear();
        URL url = null;
        try {
            //iterujac po liscie List<AirResultsStationsIds> pollutionsIds, na ktorej znajduja sie rekordy z 3 polami danych
            //czyli nazwa zanieczyszczenia, skrot zanieczyszczenia oraz id stanowiska pomiarowego, ktore mierzy wartosc danego
            //zanieczyszczenia. Na podstawie tego id bedziemy szukac konkretnych wartosci w bazie dla danego typu zanieczyszczenia
           for(AirResultsStationsIds element: pollutionsIds){
               String variable = element.getPollutionsIdKey();
               url = new URL("http://api.gios.gov.pl/pjp-api/rest/data/getData/"+variable);
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
                   // Inaczej niż zwykle w pierwszym etapie definiujemy JSONObject, w ktorym to znajduje się JSONArray
                   JSONObject jsonObject = new JSONObject(stringBuilder.toString());
                   String keyNamePollution = String.valueOf(jsonObject.get("key"));
                   JSONArray innerArray = (JSONArray) jsonObject.get("values");
                   List<FinalValues> preParse = new ArrayList<>();
                   for (int i = 0; i < 14; i++) {
                       JSONObject obj = (JSONObject) innerArray.get(i);
                       JSONObject singleObjPollutionValue = (JSONObject) obj;
                       String valueOfPollution = String.valueOf(singleObjPollutionValue.get("value"));
                       String dateOfMeasurement = String.valueOf(singleObjPollutionValue.get("date"));
                       //Zapisujemy kolejne rekordy o trzech polach dla wszystkich wartosci zanieczyszczen i typach zanieczyszczen dla
                       //danej stacji pomiarowej w danej miejscowosci w danej lokalizacji wybranej z listy. Np. w Zabrzu na ul. Sklodowskiej
                       //mamy stacje pomiarowa wiec wybierajac ja z listy polecanych lokalizacji na podstawie GPS pobieramy tutaj wszystkie
                       //wartosci wszystkich typow zanieczyszczen do jednej listy (wartosc zanieczyszczenia, data i godzina, skrot zanieczyszczenia).
                       //Dane sa pobierane z ostatnich dwoch dni przez co mozemy skonstruowac odpowiedni wykres pokazujacy tendencje danego zanieczyszczenia
                       preParse.add(i, new FinalValues(valueOfPollution, dateOfMeasurement, keyNamePollution));
                       finalValuesList.add(i, preParse.get(i));
//                return preParse;
                   }
                   preParse = finalValuesList;
               } else {
                   System.out.println("tabla nie obsluzona");
               }
           }

        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return finalValuesList;
    }
}
