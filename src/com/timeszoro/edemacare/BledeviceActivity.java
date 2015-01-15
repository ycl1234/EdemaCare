/**
 * Created by Timeszoro on 2014/12/16.
 */
package com.timeszoro.edemacare;



import java.lang.reflect.Field;
import java.util.ArrayList;


import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.ViewConfiguration;

import android.widget.ArrayAdapter;
import android.widget.Toast;
import com.example.edemacare.R;
import com.timeszoro.fragment.BleListFragment;
import com.timeszoro.fragment.ScanProFragment;
import com.timeszoro.mode.BleDevice;
import com.timeszoro.mode.BleDevicesLab;


public class BledeviceActivity extends FragmentActivity {

	private static final String TAG = "BLE_ACTIONBAR";
	private static final int REQUEST_ENABLE_BT = 1;
	private static BluetoothManager mBleManager = null;
	private static BluetoothAdapter mBleAdapter = null;

	private ScanProFragment mScanProFragment;
	private BleListFragment mBleListFragment;
	private boolean mScanning;//
	private android.os.Handler mHandler;
	private int SCAN_PERIOD ;
	private BleDevicesLab mBleDeviceLab;
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bledevice);
		forceShowOverflowMenu();
		mScanProFragment = new ScanProFragment();
		mBleListFragment = new BleListFragment();
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_blelist, mBleListFragment).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_scanbtn, mScanProFragment).commit();

		//init
		mHandler = new android.os.Handler();
		mScanning = true;
		int  duration = Integer.valueOf(getString(R.string.scan_interval));
		SCAN_PERIOD = duration;
		mBleDeviceLab = BleDevicesLab.getBleLab(this);
		//register
		registScanBro();


		//check if the device support the ble
		if (!getPackageManager().hasSystemFeature(PackageManager.FEATURE_BLUETOOTH_LE)) {
			Toast.makeText(this, R.string.ble_not_supported, Toast.LENGTH_SHORT).show();
			finish();
		}

		mBleManager = (BluetoothManager)getSystemService(Context.BLUETOOTH_SERVICE);
		mBleAdapter = mBleManager.getAdapter();
		// Checks if Bluetooth is supported on the device.
		if (mBleAdapter == null) {
			Toast.makeText(this, R.string.error_bluetooth_not_supported, Toast.LENGTH_SHORT).show();
			finish();
			return;
		}

		
	}

	@Override
	protected void onResume() {
		super.onResume();
		//enable the ble
		if (!mBleAdapter.isEnabled()) {
			if (!mBleAdapter.isEnabled()) {
				Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
				startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
			}
		}
		//enable the scan button
		mScanProFragment.getmScanImg().setEnabled(true);

	}

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		getMenuInflater().inflate(R.menu.bledevice, menu);
		return true;
	}
	/**
	 * show the overflow button
	 */
    private void forceShowOverflowMenu() {  
        try {  
            ViewConfiguration config = ViewConfiguration.get(this);  
            Field menuKeyField = ViewConfiguration.class  
                    .getDeclaredField("sHasPermanentMenuKey");  
            if (menuKeyField != null) {  
                menuKeyField.setAccessible(true);  
                menuKeyField.setBoolean(config, false);  
            }  
        } catch (Exception e) {  
            e.printStackTrace();  
        }  
    }  
    
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
    	switch (item.getItemId()) {
		case R.id.action_upload:
			Log.d(TAG, "upload button is clicked");
			break;
//		case R.id.action_setting:
//			Log.d(TAG, "setting button is clicked");
		default:
			break;
		}
    	return super.onOptionsItemSelected(item);
    }

	@Override
	protected void onActivityResult(int requestCode, int resultCode, Intent data) {
		if(resultCode == REQUEST_ENABLE_BT && resultCode == RESULT_CANCELED ){
			//if do not enable the ble
			finish();
			return;
		}
		super.onActivityResult(requestCode, resultCode, data);
	}


	public void scanLeDevice(final boolean enable) {
		//clean the ble device list
		BleDevicesLab.getBleLab(this).getBleList().clear();
		((BleListFragment.BleDevicesAdapter)BleListFragment.getdataAdapter()).notifyDataSetChanged();
		//begin scan
		if (enable) {
			// Stops scanning after a pre-defined scan period.
			mHandler.postDelayed(new Runnable() {
				@Override
				public void run() {
					mScanning = false;
					mBleAdapter.stopLeScan(mLeScanCallback);
					invalidateOptionsMenu();
				}
			}, SCAN_PERIOD);

			mScanning = true;
			mBleAdapter.startLeScan(mLeScanCallback);
		} else {
			mScanning = false;
			mBleAdapter.stopLeScan(mLeScanCallback);
		}
		invalidateOptionsMenu();
	}

	private BluetoothAdapter.LeScanCallback mLeScanCallback =
			new BluetoothAdapter.LeScanCallback() {

				@Override
				public void onLeScan(final BluetoothDevice device, int rssi, byte[] scanRecord) {
					runOnUiThread(new Runnable() {
						@Override
						public void run() {
							if(device != null){
								ArrayList<BluetoothDevice> bleList = mBleDeviceLab.getBleList();
								if(!bleList.contains(device)){
									bleList.add(device);
									BleListFragment.getdataAdapter().notifyDataSetChanged();
									Log.d(TAG,"add the device success");
								}


							}
;
						}
					});
				}
	};

	//register the scan broadcast
	private void registScanBro(){
		IntentFilter intentFilter = new IntentFilter(getString(R.string.ble_scan_broadcast));
		registerReceiver(scanReceiver,intentFilter);
	}

	//unregister the scan broadcast
	private void unregisterScanBro(){

	}

	private BroadcastReceiver scanReceiver = new BroadcastReceiver() {
		@Override
		public void onReceive(Context context, Intent intent) {
			String action = intent.getAction();
			if(action.equals(getString(R.string.ble_scan_broadcast))){
				//
				Log.d(TAG,"did the scan process");
				scanLeDevice(true);
			}
		}
	};

	public void setScanning(boolean scan) {
		this.mScanning = scan;
	}
	public boolean getScanning(){
		return mScanning;
	}
}
