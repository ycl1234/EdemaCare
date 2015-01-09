/**
 * Created by Timeszoro on 2015/01/07.
 */

package com.timeszoro.edemacare;

import android.app.Service;
import android.bluetooth.*;
import android.content.Context;
import android.content.Intent;
import android.os.Binder;
import android.os.IBinder;
import android.util.Log;

import java.util.UUID;

/**
 * Service for managing connection and data communication with a GATT server hosted on a
 * given Bluetooth LE device.
 */
public class BluetoothLeService extends Service {
    private static final String TAG = "BluetoothLeService";


    public final static String ACTION_GATT_CONNECTED = "times.ble.common.ACTION_GATT_CONNECTED";
    public final static String ACTION_GATT_DISCONNECTED = "times.ble.common.ACTION_GATT_DISCONNECTED";
    public final static String ACTION_GATT_SERVICES_DISCOVERED = "times.ble.common.ACTION_SERVICES_DISCOVERED ";

    public final static String ACTION_DATA_READ = "times.ble.common.ACTION_SERVICES_DATA_READ";
    public final static String ACTION_DATA_WRITE = "times.ble.common.ACTION_SERVICES_DATA_WRITE";
    public final static String ACTION_DATA_NOTIFY = "times.ble.common.ACTION_SERVICES_DATA_NOTIFY";
    public final static String EXTRA_DATA = "times.ble.common.EXTRA_DATA";
    public final static String EXTRA_UUID = "times.ble.common.EXTRA_UUID";
    public final static String EXTRA_STATUS = "times.ble.common.EXTRA_STATUS";
    public final static String EXTRA_ADDRESS = "times.ble.common.EXTRA_ADDRESS";


    private BluetoothGatt mBluetoothGatt = null;
    private BluetoothManager mBluetoothManager = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private String mBluetoothDeviceAddress;
    private int mConnectionState = STATE_DISCONNECTED;

    private static final int STATE_DISCONNECTED = 0;
    private static final int STATE_CONNECTING = 1;
    private static final int STATE_CONNECTED = 2;

    private boolean mBusy = false;
    //Blinder
    private final IBinder mBinder = new LocalBinder();
    private static String mBluetoothDeviceAdress ;


    /**functions for the GATT connection
     * function #01 : connect to the GATT with the ble adress
     * function #02 : the call back funciton for the connect() fucntion
     */
    //connection the GATT, called when service is binded
    public boolean connect(final String adress){
        if (mBluetoothAdapter == null || adress == null) {
            Log.w(TAG, "BluetoothAdapter not initialized or unspecified address.");
            return false;
        }
        // Previously connected device.  Try to reconnect.
        if (mBluetoothDeviceAddress != null && adress.equals(mBluetoothDeviceAddress)
                && mBluetoothGatt != null) {
            Log.d(TAG, "Trying to use an existing mBluetoothGatt for connection.");
            if (mBluetoothGatt.connect()) {
                mConnectionState = STATE_CONNECTING;
                return true;
            } else {
                return false;
            }
        }
        final BluetoothDevice device = mBluetoothAdapter.getRemoteDevice(adress);
        if (device == null) {
            Log.w(TAG, "Device not found.  Unable to connect.");
            return false;
        }
        // We want to directly connect to the device, so we are setting the autoConnect
        // parameter to false.
        mBluetoothGatt = device.connectGatt(this, false, mGattCallback);
        Log.d(TAG, "Trying to create a new connection.");
        mBluetoothDeviceAddress = adress;
        mConnectionState = STATE_CONNECTING;
        return true;
    }
    // the call back function
    private BluetoothGattCallback mGattCallback = new BluetoothGattCallback() {

        @Override
        public void onConnectionStateChange(BluetoothGatt gatt, int status,
                                            int newState) {
            if (mBluetoothGatt == null) {
                Log.e(TAG, "mBluetoothGatt not created");
                return;
            }
            BluetoothDevice device = gatt.getDevice();
            String address = device.getAddress();
            Log.d(TAG, "onConnectionStateChange (" + address +") "+ newState +"status: "+status);
            switch (newState) {
                case BluetoothProfile.STATE_CONNECTED:
                    broadcastUpdate(ACTION_GATT_CONNECTED, address, status);
                    break;
                case BluetoothProfile.STATE_DISCONNECTED:
                    broadcastUpdate(ACTION_GATT_DISCONNECTED, address, status);

                default:
                    Log.e(TAG, "new state not processed: " + newState);
                    break;
            }
        }

        @Override
        public void onServicesDiscovered(BluetoothGatt gatt, int status) {
            BluetoothDevice device = gatt.getDevice();
            broadcastUpdate(ACTION_GATT_SERVICES_DISCOVERED, device.getAddress(), status);
        }

        @Override
        public void onDescriptorRead(BluetoothGatt gatt,
                                     BluetoothGattDescriptor descriptor, int status) {
            mBusy = false;
            super.onDescriptorRead(gatt, descriptor, status);
        }

        @Override
        public void onDescriptorWrite(BluetoothGatt gatt,
                                      BluetoothGattDescriptor descriptor, int status) {
            mBusy = false;
            super.onDescriptorWrite(gatt,descriptor,status);
        }

        @Override
        public void onCharacteristicRead(BluetoothGatt gatt,
                                         BluetoothGattCharacteristic characteristic, int status) {
            broadcastUpdate(ACTION_DATA_READ, characteristic, status);
        }

        @Override
        public void onCharacteristicWrite(BluetoothGatt gatt,
                                          BluetoothGattCharacteristic characteristic, int status) {
            broadcastUpdate(ACTION_DATA_WRITE, characteristic, status);
        }

        @Override
        public void onCharacteristicChanged(BluetoothGatt gatt,
                                            BluetoothGattCharacteristic characteristic) {
            broadcastUpdate(ACTION_DATA_NOTIFY, characteristic, BluetoothGatt.GATT_SUCCESS);
        }

        public void onReadRemoteRssi(BluetoothGatt gatt, int rssi, int status) {};

    };
    //dis connection of the Gatt
    public void disconnect() {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.disconnect();
    }

