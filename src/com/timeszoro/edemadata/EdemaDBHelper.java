package com.timeszoro.edemadata;

import android.content.Context;
import android.content.Intent;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import com.timeszoro.edemacare.EdemaActivity;

/**
 * Created by Administrator on 2015/1/13.
 */
public class EdemaDBHelper extends SQLiteOpenHelper {


    //Get current time;
    private static long curTime = System.currentTimeMillis();
    // Database Version
    private static final int DATABASE_VERSION = 1;
    // Database Name
    private static final String DATABASE_NAME = "edema_db_"+curTime;
    // Edema table name
    public static final String TABLE_EDEMA = "edema";
    // Edema Table Columns names
    public static final String KEY_ID = "id";
    public static final String KEY_FRE = "frequency";
    public static final String KEY_IMP = "impedance";
    public static final String KEY_PHA = "phase";
    private Context mContext;

    public EdemaDBHelper(Context context) {
        super(context,DATABASE_NAME,null,DATABASE_VERSION);
        this.mContext = mContext;
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        String CREATE_EDEMA_TABLE = "CREATE TABLE IF NOT EXISTS " + TABLE_EDEMA + "("
                + KEY_ID + " INTEGER PRIMARY KEY," + KEY_FRE + " INTEGER,"
                + KEY_IMP + " INTEGER, " + KEY_PHA + " INTEGER"+ ")";
        db.execSQL(CREATE_EDEMA_TABLE);
//        final Intent intent = new Intent(EdemaActivity.EDEMA_GATT_CONNECTION);
//        mContext.sendBroadcast(intent);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        // Drop older table if existed
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_EDEMA);
        // Create tables again
        onCreate(db);
    }
}
