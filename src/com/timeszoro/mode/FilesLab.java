package com.timeszoro.mode;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import java.io.File;
import java.util.ArrayList;

/**
 * Created by Administrator on 2015/1/15.
 */
public class FilesLab {
    private static FilesLab mFileLab;
    private ArrayList<File> mFiles ;
    private Context mAppContext;

    public FilesLab(Context context) {
        mAppContext = context;
        mFiles = new ArrayList<File>();

        // get the ble devices by scan , and add to the mDevices Array
//		for(int i = 0; i < 20;i++){
//			File file = new File("File Name # " + i);
//
//			mFiles.add(file);
//		}

    }

    public static FilesLab getFileLab(Context context){
        if(mFileLab == null){
            mFileLab = (FilesLab) new FilesLab(context);
        }
        return mFileLab;
    }



    public  ArrayList<File> getFileList(){
        
        return mFiles;
    }
}
