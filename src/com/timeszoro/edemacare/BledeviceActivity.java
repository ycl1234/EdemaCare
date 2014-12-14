package com.timeszoro.edemacare;


import android.support.v4.app.Fragment;


public class BledeviceActivity extends BaseFraActivity {

	@Override
	protected Fragment createFragmetn() {
		return new BleListFragment();
	}
}
