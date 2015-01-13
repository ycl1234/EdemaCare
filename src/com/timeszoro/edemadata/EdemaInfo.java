package com.timeszoro.edemadata;

/**
 * Created by Administrator on 2015/1/13.
 */
public class EdemaInfo {
    private int mId;
    private int mFre;
    private int mImp;
    private int mPha;

    public EdemaInfo(int fre,int imp,int pha){
        mFre = fre;
        mImp = imp;
        mPha = pha;
    }
    public EdemaInfo(){

    }
    public void setFre(int mFre) {
        this.mFre = mFre;
    }

    public int getFre() {
        return mFre;
    }

    public void setImp(int mImp) {
        this.mImp = mImp;
    }

    public int getImp() {
        return mImp;
    }

    public void setPha(int mPha) {
        this.mPha = mPha;
    }

    public int getPha() {
        return mPha;
    }



    public int getId() {
        return mId;
    }
}
