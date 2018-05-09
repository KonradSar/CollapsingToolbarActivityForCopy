package com.example.konrad.collapsingtoolbaractivityforcopy;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.LimitLine;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.github.mikephil.charting.formatter.IAxisValueFormatter;

import java.util.ArrayList;

import static com.example.konrad.collapsingtoolbaractivityforcopy.NaszeMEtody.listOfColors;

public class GraphicsPM25 extends AppCompatActivity {
    private float[] yData = NaszeMEtody.listOfValuesForPM25;
    private String[] xData = NaszeMEtody.listOfDataAndTimeValForPM25;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_pm25);
        barChart = (BarChart) findViewById(R.id.pieChartPMTwoPointFive);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        setUpPieCharts();
        TextView textView;
        textView = (TextView) findViewById(R.id.graphicDesc);
        textView.setText("Concentration of PM25 measurements in the air [\u00B5g]/m3 (Standard concentration is below 60 [\u00B5g]/m3)");
        textView.setTextColor(Color.BLACK);
    }
    private void setUpPieCharts() {

        ArrayList<BarEntry> barEntries = new ArrayList<>();
        for (int i = 0; i < yData.length; i++) {
            barEntries.add(new BarEntry(i, yData[i]));
        }
        BarDataSet barDataSet = new BarDataSet(barEntries, "Data");

        final ArrayList<String> time = new ArrayList<>();
        for(int i = 0; i < xData.length; i++){
            time.add(xData[i].substring((xData[i].length()-8),(xData[i].length()-3)));
        }
        XAxis xAxis = barChart.getXAxis();
        // Lista ma wartosci od 0 do 13
        for(int i = 0; i < 14; i++){
            if(barEntries.get(i).getY() > 60) {
                //Pobieramy kolejno wartosci osi Y czyli 1,2,3,4.... z tym, ze sa to float czyli wartosci 1.0, 2.0, 3.0, 4.0..
                // dlatego musimy je przekonwertowac do typu int. Kazdy element osi Y to kolejny int po przekonwertowaniu.
                // Gdy mamy wartosci int mozemy teraz przypisac kolor do tablicy int[] o indeksie int = 1,2,3,4...Calosc polega na tym, ze
                // jezeli wartosc osi Y jest wieksza od 10 to pobieramy przypisana jej wartosc z osi Y wyrazona w float. Nastepnie przetwarzamy
                // do int i dzieki temu definiujac tablice intow z indeksami rownymi wartosciom osi Y i wartosciami koloru Red lub Green kolorujemy
                // caly wykres metoda barDataSet.setColors(listOfColors) gdzie listOfColors to public static int[10] czyli od 0 do9.
                // Wartosci osi Y to np 1.00, 408.23/ 2.00, 322.22/.....
                float position1 = barEntries.get(i).getX();
                String position = String.valueOf(position1);
                // Metoda substring pozwala na wycinanie z danej DateTime tylko wartosci godziny czyli z danej 28-04-2018 21:00:00 mamy tylko String
                // o wartosci ""21:00"" i przypisujemy go do osi X jako xAxis.
                if (i < 10){
                    int position3 = Integer.valueOf(position.substring((0),(1)));
                    listOfColors[position3] = Color.RED;
                }else{
                    int position3 = Integer.valueOf(position.substring((0),(2)));
                    listOfColors[position3] = Color.RED;
                }


            }else{
                float position1 = barEntries.get(i).getX();
                String position = String.valueOf(position1);
                if (i < 10){
                    int position3 = Integer.valueOf(position.substring((0),(1)));
                    listOfColors[position3] = Color.GREEN;
                }else{
                    int position3 = Integer.valueOf(position.substring((0),(2)));
                    listOfColors[position3] = Color.GREEN;
                }

            }
        }
        barDataSet.setColors(listOfColors);
        xAxis.getPosition();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);
        xAxis.setValueFormatter(new IAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, AxisBase axis) {
                return time.get((int) value);
            }
        });

        BarData theData = new BarData(barDataSet);
        barChart.animateXY(1000, 1000);
        barChart.setData(theData);
        // Definiujemy LimitLine z wartoscia jako poziom graniczny dla wartosci dopuszczalnych plus kolor, grubosc i rozmiar textu
        // jednak jest ona tak mala ze nie jest widoczna na skali
        int maxCapacity = 60;
        LimitLine limitLineForGraphic = new LimitLine(maxCapacity, "Max Value for PM25 is 60 "+"\u00B5"+"g/m3");
        barChart.getAxisLeft().addLimitLine(limitLineForGraphic);
        limitLineForGraphic.setLineWidth(3f);
        limitLineForGraphic.setTextSize(12f);
        limitLineForGraphic.setTextColor(Color.BLUE);
        limitLineForGraphic.setLineColor(Color.YELLOW);
        barChart.setHighlightPerDragEnabled(true);
        xAxis.setDrawAxisLine(true);
        xAxis.setDrawGridLines(false);
    }
}
