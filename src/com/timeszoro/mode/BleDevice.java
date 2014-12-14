package com.timeszoro.mode;

import java.util.UUID;

public class BleDevice {
	private UUID mID;
	private String mName;
	private boolean mConnectStatus;
	
	
	
	
	public UUID getID() {
		return mID;
	}
	public void setID(UUID iD) {
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
}
