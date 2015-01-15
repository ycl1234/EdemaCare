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
import android.util.Log;
import com.example.edemacare.R;




public class TimerService extends Service{
    //actions between service and activity
    private final String ACTION_SERVICE_2_ACTIVITY = "com.timeszoro.timer2activity";

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
    private boolean mFlag = false;
    private static TimeThread mThread;
    //static
    private static int mAllThreadNum = 0;
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        stopCount();
        stopSelf();
        return super.onUnbind(intent);
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


    public void init(){
        mHours = 0;
        mMins = 0;
        mSeconds = 0;
    }
    /**
     * begin count the time
     */
    public void startCount() {
//        mAllThreadNum++;
        if(mThread != null){
            setFlag(false);
            init();
            setFlag(true);
        }
        if(mThread == null){
            init();
            setFlag(true);

            mThread = new TimeThread();
            mThread.start();
        }
    }

    public void setFlag(boolean mFlag) {
        this.mFlag = mFlag;
    }

    public void stopCount(){
        setFlag(false);
        mThread.deleteThread();
        if(mThread != null){
            mThread.interrupt();
            mThread = null;
        }

    }
    public class TimeThread extends Thread{
        public int mThreadNum = 0;
        public TimeThread(){
//            mThreadNum++;
            Log.d("Current mAllThreadNum ", " "+mAllThreadNum );
            Log.d("Current mThreadNum ", " "+mThreadNum );
        }
        public void deleteThread(){
            mThreadNum--;
        }
        @Override
        public void run() {
            super.run();
            while (mFlag && mThreadNum == mAllThreadNum) {
                Message message = Message.obtain();
                Bundle bundle = new Bundle();
                bundle.putBoolean(TIME_ADD, true);
                message.setData(bundle);
                if(mThreadNum == mAllThreadNum){
                    mHandler.sendMessage(message);
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }

            }
        }
    }

}
