package com.timeszoro.edemadata;

import android.content.Intent;
import android.graphics.Color;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/1/8.
 */


public class EdemaData {
    private static final int NUM_OF_DATA = 20;//set the number of the edema data
    private LineData mLineData;//
    private static EdemaData mEdemaData;

    private int mDataNum = 0;
    ArrayList<String> mXVals ;
    ArrayList<Entry> mYImpeVals ;//Impedance values
    ArrayList<Entry> mYPhaVals;//Phase values
    private static  final  float SCALE = 10.0f;



    //only one object is created
    public static EdemaData getEdemaDataHandle(){
        mEdemaData =  new EdemaData();
        return mEdemaData;
    }

    //the number of data
    public void setmDataNum(int num) {
        this.mDataNum = num;
    }

    public void addXVals(String newX){
        if(mXVals == null){
            mXVals = new ArrayList<String>();
        }
        // move forwad for each entry
        if(mXVals.size() >= mDataNum){
            for(int i = 0; i < mXVals.size() - 1;i++){
                mXVals.set(i,mXVals.get(i + 1));
            }
            mXVals.set(mDataNum - 1 ,newX);
        }
        else{
            mXVals.add(newX);
        }

    }

    public void addImpVal(double impVal){
        if(mYImpeVals == null){
            mYImpeVals = new ArrayList<Entry>();
        }
        if(mYImpeVals.size() >= mDataNum){
            for(int i = 0; i < mYImpeVals.size() - 1;i++){
                mYImpeVals.set(i,new Entry(mYImpeVals.get(i + 1).getVal(),i));
            }
            mYImpeVals.set(mDataNum - 1, new Entry((float) impVal/SCALE, mDataNum - 1));
        }
        else{
            mYImpeVals.add(new Entry((float)impVal/SCALE,mYImpeVals.size()));
        }

    }

    public void addPhaVal(double impVal){
        if(mYPhaVals == null){
            mYPhaVals = new ArrayList<Entry>();
        }
        if(mYPhaVals.size() >= mDataNum){
            for(int i = 0; i < mYPhaVals.size() - 1;i++){
                mYPhaVals.set(i,new Entry(mYPhaVals.get(i + 1).getVal(),i));
            }
            mYPhaVals.set(mDataNum - 1, new Entry((float) impVal/SCALE, mDataNum - 1 ));
        }
        else{
            mYPhaVals.add(new Entry((float)impVal/SCALE,mYPhaVals.size()));
        }

    }

    public LineData getLineData(){
        ArrayList<LineDataSet> dataSets = new ArrayList<LineDataSet>();
        LineDataSet dImp = new LineDataSet(mYImpeVals,"Impedance");
        dImp.setLineWidth(2.5f);
        dImp.setColor(Color.GREEN);
        dImp.setCircleSize(3f);

        LineDataSet dPha = new LineDataSet(mYPhaVals,"Phase");
        dPha.setLineWidth(2.5f);
        dPha.setColor(Color.BLUE);
        dPha.setCircleSize(3f);

        dataSets.add(dImp);
        dataSets.add(dPha);


        LineData lineData = new LineData(mXVals,dataSets);
        return lineData;

    }

    public void cleanData(){
        mYImpeVals.clear();
        mXVals.clear();
        mYPhaVals.clear();
    }

    //add the list from last
    public void addEdemaInfoList(List<EdemaInfo> list){
        int index = list.size() - 1;
        while(index >= 0){
            EdemaInfo edemaInfo = list.get(index);
            this.addXVals(String.valueOf((int)(edemaInfo.getId()/13)));
            this.addImpVal(edemaInfo.getImp());
            this.addPhaVal(edemaInfo.getPha());
            index --;
        }
    }

    //get current X val
    public void addEdemaInfo(EdemaInfo edemaInfo){
        this.addXVals(String.valueOf(Integer.parseInt(mXVals.get(mXVals.size() - 1)) + 1) );
        this.addImpVal(edemaInfo.getImp());
        this.addPhaVal(edemaInfo.getPha());

    }
}
