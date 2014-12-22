package com.timeszoro.edemacare;



import java.lang.reflect.Field;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.view.Menu;
import android.view.ViewConfiguration;

import com.example.edemacare.R;


public class BledeviceActivity extends FragmentActivity {

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_bledevice);
		forceShowOverflowMenu();
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_blelist, new BleListFragment()).commit();
		getSupportFragmentManager().beginTransaction().add(R.id.fragment_scanbtn, new ScanProFragment()).commit();
		
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
    
    
//	@Override
//	protected Fragment createFragment() {
//		return new ScanProFragment();
//	}
}
