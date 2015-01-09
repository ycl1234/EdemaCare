package com.timeszoro.edemacare;

import android.app.Activity;
import android.bluetooth.BluetoothGatt;
import android.bluetooth.BluetoothGattCharacteristic;
import android.bluetooth.BluetoothGattService;
import android.content.*;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;
import antistatic.spinnerwheel.AbstractWheel;
import antistatic.spinnerwheel.OnWheelScrollListener;
import antistatic.spinnerwheel.adapters.NumericWheelAdapter;
import com.example.edemacare.R;
import com.github.mikephil.charting.charts.*;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.interfaces.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.*;
import com.github.mikephil.charting.utils.XLabels.XLabelPosition;
import com.timeszoro.edemadata.EdemaData;
import com.timeszoro.fragment.TimeCountFragment;

import java.util.UUID;


/**
 * Created by Administrator on 2015/1/5.
 */
public class EdemaActivity extends Activity implements OnChartValueSelectedListener{
    private static final int GATT_TIMEOUT = 200; // milliseconds
    private final  int CUR_FRE = 5;
    private final String TAG = "Edema data";
    private final String CONNECT_STATUS = "Connect status";
    private int mCurFre = CUR_FRE;
    private TimeCountFragment mTimerFragment;

    public static final String EXTRAS_DEVICE_NAME = "DEVICE_NAME";
    public static final String EXTRAS_DEVICE_ADDRESS = "DEVICE_ADDRESS";

    //information of the data chart
    LineChart mLineChart ;

    //variate of the service
    private BluetoothLeService mBleService;
    private BluetoothGatt mBluetoothGatt;

