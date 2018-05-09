package com.example.konrad.collapsingtoolbaractivityforcopy;

import android.Manifest;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.AsyncTask;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;


import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.concurrent.ExecutionException;

import butterknife.BindView;
import butterknife.ButterKnife;


import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.benzeneNM;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.carbonOxideNM;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.conditionForDataBenzene;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.conditionForDataCO;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.conditionForDataNO;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.conditionForDataPM10;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.conditionForDataPM25;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.conditionForDataSO;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.finalValuesList;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.listOfValuesForCO;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.nitroDioxideNM;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.pm10NM;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.pm25NM;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.pollutionsIds;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.stringsForAlertDialogSearcher;
import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.sulfurDioxideNM;

public class MainActivity extends AppCompatActivity implements LocationListener {
    @BindView(R.id.cityNameTextView) TextView cityNameTextView;
    Button selectCityFromGPSButton;
    Button selectPositionManually;
    TextView gpsLatitude;
    TextView gpsLongitude;
    static final int REQUEST_LOCATION = 1;
    LocationManager locationManager;
    LocationListener listener;
    private Context context1;
    // Dla AlertView
    private View alertView;
    private View alertViewSearcher;
    private View alertViewGeneralInfoForResults;
    ImageView pmtwo;
    ImageView pmten;
    ImageView nitro;
    ImageView sulfur;
    ImageView carbon;
    ImageView benzeneee;
    ImageView carbonnormalle;
    ImageView carbonexceeded;
    ImageView carbonodata;
    ImageView nitronormal;
    ImageView nitroexceed;
    ImageView nitronodata;
    ImageView sulfurnormal;
    ImageView sulfurexceed;
    ImageView sulfurnodata;
    ImageView benzenenormal;
    ImageView benzeneexceed;
    ImageView benzenenodata;
    ImageView pmtwonormal;
    ImageView pmtwoexceed;
    ImageView pmtwonodata;
    ImageView pmtennormal;
    ImageView pmtenexceed;
    ImageView pmtennodata;
    MediaPlayer myFirstSound;
    MediaPlayer mySecondSound;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        myFirstSound = MediaPlayer.create(this, R.raw.klikanie);
        mySecondSound = MediaPlayer.create(this, R.raw.error);
        NaszeMEtody.stringsForAlertDialog.clear();
        // Konfigurujemy inflater dla AlertDialog.setView......
        final LayoutInflater inflater = LayoutInflater.from(getApplicationContext());
        alertView = inflater.inflate(R.layout.maindialog, null);
        alertViewSearcher = inflater.inflate(R.layout.manualsearcher, null);
        alertViewGeneralInfoForResults = inflater.inflate(R.layout.generaliformationaboutresults, null);
        cityNameTextView = (TextView) findViewById(R.id.cityNameTextView);
        selectCityFromGPSButton = (Button) findViewById(R.id.cityNameButton);
        selectPositionManually = (Button) findViewById(R.id.typeCityName);
        gpsLatitude = (TextView) findViewById(R.id.latitude);
        gpsLongitude = (TextView) findViewById(R.id.longitude);
        locationManager = (LocationManager) getSystemService(LOCATION_SERVICE);
        pmtwo = (ImageView) findViewById(R.id.pmtwo);
        pmten = (ImageView) findViewById(R.id.pmten);
        nitro = (ImageView) findViewById(R.id.nitro);
        sulfur = (ImageView) findViewById(R.id.sulfur);
        carbon = (ImageView) findViewById(R.id.carbon);
        benzeneee = (ImageView) findViewById(R.id.benzene);
        // Ze wzgledu na problemy ze skalowaniem obrazow i bardzo brzydki efekt graficzny po zastosowaniu metody setBackgroundResource dla
        // danego ImageView postanowilem na utworzenie dla kazdego elementu GridLayout 3 dodatkowych ImageView, ktorch widocznosc bedzie
        // ustalana w zaleznosci od potrzeby za pomoca metody setVisibility(View.Visible) lub (View.GONE). Dzieki temu grafiki sa takie jak
        // w pliku zrodlowym. WAZNE UWAGA TRZEBA DODAC W PLIKU MANIFEST ADNOTACJE -> android:largeHeap="true" zapobiega to problemowi wyswietlania
        // obrazow o zbyt duzej rozdzielczosci i pojemnosci. Kazdy nasz wariant posiada swoje ImageView z obrazem okreslonym w kodzie activity.xml
        // za pomoca src:...., to daje obrazy bez znieksztalcenia.

        carbonexceeded = (ImageView) findViewById(R.id.carbonexceed);
        carbonnormalle = (ImageView) findViewById(R.id.carbonnormal);
        carbonodata = (ImageView) findViewById(R.id.carbonodata);
        nitronormal = (ImageView) findViewById(R.id.nitronormal);
        nitroexceed = (ImageView) findViewById(R.id.nitroexceed);
        nitronodata = (ImageView) findViewById(R.id.nitronodata);
        sulfurnormal= (ImageView) findViewById(R.id.sulfurnormal);
        sulfurexceed = (ImageView) findViewById(R.id.sulfurexceed);
        sulfurnodata = (ImageView) findViewById(R.id.sulfurnodata);
        benzenenormal = (ImageView) findViewById(R.id.benzenenormal);
        benzeneexceed = (ImageView) findViewById(R.id.benzeneexceed);
        benzenenodata = (ImageView) findViewById(R.id.benzenenodata);
        pmtwonormal = (ImageView) findViewById(R.id.pmtwonormal);
        pmtwoexceed = (ImageView) findViewById(R.id.pmtwoexceed);
        pmtwonodata = (ImageView) findViewById(R.id.pmtwonodata);
        pmtennormal = (ImageView) findViewById(R.id.pmtennormal);
        pmtenexceed = (ImageView) findViewById(R.id.pmtenexceed);
        pmtennodata = (ImageView) findViewById(R.id.pmtennodata);

