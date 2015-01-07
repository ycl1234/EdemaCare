package com.timeszoro.edemacare;

import android.app.Fragment;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.example.edemacare.R;
import com.timeszoro.service.TimerService;


/**
 * Created by Administrator on 2015/1/6.
 */
public class TimeCountFragment extends Fragment {
    //actions between service and activity
    private final String ACTION_SERVICE_2_ACTIVITY = "com.timeszoro.timer2activity";
    private final String ACTION_ACTIVITY_2_SERVICE = "com.timeszoro.activity2timer";
    //UI elements
    TextView mHoursText;
    TextView mMinText;
    TextView mSecondText;
    //BroadCast Receiver
    private TimerFromServieReceiver mReceiver;
    private IntentFilter mFilter;
    //Service Connection
    private boolean mConnected = false;
    private static boolean existed = false;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //init

        mReceiver = new TimerFromServieReceiver();
        mFilter = new IntentFilter();
        mFilter.addAction(ACTION_SERVICE_2_ACTIVITY);
        //bind service
        if(!existed){
            Intent intent = new Intent(getActivity(), TimerService.class);
            getActivity().bindService(intent,conn,Context.BIND_AUTO_CREATE);
            existed = false;
        }

    }

    @Override
    public void onResume() {
        super.onResume();
        getActivity().registerReceiver(mReceiver,mFilter);
//        mHoursText.setText("00");
//        mMinText.setText("00");
//        mSecondText.setText("00");
    }

    @Override
    public void onPause() {
        super.onPause();
        getActivity().unregisterReceiver(mReceiver);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_timercount, container, false);
        mHoursText = (TextView)v.findViewById(R.id.hours);
        mMinText = (TextView)v.findViewById(R.id.mins);
        mSecondText = (TextView)v.findViewById(R.id.seconds);

        return v;
    }

    /**
     * Get the current time from TimerService,and update the TextView
     */
    class TimerFromServieReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            int hours = bundle.getInt(getString(R.string.ble_sendtime_hours),0);
            int mins = bundle.getInt(getString(R.string.ble_sendtime_mins),0);
            int seconds = bundle.getInt(getString(R.string.ble_sendtime_seconds),0);

            if(hours < 10){
               mHoursText.setText("0" + hours);
            }
            else{
                mHoursText.setText(String.valueOf(hours));
            }
            if(mins < 10){
                mMinText.setText("0" + mins);
            }
            else{
                mMinText.setText(String.valueOf(mins));
            }
            if(seconds < 10){
                mSecondText.setText("0" + seconds);
            }
            else{
                mSecondText.setText(String.valueOf(seconds));
            }
        }
    }

    /**
     * Bind with the TimerService
//     */
//    protected void bindService()     //启动TimeService服务，开始计时
//    {
//        if (!ifBind)//没有绑定则绑定
//        {
//            Intent intent = new Intent(Mychronometer.this, TimeService.class);
//            bindService(intent, conn, Context.BIND_AUTO_CREATE);//绑定服务
//            ifBind = true;
//        } else//已经绑定则广播告诉TimeService.java开启线程
//        {
//            Intent intentStart = new Intent();
//            intentStart.putExtra("StartOrNot", true);
//            intentStart.setAction(ACTIONActivityToService);
//            sendBroadcast(intentStart);
//        }
//    }


    private void stopService(){
        if(mConnected){
            getActivity().unbindService(conn);
            mConnected = false;
        }

//        if(mConnected){
//            unbi
//        }
//        Intent intent = new Intent();
//        intent.putExtra(getString(R.string.ble_time_start), false);
//        intent.setAction(ACTION_ACTIVITY_2_SERVICE);
//        getActivity().sendBroadcast(intent);
    }



    private ServiceConnection conn = new ServiceConnection(){
        @Override
        public void onServiceConnected(ComponentName name, IBinder service) {
            mConnected = true;
            //get the bind of the service,
            TimerService.TimerBinder binder = (TimerService.TimerBinder)service;
            TimerService timerService = binder.getService();
            timerService.startCount();
        }

        @Override
        public void onServiceDisconnected(ComponentName name) {


        }
    };
}
