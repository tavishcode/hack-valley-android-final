package com.kevalpatel2106.sample;

/**
 * Created by tavish on 2/24/18.
 */

import android.database.sqlite.SQLiteOpenHelper;

/**
 * Created by tavish on 2/24/18.
 */

import android.content.ContentValues;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

public class databaseHelper extends SQLiteOpenHelper {

    private static final String TABLE_NAME_USERS = "users";
    private static final String TABLE_NAME_MESSAGES = "messages";
    private static final String COLUMN_NAME_OWNER = "OWNER";
    private static final String COLUMN_NAME_ID = "ID";
    private static final String COLUMN_NAME_SENDER = "SENDER";
    private static final String COLUMN_NAME_FBID = "FACEBOOKID";
    private static final String COLUMN_NAME_TIME = "TIME";
    private static final String COLUMN_NAME_DATE = "DATE";
    private static final String COLUMN_NAME_MESSAGE = "MESSAGE";
    private static final String COLUMN_NAME_READ = "READ";



    private static final String TEXT_TYPE = " TEXT";
    private static final String INTEGER_TYPE = " INTEGER";
    private static final String REAL_TYPE = " REAL";
    private static final String COMMA_SEP = ",";

    private static final String SQL_CREATE_USERS =
            "CREATE TABLE " + TABLE_NAME_USERS + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    COLUMN_NAME_SENDER + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_FBID + TEXT_TYPE +  COMMA_SEP +
                    COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_READ + INTEGER_TYPE + COMMA_SEP +
                    COLUMN_NAME_OWNER + TEXT_TYPE +  COMMA_SEP +
                    COLUMN_NAME_TIME + TEXT_TYPE + " )";

    private static final String SQL_CREATE_MESSAGES =
            "CREATE TABLE " + TABLE_NAME_MESSAGES + " (" +
                    COLUMN_NAME_ID + " INTEGER PRIMARY KEY AUTOINCREMENT" + COMMA_SEP +
                    COLUMN_NAME_TIME + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_DATE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_MESSAGE + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_SENDER + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_FBID + TEXT_TYPE + COMMA_SEP +
                    COLUMN_NAME_OWNER + TEXT_TYPE + " )";


    private static final String SQL_DELETE_USERS =
            "DROP TABLE IF EXISTS " + TABLE_NAME_USERS;

    private static final String SQL_DELETE_MESSAGES =
            "DROP TABLE IF EXISTS " + TABLE_NAME_MESSAGES;

    public static final String DATABASE_NAME = "FavEx.db";

    public databaseHelper(Context context) {
        super(context, DATABASE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        db.execSQL(SQL_CREATE_USERS);
        db.execSQL(SQL_CREATE_MESSAGES);
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL(SQL_DELETE_USERS);
        db.execSQL(SQL_DELETE_MESSAGES);
        onCreate(db);
    }

    public boolean insertUser(String sender, String facebookId, String date, String myFacebookId, String time){
        SQLiteDatabase db = this.getWritableDatabase();

        db.delete(TABLE_NAME_USERS, "FACEBOOKID = " + "\"" + facebookId + "\"", null);

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_SENDER, sender);
        cv.put(COLUMN_NAME_FBID, facebookId);
        cv.put(COLUMN_NAME_DATE, date);
        cv.put(COLUMN_NAME_READ, 0);
        cv.put(COLUMN_NAME_OWNER, myFacebookId);
        cv.put(COLUMN_NAME_TIME, time);

        if( db.insert(TABLE_NAME_USERS, null, cv) == -1){
            return false;
        }
        return true;
    }

    public void updateRead(String facebookId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_READ, 1);

        db.update(TABLE_NAME_USERS, cv, "FACEBOOKID = " + "\"" + facebookId + "\"", null);
    }

    public boolean insertMessage(String message, String sender, String facebookId, String time, String date, String myFacebookId){
        SQLiteDatabase db = this.getWritableDatabase();

        ContentValues cv = new ContentValues();
        cv.put(COLUMN_NAME_MESSAGE, message);
        cv.put(COLUMN_NAME_SENDER, sender);
        cv.put(COLUMN_NAME_FBID, facebookId);
        cv.put(COLUMN_NAME_TIME, time);
        cv.put(COLUMN_NAME_DATE, date);
        cv.put(COLUMN_NAME_OWNER, myFacebookId);

        if( db.insert(TABLE_NAME_MESSAGES, null, cv) == -1){
            return false;
        }
        return true;
    }

    public String getUserName(String myFacebookId, String facebookId){
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor tmp = db.rawQuery("select * from " + TABLE_NAME_USERS + " WHERE " + COLUMN_NAME_OWNER + " = \"" + myFacebookId + "\"" + " AND " + COLUMN_NAME_FBID + " = \"" + facebookId + "\"", null);
        if(tmp.getCount() > 0){
            tmp.moveToPosition(0);
            return tmp.getString(1);
        }
        else {
            return "Favor Recipient";
        }
    }

    public Cursor getAllChats(String myFacebookId){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("select * from " + TABLE_NAME_USERS + " WHERE " + COLUMN_NAME_OWNER + " = \"" + myFacebookId + "\"", null);
    }

    public Cursor getMessagesByFacebookId(String facebookId, String myFacebookId){
        SQLiteDatabase db = this.getReadableDatabase();

        return db.rawQuery("SELECT * FROM " + TABLE_NAME_MESSAGES + " WHERE " + COLUMN_NAME_FBID + " = \"" + facebookId + "\"" + " AND " + COLUMN_NAME_OWNER + " = \"" + myFacebookId + "\"", null);
    }
}