        nitronormal.setVisibility(View.GONE);
        nitroexceed.setVisibility(View.GONE);
        nitronodata.setVisibility(View.GONE);
        nitro.setVisibility(View.VISIBLE);

        sulfurnormal.setVisibility(View.GONE);
        sulfurexceed.setVisibility(View.GONE);
        sulfurnodata.setVisibility(View.GONE);
        sulfur.setVisibility(View.VISIBLE);

        benzenenormal.setVisibility(View.GONE);
        benzeneexceed.setVisibility(View.GONE);
        benzenenodata.setVisibility(View.GONE);
        benzeneee.setVisibility(View.VISIBLE);

        pmtwonormal.setVisibility(View.GONE);
        pmtwoexceed.setVisibility(View.GONE);
        pmtwonodata.setVisibility(View.GONE);
        pmtwo.setVisibility(View.VISIBLE);

        pmtennormal.setVisibility(View.GONE);
        pmtenexceed.setVisibility(View.GONE);
        pmtennodata.setVisibility(View.GONE);
        pmten.setVisibility(View.VISIBLE);

        carbonodata.setVisibility(View.GONE);
        carbonnormalle.setVisibility(View.GONE);
        carbonexceeded.setVisibility(View.GONE);
        carbon.setVisibility(View.VISIBLE);
        refreshData();
        getLocation();
        // listener dla śledzenia zmian stanu pola tekstowego aby w razie zmiany long i latit zaswiecil sie na zielono i umozliwil akcje buttona load city
        gpsLatitude.addTextChangedListener(new TextWatcher() {
            @Override public void afterTextChanged(Editable s) {
                if(s.length() != 0) selectCityFromGPSButton.setBackgroundColor(Color.parseColor("#8BC34A"));
            } @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            } @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                if(s.length() != 0) selectCityFromGPSButton.setBackgroundColor(Color.parseColor("#8BC34A"));
            }
        });
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        locationManager.requestLocationUpdates(LocationManager.GPS_PROVIDER, 100, 0, listener);
        displayData();

        carbon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadCarbonPollutionsActivity(v, inflater);
            }
        });
        carbonexceeded.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadCarbonPollutionsActivity(v, inflater);
            }
        });
        carbonnormalle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadCarbonPollutionsActivity(v, inflater);
            }
        });
        carbonodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadCarbonPollutionsActivity(v, inflater);
            }
        });
        pmten.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadPM10PollutionsActivity(v, inflater);
            }
        });
        pmtennormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadPM10PollutionsActivity(v, inflater);
            }
        });
        pmtenexceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadPM10PollutionsActivity(v, inflater);
            }
        });
        pmtennodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadPM10PollutionsActivity(v, inflater);
            }
        });
        pmtwo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadPM25PollutionsActivity(v, inflater);
            }
        });
        pmtwonormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadPM25PollutionsActivity(v, inflater);
            }
        });
        pmtwoexceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadPM25PollutionsActivity(v, inflater);
            }
        });
        pmtwonodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadPM25PollutionsActivity(v, inflater);
            }
        });
        benzeneee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadBenzenePollutionsActivity(v, inflater);
            }
        });
        benzenenormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadBenzenePollutionsActivity(v, inflater);
            }
        });
        benzeneexceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadBenzenePollutionsActivity(v, inflater);
            }
        });
        benzenenodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadBenzenePollutionsActivity(v, inflater);
            }
        });
        sulfur.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadSulfurPollutionsActivity(v, inflater);
            }
        });
        sulfurnormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadSulfurPollutionsActivity(v, inflater);
            }
        });
        sulfurexceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadSulfurPollutionsActivity(v, inflater);
            }
        });
        sulfurnodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadSulfurPollutionsActivity(v, inflater);
            }
        });
        nitro.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadNitroPollutionsActivity(v, inflater);
            }
        });
        nitronormal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadNitroPollutionsActivity(v, inflater);
            }
        });
        nitroexceed.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadNitroPollutionsActivity(v, inflater);
            }
        });
        nitronodata.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GoAheadNitroPollutionsActivity(v, inflater);
            }
        });

        // listener dla buttona, ktory rozpoczyna akcje szukania danych dla lokalizacji po zaswieceniu sie na zielono buttona load city
        selectCityFromGPSButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                myFirstSound.start();
                // jezeli siec jest dostepna to pobieramy liste miast, kodow i nazw ulic
                if (isNetworkAvailable()) {
                    AsyncTask<Object, Object, List<AirResults>> execute = new API().execute();
                    try {
                        final List<AirResults> mojaLista = execute.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
//                    List<AirResults> listaProbna = new ArrayList<AirResults>();
//                    listaProbna.addAll(NaszeMEtody.cities);
                    if (NaszeMEtody.longitude != 0) {
                        displayData();
                    } else {
                        selectCityFromGPSButton.setBackgroundColor(Color.parseColor("#F44336"));
                        Toast.makeText(getApplicationContext(), "Please wait for a while", Toast.LENGTH_SHORT).show();

                    }
                    // ponizej gotowa lista miast, ulic i kodow do API
                    List<AirResults> listaProbna = new ArrayList<AirResults>();
                    listaProbna.addAll(NaszeMEtody.cities);
                    // tworzymy liste numerow id miast i nazw ulic
                    final List<AirResults> citiesIdsAndStreets = new ArrayList<AirResults>();
                    // tworzymy zmienna z nazwa miasta w polu tekstowym aplikacji
                    String selectedCityName = cityNameTextView.getText().toString();
                    // iterujac po liscie miast wypisujemy do listy roboczej nazwy id miasta i ulicy odpowiadajacej zmiennej selectedCityName
                    NaszeMEtody.stringsForAlertDialog.clear();
                    for(int i = 0; i < listaProbna.size(); i++){
                        if (listaProbna.get(i).getCityName().equals(selectedCityName)){
                            citiesIdsAndStreets.add(new AirResults(listaProbna.get(i).getCityNumber(), listaProbna.get(i).getAddress()));
                            NaszeMEtody.finalCityStations.add(new AirResults(listaProbna.get(i).getCityNumber(), listaProbna.get(i).getAddress(), listaProbna.get(i).getCityName()));
                            NaszeMEtody.stringsForAlertDialog.add(listaProbna.get(i).getAddress());
                        }
                    }

                    // jezeli lista z adresami powiazanymi z wymaganym miastem ma size wiekszy niz 1 to znaczy ze mamy kilka punktow dla
                    // danego miasta i trzeba wybrac lokalizacje z listy

                    if (citiesIdsAndStreets.size() > 0){
                        displayAlertDialog();
                    }else{
                        Toast.makeText(getApplicationContext(), "There is no city available such as your current location", Toast.LENGTH_SHORT).show();
                    }
                }else{
                    Toast.makeText(getApplicationContext(), "Proszę Włączyć Transmisję Danych", Toast.LENGTH_SHORT).show();
                }
            }
        });
        selectPositionManually.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                myFirstSound.start();
                if (isNetworkAvailable()) {
                    AsyncTask<Object, Object, List<AirResults>> execute = new API().execute();
                    try {
                        final List<AirResults> mojaLista = execute.get();
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } catch (ExecutionException e) {
                        e.printStackTrace();
                    }
                    // ponizej gotowa lista miast, ulic i kodow do API
                    final List<AirResults> listaProbna = new ArrayList<AirResults>();
                    listaProbna.addAll(NaszeMEtody.cities);
                    // tworzymy liste numerow id miast i nazw ulic
//                    final List<AirResults> citiesIdsAndStreets = new ArrayList<AirResults>();
//                    // tworzymy zmienna z nazwa miasta w polu tekstowym aplikacji
//                    String selectedCityName = cityNameTextView.getText().toString();
//                    // iterujac po liscie miast wypisujemy do listy roboczej nazwy id miasta i ulicy odpowiadajacej zmiennej selectedCityName
//                    NaszeMEtody.stringsForAlertDialog.clear();
                    for(int i = 0; i < listaProbna.size(); i++){
//                        if (listaProbna.get(i).getCityName().equals(selectedCityName)){
//                            citiesIdsAndStreets.add(new AirResults(listaProbna.get(i).getCityNumber(), listaProbna.get(i).getAddress()));
//                            NaszeMEtody.finalCityStations.add(new AirResults(listaProbna.get(i).getCityNumber(), listaProbna.get(i).getAddress()));
//                            NaszeMEtody.stringsForAlertDialog.add(listaProbna.get(i).getAddress());
//                        }
                        if(listaProbna.get(i).getAddress().matches("null")){
                            stringsForAlertDialogSearcher.add(i+". "+"Miasto: "+listaProbna.get(i).getCityName()+";");
                        }else{
                            stringsForAlertDialogSearcher.add(i+". "+listaProbna.get(i).getCityName()+": "+listaProbna.get(i).getAddress()+";");
                        }


                }
                AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                builder.setView(alertViewSearcher);
                builder.setTitle("Manual Search");
                builder.setIcon(R.drawable.searcher);
                final View finalV = v;
                builder.setMessage("Select Current Position From The List");
                builder.setPositiveButton("Select", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        if (finalV != null) {
                            ViewGroup parentViewGroup = (ViewGroup) finalV.getParent();
                            if (parentViewGroup != null) {
                                parentViewGroup.removeView(alertView);
                            }
                        }
                        try {
                            alertView = inflater.inflate(R.layout.maindialog, null);
                        } catch (InflateException e) {
                        }

                        String[] addresses = new String[stringsForAlertDialogSearcher.size()];
                        addresses = stringsForAlertDialogSearcher.toArray(addresses);
                        AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
                        alertDialog.setTitle("Available Locations Below");
                        alertDialog.setItems(addresses, new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                AirResults selectedCityFromTheList = listaProbna.get(which);
                                String cityNumberOfSelectedLocalization = selectedCityFromTheList.getCityNumber();
                                String cityName = selectedCityFromTheList.getCityName();
                                String address = selectedCityFromTheList.getAddress();
                                NaszeMEtody.cityNameId = cityNumberOfSelectedLocalization;
                                NaszeMEtody.cityName = cityName;
                                NaszeMEtody.address = address;
                                askAboutPollutionsIds();
                                getFinalData();

                            }
                        });
                        alertDialog.create();
                        if (finalV != null) {
                            ViewGroup parentViewGroup = (ViewGroup) finalV.getParent();
                            if (parentViewGroup != null) {
                                parentViewGroup.removeView(alertView);
                                parentViewGroup.removeView(alertViewSearcher);
                            }
                        }
                        alertDialog.show();


                    }
                }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();

                    }
                });
                builder.create();
                    if (finalV != null) {
                        ViewGroup parentViewGroup = (ViewGroup) finalV.getParent();
                        if (parentViewGroup != null) {
                            parentViewGroup.removeView(alertView);
                        }
                    }
                    try {
                        alertView = inflater.inflate(R.layout.manualsearcher, null);
                    } catch (InflateException e) {
                    }
                builder.show();

                }else{
                    Toast.makeText(getApplicationContext(), "Proszę Włączyć Transmisję Danych", Toast.LENGTH_SHORT).show();
                }
            }

        });
    }

    private void GoAheadCarbonPollutionsActivity(View v, final LayoutInflater inflater) {
        // Dzieki temu warunkowi + try nasz child bedzie za kazdym razem wolny od danego parenta przez co nie bedzie
        // problemu z ponownym wyswietlaniem AlertDialoga kilka razy pod rzad
        if (v != null) {
            ViewGroup parentViewGroup = (ViewGroup) v.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(alertView);
            }
        }
        try {
            alertView = inflater.inflate(R.layout.maindialog, null);
        } catch (InflateException e) {

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(alertView);
        builder.setTitle("Do you want to see full information for CO pollution?");
        final View finalV = v;
        builder.setPositiveButton("See the form", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkCondition();
                if(conditionForDataCO){
                    Intent intent = new Intent(MainActivity.this, GraphicsCarbo.class);
                    startActivity(intent);
                }else{
                    if (finalV != null) {
                        ViewGroup parentViewGroup = (ViewGroup) finalV.getParent();
                        if (parentViewGroup != null) {
                            parentViewGroup.removeView(alertView);
                        }
                    }
                    try {
                        alertView = inflater.inflate(R.layout.maindialog, null);
                    } catch (InflateException e) {

                    }
                    mySecondSound.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(alertView);
                    builder.setTitle("WARNING!!!");
                    builder.setMessage("There is no data for CO assigned to your GPS position");
                    builder.setIcon(R.drawable.error);
                    builder.create();
                    builder.show();
                }

            }
        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }
    private void GoAheadSulfurPollutionsActivity(View v, final LayoutInflater inflater) {
        // Dzieki temu warunkowi + try nasz child bedzie za kazdym razem wolny od danego parenta przez co nie bedzie
        // problemu z ponownym wyswietlaniem AlertDialoga kilka razy pod rzad
        if (v != null) {
            ViewGroup parentViewGroup = (ViewGroup) v.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(alertView);
            }
        }
        try {
            alertView = inflater.inflate(R.layout.maindialog, null);
        } catch (InflateException e) {

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(alertView);
        builder.setTitle("Do you want to see full information for SO2 pollution?");
        final View finalV = v;
        builder.setPositiveButton("See form", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkCondition();
                if(conditionForDataSO){
                    Intent intent = new Intent(MainActivity.this, GraphicsSulfur.class);
                    startActivity(intent);
                }else{
                    if (finalV != null) {
                        ViewGroup parentViewGroup = (ViewGroup) finalV.getParent();
                        if (parentViewGroup != null) {
                            parentViewGroup.removeView(alertView);
                        }
                    }
                    try {
                        alertView = inflater.inflate(R.layout.maindialog, null);
                    } catch (InflateException e) {

                    }
                    mySecondSound.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(alertView);
                    builder.setTitle("WARNING!!!");
                    builder.setMessage("There is no data for CO assigned to your GPS position");
                    builder.setIcon(R.drawable.error);
                    builder.create();
                    builder.show();
                }
            }
        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }
    private void GoAheadNitroPollutionsActivity(View v, final LayoutInflater inflater) {
        // Dzieki temu warunkowi + try nasz child bedzie za kazdym razem wolny od danego parenta przez co nie bedzie
        // problemu z ponownym wyswietlaniem AlertDialoga kilka razy pod rzad
        if (v != null) {
            ViewGroup parentViewGroup = (ViewGroup) v.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(alertView);
            }
        }
        try {
            alertView = inflater.inflate(R.layout.maindialog, null);
        } catch (InflateException e) {

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(alertView);
        builder.setTitle("Do you want to see full information for NO2 pollution?");
        final View finalV = v;
        builder.setPositiveButton("See form", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkCondition();
                if (conditionForDataNO) {
                    Intent intent = new Intent(MainActivity.this, GraphicsNitro.class);
                    startActivity(intent);
                } else {
                    if (finalV != null) {
                        ViewGroup parentViewGroup = (ViewGroup) finalV.getParent();
                        if (parentViewGroup != null) {
                            parentViewGroup.removeView(alertView);
                        }
                    }
                    try {
                        alertView = inflater.inflate(R.layout.maindialog, null);
                    } catch (InflateException e) {

                    }
                    mySecondSound.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(alertView);
                    builder.setTitle("WARNING!!!");
                    builder.setMessage("There is no data for CO assigned to your GPS position");
                    builder.setIcon(R.drawable.error);
                    builder.create();
                    builder.show();
                }
            }
        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }
    private void GoAheadPM10PollutionsActivity(View v, final LayoutInflater inflater) {
        // Dzieki temu warunkowi + try nasz child bedzie za kazdym razem wolny od danego parenta przez co nie bedzie
        // problemu z ponownym wyswietlaniem AlertDialoga kilka razy pod rzad
        if (v != null) {
            ViewGroup parentViewGroup = (ViewGroup) v.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(alertView);
            }
        }
        try {
            alertView = inflater.inflate(R.layout.maindialog, null);
        } catch (InflateException e) {

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(alertView);
        builder.setTitle("Do you want to see full information for PM10 pollution?");
        final View finalV = v;
        builder.setPositiveButton("See form", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkCondition();
                if(conditionForDataPM10){
                    Intent intent = new Intent(MainActivity.this, GraphicsPM10.class);
                    startActivity(intent);
                }else{
                    if (finalV != null) {
                        ViewGroup parentViewGroup = (ViewGroup) finalV.getParent();
                        if (parentViewGroup != null) {
                            parentViewGroup.removeView(alertView);
                        }
                    }
                    try {
                        alertView = inflater.inflate(R.layout.maindialog, null);
                    } catch (InflateException e) {

                    }
                    mySecondSound.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(alertView);
                    builder.setTitle("WARNING!!!");
                    builder.setMessage("There is no data for CO assigned to your GPS position");
                    builder.setIcon(R.drawable.error);
                    builder.create();
                    builder.show();
                }
            }
        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }
    private void GoAheadPM25PollutionsActivity(View v, final LayoutInflater inflater) {
        // Dzieki temu warunkowi + try nasz child bedzie za kazdym razem wolny od danego parenta przez co nie bedzie
        // problemu z ponownym wyswietlaniem AlertDialoga kilka razy pod rzad
        if (v != null) {
            ViewGroup parentViewGroup = (ViewGroup) v.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(alertView);
            }
        }
        try {
            alertView = inflater.inflate(R.layout.maindialog, null);
        } catch (InflateException e) {

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(alertView);
        builder.setTitle("Do you want to see full information for PM25 pollution?");
        final View finalV = v;
        builder.setPositiveButton("See form", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkCondition();
                if(conditionForDataPM25){
                    Intent intent = new Intent(MainActivity.this, GraphicsPM25.class);
                    startActivity(intent);
                }else{
                    if (finalV != null) {
                        ViewGroup parentViewGroup = (ViewGroup) finalV.getParent();
                        if (parentViewGroup != null) {
                            parentViewGroup.removeView(alertView);
                        }
                    }
                    try {
                        alertView = inflater.inflate(R.layout.maindialog, null);
                    } catch (InflateException e) {

                    }
                    mySecondSound.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(alertView);
                    builder.setTitle("WARNING!!!");
                    builder.setMessage("There is no data for CO assigned to your GPS position");
                    builder.setIcon(R.drawable.error);
                    builder.create();
                    builder.show();
                }
            }
        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }
    private void GoAheadBenzenePollutionsActivity(final View v, final LayoutInflater inflater) {
        // Dzieki temu warunkowi + try nasz child bedzie za kazdym razem wolny od danego parenta przez co nie bedzie
        // problemu z ponownym wyswietlaniem AlertDialoga kilka razy pod rzad

        if (v != null) {
            ViewGroup parentViewGroup = (ViewGroup) v.getParent();
            if (parentViewGroup != null) {
                parentViewGroup.removeView(alertView);
            }
        }
        try {
            alertView = inflater.inflate(R.layout.maindialog, null);
        } catch (InflateException e) {

        }

        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(alertView);
        builder.setTitle("Do you want to see full information for C6H6 pollution?");
        final View finalV = v;
        builder.setPositiveButton("See form", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                checkCondition();
                if(conditionForDataBenzene){
                    Intent intent = new Intent(MainActivity.this, GraphicsBenzene.class);
                    startActivity(intent);
                }else{
                    if (finalV != null) {
                        ViewGroup parentViewGroup = (ViewGroup) finalV.getParent();
                        if (parentViewGroup != null) {
                            parentViewGroup.removeView(alertView);
                        }
                    }
                    try {
                        alertView = inflater.inflate(R.layout.maindialog, null);
                    } catch (InflateException e) {

                    }
                    mySecondSound.start();
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setView(alertView);
                    builder.setTitle("WARNING!!!");
                    builder.setMessage("There is no data for Benzene assigned to your GPS position");
                    builder.setIcon(R.drawable.error);
                    builder.create();
                    builder.show();
                }

            }
        }).setNegativeButton("Deny", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builder.create();
        builder.show();
    }

    // sprawdzanie czy transmisja danych jest włączona
    private boolean isNetworkAvailable() {
        ConnectivityManager connectivityManager
                = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
    void getLocation(){
        if(ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) !=
                PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION)
                != PackageManager.PERMISSION_GRANTED){
            if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.M){
                requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION,
                Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.INTERNET}, 10);
            }
              return;
        }
        locationManager.requestLocationUpdates(locationManager.GPS_PROVIDER, 100, 0, listener);

    }
    void displayAlertDialog(){
        // w celu wyswietlenia listy ulic w AlertDialogu sporzadzam liste typu <String> o nazwie NaszeMEtody.stringsForAlertDialog i konwertuje
        // ja do tablicy typu String[]. Podsumowujac do wyswietlania danych kolejnosc jest nastepujaca: List<String> -> String[] i to wyswietlamy.
        String[] charSequence = new String[NaszeMEtody.stringsForAlertDialog.size()];
        for (int i = 0; i < NaszeMEtody.stringsForAlertDialog.size(); i++){
            charSequence[i] = String.valueOf(NaszeMEtody.stringsForAlertDialog.get(i));
        }
        final AlertDialog.Builder alertDialog = new AlertDialog.Builder(MainActivity.this);
        alertDialog.setTitle("Select Your Neighbourhood");
        alertDialog.setItems(charSequence, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                myFirstSound.start();
                // najzwyczajniej operujemy tutaj na liscie zdefiniowanej w petli powyzej List<AirResults> a nie na List<String>, ktora stworzylem w celu
                // konwersji do String[] i wyswietlenia.
                // Tworzymy obiekt klasy przenoszacej dane i tworzymy 3 zmienne ktore przechowujemy globalnie do dalszych celow.
                // Sa to nazwa maista, id miasta oraz adres.
                AirResults finalPosition = NaszeMEtody.finalCityStations.get(which);
                String cityNameIdFromTheChoiceList = finalPosition.getCityNumber();
                String cityName = finalPosition.getCityName();
                String address = finalPosition.getAddress();
                NaszeMEtody.cityNameId = cityNameIdFromTheChoiceList;
                NaszeMEtody.cityName = cityName;
                NaszeMEtody.address = address;
                askAboutPollutionsIds();
                getFinalData();
            }
        });
        alertDialog.create();
        alertDialog.show();
    }
    void getFinalData(){
        finalValuesList.clear();
        AsyncTask<Object, Object, List<FinalValues>> execute = new FinalValuesAPI().execute();
        try {
            final List<FinalValues> mojaLista = execute.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<FinalValues> finalResultsList = new ArrayList<FinalValues>();
        finalResultsList.addAll(finalValuesList);
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        builder.setView(alertViewGeneralInfoForResults);
        builder.setIcon(R.drawable.result);
        builder.setTitle("Already Done");
        builder.setMessage("Data set available for selected city: "+ NaszeMEtody.address+", "+NaszeMEtody.cityName);
        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });
        builder.create();
        builder.show();
        adjustDataToButtonsImages();
    }

    void adjustDataToButtonsImages(){
        List<FinalValues> list = new ArrayList<>();
        list.addAll(finalValuesList);
        List<FinalValues> listWithoutNullValues = new ArrayList<>();
        for(FinalValues element:list){
            if (!element.getValue().equals("null")){
                listWithoutNullValues.add(new FinalValues(element.getValue(), element.getDateFormat(),element.getKey()));
            }else {
                // UWAGA WAZNE!!!! Pamietamy aby unikac FatalFormatException liczby z czesciami dziesietnymi piszemy po KROPCE a nie przecinku
                listWithoutNullValues.add(new FinalValues("0.00", element.getDateFormat(), element.getKey()));
            }
        }
        // Ladujemy w celu segregacji wartosci uzyskane z uslugi API wartosci w postaci listy ze wszystkimi dostepnymi danymi dla danej lokalizacji
        // do list roboczych, w ktorych beda wartosci i daty z godzinami odpowiadajace danemu zanieczyszczeniu
        List<FinalValues> pm10 = new ArrayList<>();
        List<FinalValues> pm25 = new ArrayList<>();
        List<FinalValues> benzene = new ArrayList<>();
        List<FinalValues> carbonOxide = new ArrayList<>();
        List<FinalValues> sulfurDioxide = new ArrayList<>();
        List<FinalValues> nitroDioxide = new ArrayList<>();
        for (FinalValues element : listWithoutNullValues){
            if(element.getKey().equals("PM10")){
                pm10.add(new FinalValues(element.getValue(), element.getDateFormat()));
                pm10NM.add(new FinalValues(element.getValue(), element.getDateFormat()));
            }
            if(element.getKey().equals("SO2")){
                sulfurDioxide.add(new FinalValues(element.getValue(), element.getDateFormat()));
                sulfurDioxideNM.add(new FinalValues(element.getValue(), element.getDateFormat()));
            }
            if(element.getKey().equals("CO")){
                carbonOxide.add(new FinalValues(element.getValue(), element.getDateFormat()));
                carbonOxideNM.add(new FinalValues(element.getValue(), element.getDateFormat()));
            }
            if(element.getKey().equals("NO2")){
                nitroDioxide.add(new FinalValues(element.getValue(), element.getDateFormat()));
                nitroDioxideNM.add(new FinalValues(element.getValue(), element.getDateFormat()));
            }
            if(element.getKey().equals("PM2.5")){
                pm25.add(new FinalValues(element.getValue(), element.getDateFormat()));
                pm25NM.add(new FinalValues(element.getValue(), element.getDateFormat()));
            }
            if(element.getKey().equals("C6H6")){
                benzene.add(new FinalValues(element.getValue(), element.getDateFormat()));
                benzeneNM.add(new FinalValues(element.getValue(), element.getDateFormat()));
            }


        }
        if (!pm10.isEmpty()) {
            for (int i = 0; i < pm10.size(); i++) {
                String numberToConvert = pm10.get(i).getValue();
                float f = Float.valueOf(numberToConvert.trim()).floatValue();
                // Jezeli wartosc f jest mniejsza niz norma graniczna 101 to ustawiamy wedlug niej widoczna ikone a wczesniej
                // ladujemy do listy wszystkie wartosci. Mamy zatem rodzaj ikony w main activity ustawiony wedlug
                // pierwszej roznej od zera wartosci f po czym ladujemy wszystko do NaszeMetody class
                if (f != 0.00 && f > 101) {
                    float[] listWithValuesForGraphic = new float[pm10.size()];
                    String[] listWithDataAndTimeValues = new String[pm10.size()];
                    for (int b = 0; b < pm10.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(pm10.get(b).getValue());
                        listWithDataAndTimeValues[b] = pm10.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForPM10 = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForPM10 = listWithDataAndTimeValues;
                    }
                    pmtennormal.setVisibility(View.GONE);
                    pmtenexceed.setVisibility(View.VISIBLE);
                    pmtennodata.setVisibility(View.GONE);
                    pmten.setVisibility(View.GONE);
                    break;
                } else if(f != 0.00){
                    // Jezeli jednak wartosc f jest mniejsza od normy granicznej, ladujemy wszystkie dane do list i tablic
                    // w NaszeMetody i ustawiamy ikone w main activity wedlug pierwszej roznej od zera wartosci f, ktora jest mniejsza
                    // od normy granicznej 101.
                    float[] listWithValuesForGraphic = new float[pm10.size()];
                    String[] listWithDataAndTimeValues = new String[pm10.size()];
                    for (int b = 0; b < pm10.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(pm10.get(b).getValue());
                        listWithDataAndTimeValues[b] = pm10.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForPM10 = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForPM10 = listWithDataAndTimeValues;
                    }
                    pmtennormal.setVisibility(View.VISIBLE);
                    pmtenexceed.setVisibility(View.GONE);
                    pmtennodata.setVisibility(View.GONE);
                    pmten.setVisibility(View.GONE);
                    break;
                }}
        }else{
            pmtennormal.setVisibility(View.GONE);
            pmtenexceed.setVisibility(View.GONE);
            pmtennodata.setVisibility(View.VISIBLE);
            pmten.setVisibility(View.GONE);
        }

        if(!pm25.isEmpty()){
            for (int i = 0; i < pm25.size(); i++){
                String numberToConvert = pm25.get(i).getValue();
                float f = Float.valueOf(numberToConvert.trim()).floatValue();
                if (f != 0.00 && f > 61){
                    float[] listWithValuesForGraphic = new float[pm25.size()];
                    String[] listWithDataAndTimeValues = new String[pm25.size()];
                    for (int b = 0; b < pm25.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(pm25.get(b).getValue());
                        listWithDataAndTimeValues[b] = pm25.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForPM25 = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForPM25 = listWithDataAndTimeValues;
                    }
                    pmtwonormal.setVisibility(View.GONE);
                    pmtwoexceed.setVisibility(View.VISIBLE);
                    pmtwonodata.setVisibility(View.GONE);
                    pmtwo.setVisibility(View.GONE);
                    break;
                }else if(f != 0.00){
                    float[] listWithValuesForGraphic = new float[pm25.size()];
                    String[] listWithDataAndTimeValues = new String[pm25.size()];
                    for (int b = 0; b < pm25.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(pm25.get(b).getValue());
                        listWithDataAndTimeValues[b] = pm25.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForPM25 = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForPM25 = listWithDataAndTimeValues;
                    }
                    pmtwonormal.setVisibility(View.VISIBLE);
                    pmtwoexceed.setVisibility(View.GONE);
                    pmtwonodata.setVisibility(View.GONE);
                    pmtwo.setVisibility(View.GONE);
                    break;
                }}
        } else{
            pmtwonormal.setVisibility(View.GONE);
            pmtwoexceed.setVisibility(View.GONE);
            pmtwonodata.setVisibility(View.VISIBLE);
            pmtwo.setVisibility(View.GONE);
        }

        if(!nitroDioxide.isEmpty()){
            for (int i =0; i < nitroDioxide.size(); i++){
                String numberToConvert = nitroDioxide.get(i).getValue();
                float f = Float.valueOf(numberToConvert.trim()).floatValue();
                if (f != 0.00 && f > 151){
                    float[] listWithValuesForGraphic = new float[nitroDioxide.size()];
                    String[] listWithDataAndTimeValues = new String[nitroDioxide.size()];
                    for (int b = 0; b < nitroDioxide.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(nitroDioxide.get(b).getValue());
                        listWithDataAndTimeValues[b] = nitroDioxide.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForNO = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForNO = listWithDataAndTimeValues;
                    }
                    nitronormal.setVisibility(View.GONE);
                    nitroexceed.setVisibility(View.VISIBLE);
                    nitronodata.setVisibility(View.GONE);
                    nitro.setVisibility(View.GONE);
                    break;
                }else if(f != 0.00){
                    float[] listWithValuesForGraphic = new float[nitroDioxide.size()];
                    String[] listWithDataAndTimeValues = new String[nitroDioxide.size()];
                    for (int b = 0; b < nitroDioxide.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(nitroDioxide.get(b).getValue());
                        listWithDataAndTimeValues[b] = nitroDioxide.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForNO = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForNO = listWithDataAndTimeValues;
                    }
                    nitronormal.setVisibility(View.VISIBLE);
                    nitroexceed.setVisibility(View.GONE);
                    nitronodata.setVisibility(View.GONE);
                    nitro.setVisibility(View.GONE);
                    break;
                }}
        }else{
            nitronormal.setVisibility(View.GONE);
            nitroexceed.setVisibility(View.GONE);
            nitronodata.setVisibility(View.VISIBLE);
            nitro.setVisibility(View.GONE);
        }
        if(!sulfurDioxide.isEmpty()){
            for (int i = 0; i < sulfurDioxide.size(); i++){
                String numberToConvert = sulfurDioxide.get(i).getValue();
                float f = Float.valueOf(numberToConvert.trim()).floatValue();
                if (f != 0.00 && f > 201){
                    float[] listWithValuesForGraphic = new float[sulfurDioxide.size()];
                    String[] listWithDataAndTimeValues = new String[sulfurDioxide.size()];
                    for (int b = 0; b < sulfurDioxide.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(sulfurDioxide.get(b).getValue());
                        listWithDataAndTimeValues[b] = sulfurDioxide.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForSO = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForSO = listWithDataAndTimeValues;
                    }
                    sulfurnormal.setVisibility(View.GONE);
                    sulfurexceed.setVisibility(View.VISIBLE);
                    sulfurnodata.setVisibility(View.GONE);
                    sulfur.setVisibility(View.GONE);
                    break;
                }else if(f != 0.00){
                    float[] listWithValuesForGraphic = new float[sulfurDioxide.size()];
                    String[] listWithDataAndTimeValues = new String[sulfurDioxide.size()];
                    for (int b = 0; b < sulfurDioxide.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(sulfurDioxide.get(b).getValue());
                        listWithDataAndTimeValues[b] = sulfurDioxide.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForSO = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForSO = listWithDataAndTimeValues;
                    }
                    sulfurnormal.setVisibility(View.VISIBLE);
                    sulfurexceed.setVisibility(View.GONE);
                    sulfurnodata.setVisibility(View.GONE);
                    sulfur.setVisibility(View.GONE);
                    break;
                }}
        }else{
            sulfurnormal.setVisibility(View.GONE);
            sulfurexceed.setVisibility(View.GONE);
            sulfurnodata.setVisibility(View.VISIBLE);
            sulfur.setVisibility(View.GONE);
        }
        if(!benzene.isEmpty()){
            for (int i = 0; i < benzene.size(); i++){
                String numberToConvert = benzene.get(i).getValue();
                float f = Float.valueOf(numberToConvert.trim()).floatValue();
                if (f != 0.00 && f > 16){
                    float[] listWithValuesForGraphic = new float[benzene.size()];
                    String[] listWithDataAndTimeValues = new String[benzene.size()];
                    for (int b = 0; b < benzene.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(benzene.get(b).getValue());
                        listWithDataAndTimeValues[b] = benzene.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForBenzene = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForBenzene = listWithDataAndTimeValues;
                    }
                    benzenenormal.setVisibility(View.GONE);
                    benzeneexceed.setVisibility(View.VISIBLE);
                    benzenenodata.setVisibility(View.GONE);
                    benzeneee.setVisibility(View.GONE);
                    break;
                }else if(f != 0.00){
                    float[] listWithValuesForGraphic = new float[benzene.size()];
                    String[] listWithDataAndTimeValues = new String[benzene.size()];
                    for (int b = 0; b < benzene.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(benzene.get(b).getValue());
                        listWithDataAndTimeValues[b] = benzene.get(b).getDateFormat();
                        NaszeMEtody.listOfValuesForBenzene = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForBenzene = listWithDataAndTimeValues;
                    }
                    benzenenormal.setVisibility(View.VISIBLE);
                    benzeneexceed.setVisibility(View.GONE);
                    benzenenodata.setVisibility(View.GONE);
                    benzeneee.setVisibility(View.GONE);
                    break;
                }}
        }else{
            benzenenormal.setVisibility(View.GONE);
            benzeneexceed.setVisibility(View.GONE);
            benzenenodata.setVisibility(View.VISIBLE);
            benzeneee.setVisibility(View.GONE);
        }
        if(!carbonOxide.isEmpty()){
            for (int i = 0; i < carbonOxide.size(); i++){
                String numberToConvert = carbonOxide.get(i).getValue();
                float f = Float.valueOf(numberToConvert.trim()).floatValue();
                if (f != 0.00 && f > 11){
                    float[] listWithValuesForGraphic = new float[carbonOxide.size()];
                    String[] listWithDataAndTimeValues = new String[carbonOxide.size()];
                    for (int b = 0; b < carbonOxide.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(carbonOxide.get(b).getValue());
                        listWithDataAndTimeValues[b] = carbonOxide.get(b).getDateFormat();
                        listOfValuesForCO = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForCO = listWithDataAndTimeValues;
                    }
                    carbonexceeded.setVisibility(View.VISIBLE);
                    carbonnormalle.setVisibility(View.GONE);
                    carbon.setVisibility(View.GONE);
                    break;
                }else if(f != 0.00){
                    float[] listWithValuesForGraphic = new float[carbonOxide.size()];
                    String[] listWithDataAndTimeValues = new String[carbonOxide.size()];
                    for (int b = 0; b < carbonOxide.size(); b++) {
                        listWithValuesForGraphic[b] = Float.parseFloat(carbonOxide.get(b).getValue());
                        listWithDataAndTimeValues[b] = carbonOxide.get(b).getDateFormat();
                        listOfValuesForCO = listWithValuesForGraphic;
                        NaszeMEtody.listOfDataAndTimeValForCO = listWithDataAndTimeValues;
                    }
                    carbonexceeded.setVisibility(View.GONE);
                    carbonnormalle.setVisibility(View.VISIBLE);
                    carbon.setVisibility(View.GONE);
                    break;
                }}
        }else{
            carbonodata.setVisibility(View.VISIBLE);
            carbonexceeded.setVisibility(View.GONE);
            carbonnormalle.setVisibility(View.GONE);
            carbon.setVisibility(View.GONE);
        }
    }
    void displayAlertDialogForManualSearch(){

    }

    void refreshData(){
        listener = new LocationListener() {
            @Override
            public void onLocationChanged(Location location) {
                double longit = location.getLongitude();
                double latit = location.getLatitude();
                gpsLatitude.setText(String.valueOf(longit));
                gpsLongitude.setText(String.valueOf(latit));
                NaszeMEtody.latitude = latit;
                NaszeMEtody.longitude = longit;
            }

            @Override
            public void onStatusChanged(String provider, int status, Bundle extras) {

            }

            @Override
            public void onProviderEnabled(String provider) {

            }

            @Override
            public void onProviderDisabled(String provider) {

            }
        };
    }
    // wyświetlanie danych w polu
    void displayData(){
        Geocoder geocoder = new Geocoder(this, Locale.getDefault());
        if(NaszeMEtody.latitude != 0 || NaszeMEtody.longitude != 0){
            try {
                List<Address> addresses = geocoder.getFromLocation(NaszeMEtody.latitude, NaszeMEtody.longitude, 1);
                String cityName = addresses.get(0).getLocality();
                cityNameTextView.setText(cityName);
                NaszeMEtody.cityName = cityName;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }else{
            cityNameTextView.setText("Can not find your current position");
        }
    }
    void askAboutPollutionsIds(){
        pollutionsIds.clear();
        AsyncTask<Object, Object, List<AirResultsStationsIds>> execute = new StationIdsSearcherAPI().execute();
        try {
            final List<AirResultsStationsIds> mojaLista = execute.get();
        } catch (InterruptedException e) {
            e.printStackTrace();
        } catch (ExecutionException e) {
            e.printStackTrace();
        }
        List<AirResultsStationsIds> listaProbna = new ArrayList<AirResultsStationsIds>();
        // w listaProbna mam dane rejestrowane przez dana stacje pomiarowa. Robimy to na podstawie id danego miasta
        listaProbna.addAll(pollutionsIds);
    }

    public void checkCondition(){

            if (carbonOxideNM.size()==0){
                conditionForDataCO = false;
            }
            if (benzeneNM.size()==0){
                conditionForDataBenzene = false;
            }
            if (nitroDioxideNM.size()==0){
                conditionForDataNO = false;
            }
            if (sulfurDioxideNM.size()==0){
                conditionForDataSO = false;
            }
            if (pm10NM.size()==0){
                conditionForDataPM10 = false;
            }
            if (pm25NM.size()==0){
                conditionForDataPM25 = false;
            }
        }


// uprawnienia
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode){
            case REQUEST_LOCATION:

                break;
            default:
                break;
        }

    }

    // klasa implementuje interface LocationListener dlatego implementuje metody poniżej
    @Override
    public void onLocationChanged(Location location) {
        double longit = location.getLongitude();
        double latit = location.getLatitude();
        gpsLatitude.setText(String.valueOf(longit));
        gpsLongitude.setText(String.valueOf(latit));
    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

}