    /**common functions
     * function #01 : the broadcastUpate function ->called when the status of the GATT,or read happened
     * function #02 : same with the #01 , which sende the character not the adress
     */
    private void broadcastUpdate(final String action,final String address, final int status){
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_ADDRESS, address);
        intent.putExtra(EXTRA_STATUS, status);
        sendBroadcast(intent);
        mBusy = false;
    }
    private void broadcastUpdate(final String action,final BluetoothGattCharacteristic characteristic, final int status){
        final Intent intent = new Intent(action);
        intent.putExtra(EXTRA_UUID, characteristic.getUuid().toString());
        intent.putExtra(EXTRA_DATA, characteristic.getValue());
        intent.putExtra(EXTRA_STATUS, status);
        sendBroadcast(intent);
        mBusy = false;
    }

    /** Functions for the Local binder which will be called in the EdemaActivity (bind the service)
     *  function #01 : claim the LocalBinder class
     *  function #02 : override the onBind function
     *  function #03 : override the
     */
    public class LocalBinder extends Binder {
        BluetoothLeService getService() {
            return BluetoothLeService.this;
        }
    }
    @Override
    public IBinder onBind(Intent intent) {
        return mBinder;
    }
    @Override
    public boolean onUnbind(Intent intent) {
        // After using a given device, you should make sure that BluetoothGatt.close() is called
        // such that resources are cleaned up properly.  In this particular example, close() is
        // invoked when the UI is disconnected from the Service.
        close();
        return super.onUnbind(intent);
    }

    /** Functions for the gatt use
     *  function #01 : after using a given BLE device, ensure resources are released properly.
     *  function #02 : init function for the gatt ,which is called in the bind callback in EdemaActivity
     */

    public void close(){
        if (mBluetoothGatt != null) {
            Log.i(TAG, "close");
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }
    public boolean initialize(){
        Log.d(TAG, "initialize");
        if (mBluetoothManager == null) {
            mBluetoothManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
            if (mBluetoothManager == null) {
                Log.e(TAG, "Unable to init bluetoothmanager");
                return false;
            }
        }
        mBluetoothAdapter = mBluetoothManager.getAdapter();
        if (mBluetoothAdapter == null) {
            Log.e(TAG, "unable to init bluetooth adapter");
            return false;
        }
        return true;
    }

    /**Functions for the data read and write
     * function #01 : set the character which will enable the notification
     * function #02 : read the character value
     * function #03 : write the character value
     * fucntion #04 ：
     */
    public void setCharacteristicNotification(BluetoothGattCharacteristic characteristic,
                                              boolean enabled) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.setCharacteristicNotification(characteristic, enabled);

        // This is specific to Heart Rate Measurement.
        final  UUID UUID_HEART_RATE_MEASUREMENT =
                UUID.fromString(SampleGattAttributes.HEART_RATE_MEASUREMENT);

        if (UUID_HEART_RATE_MEASUREMENT.equals(characteristic.getUuid())) {
            BluetoothGattDescriptor descriptor = characteristic.getDescriptor(
                    UUID.fromString(SampleGattAttributes.CLIENT_CHARACTERISTIC_CONFIG));
            descriptor.setValue(BluetoothGattDescriptor.ENABLE_NOTIFICATION_VALUE);
            mBluetoothGatt.writeDescriptor(descriptor);
        }

    }
    public void readCharacteristic(BluetoothGattCharacteristic characteristic) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return;
        }
        mBluetoothGatt.readCharacteristic(characteristic);
    }
    public boolean writeCharacteristic(BluetoothGattCharacteristic characteristic, boolean b) {
        if (mBluetoothAdapter == null || mBluetoothGatt == null) {
            Log.w(TAG, "BluetoothAdapter not initialized");
            return false;
        }
        byte[] val = new byte[1];
        val[0] = (byte) (b ? 1 : 0);
        characteristic.setValue(val);
        mBusy = true;
        return mBluetoothGatt.writeCharacteristic(characteristic);
    }
    public boolean waitIdle(int i) {
        i /= 10;
        while (--i > 0) {
            if (mBusy)
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            else
                break;
        }
        return  i > 0;
    }

    @Override
    public void onDestroy() {
        // TODO Auto-generated method stub
        super.onDestroy();
        Log.i(TAG, "onDestory() called");
        if (mBluetoothGatt != null) {
            mBluetoothGatt.close();
            mBluetoothGatt = null;
        }
    }

    public BluetoothGatt getmBluetoothGatt() {
        return mBluetoothGatt;
    }
}