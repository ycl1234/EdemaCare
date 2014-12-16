package com.timeszoro.edemacare;



import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import com.example.edemacare.R;


public class BledeviceActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bledevice);
		
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_blelist, new BleListFragment()).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_scanbtn, new ScanProFragment()).commit();
		
	}
//	@Override
//	protected Fragment createFragment() {
//		return new ScanProFragment();
//	}
}
