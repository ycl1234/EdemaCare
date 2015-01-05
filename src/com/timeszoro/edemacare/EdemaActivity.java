package com.timeszoro.edemacare;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;
import com.example.edemacare.R;

/**
 * Created by Administrator on 2015/1/5.
 */
public class EdemaActivity extends Activity {

    private final  int CUR_FRE = 5;
    private final String TAG = "Edema data";
    private int mCurFre = CUR_FRE;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edemashow);

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
    }
}
