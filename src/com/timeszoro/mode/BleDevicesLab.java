package com.timeszoro.mode;

import java.util.ArrayList;
import java.util.UUID;

import android.bluetooth.BluetoothDevice;
import android.content.Context;
/**
 * Created by Timeszoro on 2014/12/16.
 */
public class BleDevicesLab {
	private static BleDevicesLab mBleLab;
	private ArrayList<BluetoothDevice> mDevices ;
	private Context mAppContext;
	
	public BleDevicesLab(Context context) {
		mAppContext = context;
		mDevices = new ArrayList<BluetoothDevice>();
		
		// get the ble devices by scan , and add to the mDevices Array
//		for(int i = 0; i < 50;i++){
//			BleDevice bleDevice = new BleDevice();
//			bleDevice.setName("ble device #" + i);
//			bleDevice.setID("ffee- 3f41");
//			mDevices.add(bleDevice);
//		}
		
	}
	
	public static BleDevicesLab getBleLab(Context context){
		if(mBleLab == null){
			mBleLab = (BleDevicesLab) new BleDevicesLab(context.getApplicationContext());
		}
		return mBleLab;
	}
	
	public BluetoothDevice getBleDevice(){
		return mDevices.get(0);
	}
	
	public  ArrayList<BluetoothDevice> getBleList(){
		return mDevices;
	}
}
