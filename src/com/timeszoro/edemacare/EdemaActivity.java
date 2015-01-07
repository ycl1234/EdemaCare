package com.timeszoro.edemacare;

import android.app.Activity;
import android.app.Fragment;
import android.os.Bundle;
import android.util.Log;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;
import com.example.edemacare.R;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.data.*;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/5.
 */
public class EdemaActivity extends Activity {

    private final  int CUR_FRE = 5;
    private final String TAG = "Edema data";
    private int mCurFre = CUR_FRE;
    private TimeCountFragment mTimerFragment;


    //information of the data chart
    LineChart mLineChart ;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edemashow);
        //init fragment
        mTimerFragment = new TimeCountFragment();

        getFragmentManager().beginTransaction().add(R.id.fragment_time_count,mTimerFragment).commit();




        //init the wheel of frequence selection

        final AbstractWheel frequenceSel = (AbstractWheel) findViewById(R.id.ble_frequence_selection);
        NumericWheelAdapter freAdapter = new NumericWheelAdapter(this, 0, 59, "%02d");
        freAdapter.setItemResource(R.layout.wheel_text_centered_item);
        freAdapter.setItemTextResource(R.id.text);
        frequenceSel.setViewAdapter(freAdapter);
        frequenceSel.setCurrentItem(CUR_FRE);
        frequenceSel.addScrollingListener(new OnWheelScrollListener() {
            @Override
            public void onScrollingStarted(AbstractWheel wheel) {
                Log.d(TAG,"wheel scrolling");
            }

            @Override
            public void onScrollingFinished(AbstractWheel wheel) {
                Log.d(TAG,"wheel finished");
                //set current item number
                mCurFre = wheel.getCurrentItem();


            }
        });

        //init the data chart
        mLineChart = (LineChart)findViewById(R.id.chart1);
        mLineChart.setDrawGridBackground(false);//do not draw the grid
        mLineChart.setDrawYValues(false);//do not draw the y value into the chart
        mLineChart.setHighlightEnabled(true);
        mLineChart.setTouchEnabled(true);// enable touch gestures
        mLineChart.setDragEnabled(true);// enable scaling and dragging
        mLineChart.setScaleEnabled(true);
        mLineChart.setPinchZoom(false);// if disabled, scaling can be done on x- and y-axis separately
         //add the data of the chart
        int x = 20;
        int y = 50;
        ArrayList<String> xVals = new ArrayList<String>();
        for (int i = 0; i < x; i++) {
            xVals.add((i) + "");
        }
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        for (int z = 0; z < 3; z++) {

            ArrayList<Entry> values = new ArrayList<Entry>();

            for (int i = 0; i < x; i++) {
                double val = (Math.random() * y) + 3;
                values.add(new Entry((float) val, i));
            }

            LineDataSet d = new LineDataSet(values, "DataSet " + (z + 1));
            d.setLineWidth(2.5f);
            d.setCircleSize(4f);


            dataSets.add(d);
        }
        LineData lineData = new LineData(xVals,dataSets);
        mLineChart.setData(lineData);
        mLineChart.invalidate();
    }
}
