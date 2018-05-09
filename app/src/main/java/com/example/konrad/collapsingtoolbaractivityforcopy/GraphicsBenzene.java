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

public class GraphicsBenzene extends AppCompatActivity {
    private float[] yData = NaszeMEtody.listOfValuesForBenzene;
    private String[] xData = NaszeMEtody.listOfDataAndTimeValForBenzene;
    BarChart barChart;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_graphics_benzene);
        barChart = (BarChart) findViewById(R.id.pieChartBenzene);
        barChart.getDescription().setEnabled(false);
        barChart.getLegend().setEnabled(false);
        setUpPieCharts();
        TextView textView;
        textView = (TextView) findViewById(R.id.graphicDescBenzene);
        textView.setText("Concentration of Benzene measurements in the air [\u00B5g]/m3 (Standard concentration is below 16 [\u00B5g]/m3)");
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
        // Jedziemy po wartosciach y i spradzamy czy wartosc jest wieksza czy mniejsza od normy. W zaleznosci od tego jest ustawiany rozny kolor
        // dla jej pozycji
        // 14 to rozmiar tablicy czyli iterujemy do [13] elementu
        for(int i = 0; i < 14; i++){
            // 15 to norma jakosci dla poziomu umiarkowany
            if(barEntries.get(i).getY() > 15) {
                float position1 = barEntries.get(i).getX();
                String position = String.valueOf(position1);
                // Dla wartosci wiekszych niz 10 musimy zrobic substring (0,2) bo inaczej wycinalibysmy dla wartosci 10,11,12,13 tylko 1
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
                }}}
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
        int maxCapacity = 15;
        LimitLine limitLineForGraphic = new LimitLine(maxCapacity, "Max Value for Benzene is 15 "+"\u00B5"+"g/m3");
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
