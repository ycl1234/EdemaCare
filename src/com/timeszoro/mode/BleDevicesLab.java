package com.timeszoro.mode;

import java.util.ArrayList;
import java.util.UUID;

import android.content.Context;

public class BleDevicesLab {
	private static BleDevicesLab mBleLab;
	private ArrayList<BleDevice> mDevices ;
	private Context mAppContext;
	
	public BleDevicesLab(Context context) {
		mAppContext = context;
		mDevices = new ArrayList<BleDevice>();
		
		// get the ble devices by scan , and add to the mDevices Array
		for(int i = 0; i < 50;i++){
			BleDevice bleDevice = new BleDevice();
			bleDevice.setName("ble device #" + i);
			bleDevice.setID(UUID.randomUUID());
			mDevices.add(bleDevice);
		}
		
	}
	
	public static BleDevicesLab getBleLab(Context context){
		if(mBleLab == null){
			mBleLab = (BleDevicesLab) new BleDevicesLab(context.getApplicationContext());
		}
		return mBleLab;
	}
	
	public BleDevice getBleDevice(){
		return mDevices.get(0);
	}
	
	public  ArrayList<BleDevice> getBleList(){
		return mDevices;
	}
}
