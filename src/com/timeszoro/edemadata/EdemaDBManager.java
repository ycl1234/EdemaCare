package com.timeszoro.edemadata;

import android.app.Application;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2015/1/13.
 */
public class EdemaDBManager {
    private EdemaDBHelper mDBHelper;
    private SQLiteDatabase db;

    public EdemaDBManager(Context context){
         long curTime = System.currentTimeMillis();
        String dbName = "edema_db_"+curTime;
        mDBHelper = new EdemaDBHelper(context,dbName);
    }

    //Add new edema data
    public void addEdemaData(EdemaInfo data){

        db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EdemaDBHelper.KEY_FRE,data.getFre());
        values.put(EdemaDBHelper.KEY_IMP,data.getImp());
        values.put(EdemaDBHelper.KEY_PHA,data.getPha());

        db.insert(EdemaDBHelper.TABLE_EDEMA, null, values);
//        db.close();
    }

    public void addEdemaList(List<EdemaInfo> list){
        db = mDBHelper.getWritableDatabase();
        db.beginTransaction();
        try {
            for(EdemaInfo e: list){
                db.execSQL("INSERT INTO "+ EdemaDBHelper.TABLE_EDEMA+" VALUES(null,?,?,?)",new Object[]{e.getFre(),e.getImp(),e.getPha()});
            }
            db.setTransactionSuccessful();
        }
        finally{
            db.endTransaction();
        }

//        db.close();
    }

    // Getting single contact
    public EdemaInfo getEdemaInfo(int id) {
        db = mDBHelper.getReadableDatabase();

        Cursor cursor = db.query(EdemaDBHelper.TABLE_EDEMA, new String[] { EdemaDBHelper.KEY_FRE,
                        EdemaDBHelper.KEY_IMP, EdemaDBHelper.KEY_PHA }, EdemaDBHelper.KEY_ID + "=?",
                new String[] { String.valueOf(id) }, null, null, null, null);
        if (cursor != null)
            cursor.moveToFirst();

        EdemaInfo data = new EdemaInfo(Integer.parseInt(cursor.getString(0)),
                Integer.parseInt(cursor.getString(1)), Integer.parseInt(cursor.getString(2)));
        // return contact
        return data;
    }

    //get the Edema data ArrayList
    public List<EdemaInfo> getAllContacts(){
        List<EdemaInfo> edemaInfoList = new ArrayList<EdemaInfo>();
        String selectQuery = "SELECT * FROM "+ EdemaDBHelper.TABLE_EDEMA;
        db = mDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                EdemaInfo edemaInfo = new EdemaInfo();
                edemaInfo.setFre(Integer.parseInt(cursor.getString(0)));
                edemaInfo.setImp(Integer.parseInt(cursor.getString(1)));
                edemaInfo.setPha(Integer.parseInt(cursor.getString(2)));
                // Adding contact to list
                edemaInfoList.add(edemaInfo);
            } while (cursor.moveToNext());
        }
        return edemaInfoList;
    }
    // Getting EdemaInfo Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + EdemaDBHelper.TABLE_EDEMA;
        db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    //Delete single EdemaInfo

    public void deleteContact(EdemaInfo edemaInfo) {
        db = mDBHelper.getWritableDatabase();
        db.delete(EdemaDBHelper.TABLE_EDEMA, EdemaDBHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(edemaInfo.getId()) });
//        db.close();
    }

    // Updating single edemaInfo
    public int updateEdemaInfo(EdemaInfo data) {
        db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EdemaDBHelper.KEY_FRE,data.getFre());
        values.put(EdemaDBHelper.KEY_IMP,data.getImp());
        values.put(EdemaDBHelper.KEY_PHA,data.getPha());

        // updating row
        return db.update(EdemaDBHelper.TABLE_EDEMA,values, EdemaDBHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
    }

    //query the last 20 data
    public List<EdemaInfo> queryLastData(int fre){
        List<EdemaInfo> edemaInfoList = new ArrayList<EdemaInfo>();
        String selectQuery = "SELECT * FROM "+ EdemaDBHelper.TABLE_EDEMA +" WHERE "+EdemaDBHelper.KEY_FRE+"='"+fre+"'";
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        int index = 0;
        if (cursor.moveToLast()) {
            do {
                EdemaInfo edemaInfo = new EdemaInfo();
                edemaInfo.setId(Integer.parseInt(cursor.getString(0)));
                edemaInfo.setFre(Integer.parseInt(cursor.getString(1)));
                edemaInfo.setImp(Integer.parseInt(cursor.getString(2)));
                edemaInfo.setPha(Integer.parseInt(cursor.getString(3)));
                // Adding contact to list
                edemaInfoList.add(edemaInfo);
                index++;
            } while (cursor.moveToPrevious() && index < 20);
        }
        cursor.close();
        return edemaInfoList;
    }

    public int getFreDataNum(int fre){

        List<EdemaInfo> edemaInfoList = new ArrayList<EdemaInfo>();
        String selectQuery = "SELECT * FROM "+ EdemaDBHelper.TABLE_EDEMA +" WHERE "+EdemaDBHelper.KEY_FRE+"='"+fre+"'";
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        cursor.close();
        return cursor.getCount();

    }
    public void close(){
        if(db != null){
            db.close();
            db = null;
            mDBHelper = null;
        }

    }

}