    //variate of the device to be connected
    private String mDeviceName;
    private String mDeviceAddress;
    //status of the Gatt
    private boolean mConnected;
    private boolean mServiceReady = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edemashow);
        //init the variate
        final Intent intent = getIntent();
        mDeviceName = intent.getStringExtra(EXTRAS_DEVICE_NAME);
        mDeviceAddress = intent.getStringExtra(EXTRAS_DEVICE_ADDRESS);

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
        XLabels xl = mLineChart.getXLabels();
        xl.setPosition(XLabelPosition.BOTTOM);
        xl.setCenterXLabelText(true);
         //add the data of the chart
        int num = 20;
        EdemaData edemaData = EdemaData.getEdemaDataHandle();
        edemaData.setmDataNum(num);
        for(int i = 0; i < 3 * num; i++){
            edemaData.addXVals(String.valueOf(i));
        }
        for (int i = 0;i < 3 * num;i++){
            edemaData.addImpVal(i);
            edemaData.addPhaVal(i * 2);
        }
        mLineChart.setData(edemaData.getLineData());
        mLineChart.invalidate();
        mLineChart.setOnChartValueSelectedListener(this);
        //begin bind the ble service

        Intent gattServiceIntent = new Intent(this, BluetoothLeService.class);
        bindService(gattServiceIntent, mServiceConnection, BIND_AUTO_CREATE);



    }
    @Override
    protected void onResume() {
        super.onResume();
        registerReceiver(mGattUpdateReceiver, makeGattUpdateIntentFilter());
        if (mBleService != null) {
            final boolean result = mBleService.connect(mDeviceAddress);
            Log.d(TAG, "Connect request result=" + result);
        }
    }
    @Override
    protected void onPause() {
        super.onPause();
        unregisterReceiver(mGattUpdateReceiver);
    }
    @Override
    protected void onDestroy() {
        super.onDestroy();
        unbindService(mServiceConnection);
        mBleService = null;
    }


    /** Functions fo the BluetoothService connection
     *  function #01 : the call back function of the bind service
     */

    private final ServiceConnection mServiceConnection = new ServiceConnection() {
        @Override
        public void onServiceConnected(ComponentName componentName, IBinder service) {
            mBleService = ((BluetoothLeService.LocalBinder) service).getService();
            mBluetoothGatt = mBleService.getmBluetoothGatt();
            if (!mBleService.initialize()) {
                Log.e(TAG, "Unable to initialize Bluetooth");
                finish();
            }
            // Automatically connects to the device upon successful start-up initialization.
            mBleService.connect(mDeviceAddress);
        }

        @Override
        public void onServiceDisconnected(ComponentName componentName) {
            mBleService = null;
        }
    };

    /** Functions of the chart view
     *  function #01 : clear the chart view
     *  function #02 : listener when the data is selected     */
    private void clearUI(){

    }
    @Override
    public void onValueSelected(Entry e, int dataSetIndex) {
        Log.i("VAL SELECTED",
                "Value: " + e.getVal() + ", xIndex: " + e.getXIndex()
                        + ", DataSet index: " + dataSetIndex);

    }
    @Override
    public void onNothingSelected() {

    }





    /**Functions for the connection with the GATT
     * function #01 : broadcast receiver for the broadcast send from BluetoothGattCallback which show the result of Gatt connection and so on
     * function #02 : the action for the broadcast receiver
     */
    private final BroadcastReceiver mGattUpdateReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            final String action = intent.getAction();
            int status = intent.getIntExtra(BluetoothLeService.EXTRA_STATUS, BluetoothGatt.GATT_SUCCESS);
            if (BluetoothLeService.ACTION_GATT_CONNECTED.equals(action)) {
                mConnected = true;
                Log.d(CONNECT_STATUS,getString(R.string.connected));
            } else if (BluetoothLeService.ACTION_GATT_DISCONNECTED.equals(action)) {
                mConnected = false;
                Log.d(CONNECT_STATUS,getString(R.string.disconnected));
                clearUI();
            } else if (BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED.equals(action)) {
                if(status == BluetoothGatt.GATT_SUCCESS){
                    writetoCharacter();
                }

            } else if (BluetoothLeService.ACTION_DATA_WRITE.equals(action)) {
                String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                enableDataTran();
            }
            else if(BluetoothLeService.ACTION_DATA_READ.equals(action)){

            }
            else if(BluetoothLeService.ACTION_DATA_NOTIFY.equals(action)){
                // Notification
                final byte  [] value = intent.getByteArrayExtra(BluetoothLeService.EXTRA_DATA);
                String uuidStr = intent.getStringExtra(BluetoothLeService.EXTRA_UUID);
                Log.d(TAG,"onCharacteristChanged " + uuidStr);

                String shortUUIDString = GattInfo.toShortUuidStr(UUID.fromString(uuidStr));
                /***********************��ʾX�������******************************/
                if(shortUUIDString.equals("ffa3")){

                    impedanceIndex = impedanceIndex % 13;
                    char impedancetmp = (char) (value[0] & 0xff);
                    if(!bAcc_x)
                    {
                        //��ʱ���ո�λ
                        mAcc_X = impedancetmp << 8;
                        bAcc_x = true;
                    }
                    else{
                        //�ϲ�
                        mAcc_X = mAcc_X |(impedancetmp & 0xff);
                        bAcc_x = false;
                        //�õ���ʵ����ֵ
                        mImp = mAcc_X;
                        mPha = 255;
                        mFre = frequences[impedanceIndex];
                        bioService.insertBioData(mImp, mPha, mFre);
                        //��ͼ����ʾ

                        runOnUiThread(new  Runnable() {
                            public void run() {
                                double impedancetmp = (value[0] & 0xff);
                                updateChart(((double)mAcc_X/10.0), MathHelper.NULL_VALUE, impedanceIndex);


                            }
                        });
                        impedanceIndex ++;
                    }


                }
                /***********************��ʾY�������******************************/
                else if(shortUUIDString.equals("ffa4")){
                    //phaceIndex = 4;
                    phaceIndex = phaceIndex % 13;
                    char phatmp = (char) (value[0] & 0xff) ;
                    if(!bAcc_Y){
                        mAcc_Y = phatmp << 8;
                        bAcc_Y = true;
                    }
                    else{
                        mAcc_Y = mAcc_Y | (phatmp & 0xff);
                        bAcc_Y = false;
                        mImp = 255;
                        mPha = mAcc_Y;
                        mFre = frequences[phaceIndex];
                        bioService.insertBioData(mImp, mPha, mFre);


                        runOnUiThread(new  Runnable() {
                            public void run() {

                                double phatmp = (value[0] & 0xff);
                                updateChart(MathHelper.NULL_VALUE,(double)mAcc_Y/10.0,phaceIndex);

                            }
                        });
                        phaceIndex ++;
                    }

                }
                else if(shortUUIDString.equals("ffa5")){
                    runOnUiThread(new  Runnable() {
                        public void run() {
                            //int currentFrequence = 0 ;
                            if (value[0]%10 != 0) {
                                currentFrequence = value[0] + 256;
                            }
                            else {
                                currentFrequence = value[0];
                            }
                            //mTextView3.setText(String.valueOf(currentFrequence));
                        }
                    });
                }
                else{
                    //����ȷ��UUID
                    Log.d(TAG, "Error uuid info");
                }

            }
            }

    };


    private static IntentFilter makeGattUpdateIntentFilter() {
        final IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_CONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_DISCONNECTED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_NOTIFY);
        intentFilter.addAction(BluetoothLeService.ACTION_GATT_SERVICES_DISCOVERED);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_WRITE);
        intentFilter.addAction(BluetoothLeService.ACTION_DATA_READ);
        return intentFilter;
    }

    private void writetoCharacter(){

            BluetoothGattService service = mBluetoothGatt.getService(UUID.fromString("0000ffa1-0000-1000-8000-00805f9b34fb");
            BluetoothGattCharacteristic bluetoothGattCharacteristic = service.getCharacteristic(UUID.fromString("0000ffa1-0000-1000-8000-00805f9b34fb"));
            if (mBleService.writeCharacteristic(bluetoothGattCharacteristic, true)) {
                Log.i(TAG, "write success");
            }
            mBleService.waitIdle(GATT_TIMEOUT);
    }

    private void enableDataTran(){
            enableNotification(GattInfo.shortUuidToLong("ffa0"), GattInfo.shortUuidToLong("ffa3"), true);
            enableNotification(GattInfo.shortUuidToLong("ffa0"), GattInfo.shortUuidToLong("ffa4"), true);
            enableNotification(GattInfo.shortUuidToLong("ffa0"), GattInfo.shortUuidToLong("ffa5"), true);


    }
    private void enableNotification(UUID serviceUuid, UUID charaUuid,boolean enable){
        BluetoothGattService serv = mBluetoothGatt.getService(serviceUuid);
        BluetoothGattCharacteristic characteristic = serv.getCharacteristic(charaUuid);
        mBleService.setCharacteristicNotification(characteristic, true);
        mBleService.waitIdle(GATT_TIMEOUT);
    }


}
