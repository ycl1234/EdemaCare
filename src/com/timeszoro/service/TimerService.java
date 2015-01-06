/**
 * Created by Timeszoro on 2015/1/6.
 */
package com.timeszoro.service;

import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.*;
import com.example.edemacare.R;




public class TimerService extends Service implements Runnable{
    //actions between service and activity
    private final String ACTION_SERVICE_2_ACTIVITY = "com.timeszoro.timer2activity";
    private final String ACTION_ACTIVITY_2_SERVICE = "com.timeszoro.activity2timer";

    //time count
    private int mHours;
    private int mMins;
    private int mSeconds;
    //binder of the connection
    private TimerBinder mBinder = new TimerBinder();
    //time count hander
    private Handler mHandler;
    private final String TIME_ADD ="com.timeszoro.where_add_time";
    //send current time
    private Intent mIntentSendTime;
    //whether the count is runing
    private boolean mCountRunning = false;
    //time count service
    private TimeSreviceReceiver mServiceReceiver;
    /**
     * Send the time add message every one second , which will be processed in handler
     */
    @Override
    public void run() {
        mCountRunning = true;
        while(true && mCountRunning){
            Message message = Message.obtain();
            Bundle bundle = new Bundle();
            bundle.putBoolean(TIME_ADD,true);
            message.setData(bundle);
            mHandler.sendMessage(message);
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }


    }

    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    /**
     * get the binder of timerservice
     */
    public class TimerBinder extends Binder{
        public TimerService getService(){
            return TimerService.this;
        }
    }

    @Override
    public void onCreate() {
        super.onCreate();
        //init
        mIntentSendTime = new Intent();
        //register the broadcast receiver
        mServiceReceiver = new TimeSreviceReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction(ACTION_ACTIVITY_2_SERVICE);
        registerReceiver(mServiceReceiver,filter);
        //handler the timer count message
        mHandler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                //get the message , where add timer or not
                boolean timeAdd = msg.getData().getBoolean(TIME_ADD);
                if(timeAdd){
                    if(mSeconds + 1 == 60){
                        if(mMins + 1 == 60){
                            mHours ++;
                            mMins = 0;
                        }
                        else{
                            mMins ++;
                        }
                        mSeconds = 0;
                    }
                    else{
                        mSeconds ++;
                    }
                }

                //put the time information in the intent ,and send the data to activity
                mIntentSendTime.putExtra(getString(R.string.ble_sendtime_hours),mHours);
                mIntentSendTime.putExtra(getString(R.string.ble_sendtime_mins),mMins);
                mIntentSendTime.putExtra(getString(R.string.ble_sendtime_seconds),mSeconds);
                mIntentSendTime.setAction(ACTION_SERVICE_2_ACTIVITY);
                sendBroadcast(mIntentSendTime);
            }
        };
    }
    /**
     * process the broadcast send from
     */
    class TimeSreviceReceiver extends BroadcastReceiver{
        @Override
        public void onReceive(Context context, Intent intent) {
            Bundle bundle = intent.getExtras();
            boolean startCount = bundle.getBoolean(getString(R.string.ble_time_start),true);
            if(!mCountRunning && startCount){
                new Thread(TimerService.this).start();
            }
            else if(mCountRunning && !startCount){
                mCountRunning = false;
            }
        }
    }

    /**
     * begin count the time
     */
    public void startCount(){
        new Thread(TimerService.this).start();
    }
    /**
     * stop count the timer
     */
    public void stopCount(){
        mCountRunning = false;
    }
}
