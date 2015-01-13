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


    public EdemaDBManager(Context context){
        mDBHelper = new EdemaDBHelper(context);
    }

    //Add new edema data
    public void addEdemaData(EdemaInfo data){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        ContentValues values = new ContentValues();
        values.put(EdemaDBHelper.KEY_FRE,data.getFre());
        values.put(EdemaDBHelper.KEY_IMP,data.getImp());
        values.put(EdemaDBHelper.KEY_PHA,data.getPha());

        db.insert(EdemaDBHelper.TABLE_EDEMA, null, values);
        db.close();
    }

    public void addEdemaList(List<EdemaInfo> list){
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
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

        db.close();
    }

    // Getting single contact
    public EdemaInfo getEdemaInfo(int id) {
        SQLiteDatabase db = mDBHelper.getReadableDatabase();

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
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);
        if (cursor.moveToFirst()) {
            do {
                EdemaInfo edemaInfo = new EdemaInfo();
                edemaInfo.setFre(Integer.parseInt(cursor.getString(0)));
                edemaInfo.setFre(Integer.parseInt(cursor.getString(1)));
                edemaInfo.setFre(Integer.parseInt(cursor.getString(2)));
                // Adding contact to list
                edemaInfoList.add(edemaInfo);
            } while (cursor.moveToNext());
        }
        return edemaInfoList;
    }
    // Getting EdemaInfo Count
    public int getContactsCount() {
        String countQuery = "SELECT  * FROM " + EdemaDBHelper.TABLE_EDEMA;
        SQLiteDatabase db = mDBHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(countQuery, null);
        cursor.close();

        // return count
        return cursor.getCount();
    }

    //Delete single EdemaInfo

    public void deleteContact(EdemaInfo edemaInfo) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();
        db.delete(EdemaDBHelper.TABLE_EDEMA, EdemaDBHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(edemaInfo.getId()) });
        db.close();
    }

    // Updating single edemaInfo
    public int updateEdemaInfo(EdemaInfo data) {
        SQLiteDatabase db = mDBHelper.getWritableDatabase();

        ContentValues values = new ContentValues();
        values.put(EdemaDBHelper.KEY_FRE,data.getFre());
        values.put(EdemaDBHelper.KEY_IMP,data.getImp());
        values.put(EdemaDBHelper.KEY_PHA,data.getPha());

        // updating row
        return db.update(EdemaDBHelper.TABLE_EDEMA,values, EdemaDBHelper.KEY_ID + " = ?",
                new String[] { String.valueOf(data.getId()) });
    }



}
