/**
 * Created by Timeszoro on 2014/12/16.
 */
package com.timeszoro.fragment;


import java.util.ArrayList;
import java.util.List;

import android.content.Intent;
import com.example.edemacare.R;
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
	private ArrayList<BleDevice> mDevices;
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
		BleDevice bleDevice = ((BleDevicesAdapter)getListAdapter()).getItem(position);
		Log.d(TAG, bleDevice.getName()+" is clicked");
		Intent intent = new Intent(getActivity(),EdemaActivity.class);
		startActivity(intent);
		//需要添加停止扫描的代码

	}
	
	public class BleDevicesAdapter extends ArrayAdapter<BleDevice>{

		public BleDevicesAdapter(List<BleDevice> bleDevices) {
			super(getActivity(), 0, bleDevices);
		}
		
		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
//			return super.getView(position, convertView, parent);
			if(convertView == null){
				convertView = getActivity().getLayoutInflater().inflate(R.layout.ble_list_item, null);	
			}
			
			BleDevice bleDevice = getItem(position);
			
			
			
			ImageView bleImg = (ImageView)convertView.findViewById(R.id.bleitem_img);
			bleImg.setBackgroundResource(R.drawable.img_bledevice);
			
			TextView bleName = (TextView)convertView.findViewById(R.id.bleitem_name);
			bleName.setText(bleDevice.getName());
			
			TextView bleUUID = (TextView)convertView.findViewById(R.id.bleitem_uuid);
			bleUUID.setText(bleDevice.getID().toString());
			
			return convertView;
		}
		
	}
	public static BleDevicesAdapter getdataAdapter(){
		return  mBleAdapter;
	}
}