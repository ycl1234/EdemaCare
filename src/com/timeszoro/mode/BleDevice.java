package com.timeszoro.mode;
/**
 * Created by Timeszoro on 2014/12/16.
 */


public class BleDevice {
	private String mID;
	private String mName;
	private boolean mConnectStatus;
	
	
	
	
	public String getID() {
		return mID;
	}
	public void setID(String iD) {
		mID = iD;
	}
	public String getName() {
		return mName;
	}
	public void setName(String name) {
		mName = name;
	}
	public boolean isConnectStatus() {
		return mConnectStatus;
	}
	public void setConnectStatus(boolean connectStatus) {
		mConnectStatus = connectStatus;
	}
	
	public BleDevice(){
		
	}


	@Override
	public boolean equals(Object o) {

		if(this == o) return true;
		if (o == null)
			return false;
		if(!(o instanceof  BleDevice)){
			return false;
		}
		final  BleDevice tmp = (BleDevice)o;
		return tmp.getID().equals(this.getID());


	}

}
