/**
 * Created by Timeszoro on 2014/12/16.
 */
package com.timeszoro.fragment;


import java.util.ArrayList;
import java.util.List;

import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import com.example.edemacare.R;
import com.timeszoro.edemacare.BledeviceActivity;
import com.timeszoro.edemacare.EdemaActivity;
import com.timeszoro.mode.BleDevice;
import com.timeszoro.mode.BleDevicesLab;

import android.os.Bundle;
import android.support.v4.app.ListFragment;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

public class BleListFragment extends ListFragment {
	private static final String TAG  = "Ble List Fragment";
	private ArrayList<BluetoothDevice> mDevices;
	private static BleDevicesAdapter mBleAdapter;
	
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		
		mDevices = BleDevicesLab.getBleLab(getActivity()).getBleList();
		mBleAdapter = new BleDevicesAdapter(mDevices);
		this.setListAdapter(mBleAdapter);
		
	}
	
	@Override
	public void onListItemClick(ListView l, View v, int position, long id) {
		//be ready to connect to the device
		BluetoothDevice device = ((BleDevicesAdapter)getListAdapter()).getItem(position);
		Log.d(TAG, device.getName()+" is clicked");
		if (device == null) return;
		//jump to the edema care view
		final Intent intent = new Intent(getActivity(), EdemaActivity.class);
		intent.putExtra(EdemaActivity.EXTRAS_DEVICE_NAME, device.getName());
		intent.putExtra(EdemaActivity.EXTRAS_DEVICE_ADDRESS, device.getAddress());
		startActivity(intent);
		//stop scan the device
		BledeviceActivity bledeviceActivity = (BledeviceActivity)getActivity();
		if(bledeviceActivity.getScanning()){
			bledeviceActivity.setScanning(false);
			bledeviceActivity.scanLeDevice(false);
		}

	}
	
	public class BleDevicesAdapter extends ArrayAdapter<BluetoothDevice>{

		public BleDevicesAdapter(List<BluetoothDevice> bleDevices) {
			super(getActivity(), 0, bleDevices);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			return super.getView(position, convertView, parent);
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.ble_list_item, null);	
			}
			
//			BleDevice bleDevice = getItem(position);
			BluetoothDevice bluetoothDevice = getItem(position);
			
			
			ImageView bleImg = (ImageView)convertView.findViewById(R.id.bleitem_img);
			bleImg.setBackgroundResource(R.drawable.img_bledevice);
			
			TextView bleName = (TextView)convertView.findViewById(R.id.bleitem_name);
			bleName.setText(bluetoothDevice.getName());
			
			TextView bleUUID = (TextView)convertView.findViewById(R.id.bleitem_uuid);
			bleUUID.setText(bluetoothDevice.getAddress());
			
			return convertView;
		}
		
	}
	public static BleDevicesAdapter getdataAdapter(){
		return  mBleAdapter;
	}
}
