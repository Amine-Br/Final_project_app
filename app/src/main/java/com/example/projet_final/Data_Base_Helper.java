package com.example.projet_final;

import android.content.ContentValues;
import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class Data_Base_Helper extends SQLiteOpenHelper {
    public static final String DATABASE_NAME = "App.db";
    //tab1
    public static final String TABLE_NAME = "User_table";
    public static final String COL_1 = "ID";
    public static final String COL_2 = "Name";
    public static final String COL_3 = "Phone";
    public static final String COL_4 = "Email";
    public static final String COL_5 = "Password";
    public static final String COL_6 = "Mode_Worker";
    public static final String COL_7 = "Work";
    public static final String COL_8 = "Longitude";
    public static final String COL_9 = "Latitude";







    public Data_Base_Helper(Context c){
        super(c,DATABASE_NAME,null,1);

    }
    @Override
    public void onCreate(SQLiteDatabase db) {
        String create="CREATE TABLE "+TABLE_NAME+" ( "+COL_1+" INTEGER PRIMARY KEY AUTOINCREMENT, "+COL_2+" VARCHAR(25) NOT NULL, "+COL_3
                +" INTEGER NOT NULL, "+COL_4+" TEXT NOT NULL, "+COL_5+" TEXT NOT NULL, "+COL_6+" BOOLEAN DEFAULT 0, "+COL_7+" TEXT DEFAULT NULL, "+
                COL_8+" DOUBLE, "+COL_9+" DOUBLE)";
    db.execSQL(create);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
       db.execSQL("DROP TABLE IF EXISTS "+TABLE_NAME);
        onCreate(db);
    }

    public boolean insertData(String name,Integer phone,String email,String password) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(COL_2,name);
        contentValues.put(COL_3,phone);
        contentValues.put(COL_4,email);
        contentValues.put(COL_5,password);

        long result = db.insert(TABLE_NAME,null ,contentValues);
        if(result == -1)
            return false;
        else
            return true;
    }


}
