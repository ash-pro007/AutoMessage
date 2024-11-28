package com.example.automessageone;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;
import java.util.List;

public class DataBaseHelper extends SQLiteOpenHelper {

    public static final String TABLE_NAME = "message_history";
    public static final String ID = "id";
    public static final String COLUMN_CONTACT_NUMBER = "contact_number";
    public static final String COLUMN_DELIVERED_MESSAGE = "message";
    public static final String COLUMN_TIME = "time";
    public static final String COLUMN_DATE = "date";
    public static final String COLUMN_DAY = "day";

    public static final String SCHEDULE_OBJ_TABLE = "schedule_obj_table";
    public static final String COLUMN_MESSAGE = "message";
    public static final String COLUMN_NO_OF_MESSAGE = "no_of_message";

    private static final String SQL_CREATE_TABLE1 = "CREATE TABLE " + TABLE_NAME + " ( " + COLUMN_CONTACT_NUMBER + " TEXT," +
            COLUMN_DELIVERED_MESSAGE + " TEXT," + COLUMN_TIME + " TEXT," + COLUMN_DATE + " TEXT," + COLUMN_DAY +
            " TEXT )";

    private static final String SQL_CREATE_TABLE2 = "CREATE TABLE " + SCHEDULE_OBJ_TABLE + " ( " + COLUMN_MESSAGE + " TEXT, " +
            COLUMN_NO_OF_MESSAGE + " TEXT, " + COLUMN_DATE + " TEXT, " + COLUMN_TIME + " TEXT );";

    public DataBaseHelper(@Nullable Context context) {
        super(context, TABLE_NAME, null, 1);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {


        db.execSQL(SQL_CREATE_TABLE1);
        db.execSQL(SQL_CREATE_TABLE2);

        // TODO: ID = time + date => ex:: 10:20PM14/04/2023


        // TODO: Create Statement for Schedule message object.

        /*

        String createTableStatement2 = "";

        createTableStatement2 = ;

        db.execSQL(createTableStatement2);

        db.close();

         */

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    public boolean addHistory(MessageHistory messageHistory) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_CONTACT_NUMBER, messageHistory.getContactNumber());
        cv.put(COLUMN_DELIVERED_MESSAGE, messageHistory.getDeliveredMessage());
        cv.put(COLUMN_TIME, messageHistory.getTime());
        cv.put(COLUMN_DATE, messageHistory.getDate());
        cv.put(COLUMN_DAY, messageHistory.getDay());

        long insert = db.insert(TABLE_NAME, null, cv);

        db.close();
        return insert != -1;
    }

    public boolean addScheduleMessageDetails(ScheduleMessageDetails scheduleMessageDetails) {
        SQLiteDatabase db = this.getWritableDatabase();
        ContentValues cv = new ContentValues();

        cv.put(COLUMN_MESSAGE, scheduleMessageDetails.getMessage());
        cv.put(COLUMN_NO_OF_MESSAGE, scheduleMessageDetails.getNoOfMessage());
        cv.put(COLUMN_DATE, scheduleMessageDetails.getDate());
        cv.put(COLUMN_TIME, scheduleMessageDetails.getTime());

        long insert = db.insert(SCHEDULE_OBJ_TABLE, null, cv);

        db.close();
        return insert != -1;
    }


    public List<MessageHistory> getHistory() {
        List<MessageHistory> list = new ArrayList<>();

        String query = "SELECT * FROM " + TABLE_NAME + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(query, null);

        if(cursor.moveToFirst()) {
            do{
                String contactNumber = cursor.getString(0);
                String message = cursor.getString(1);
                String time = cursor.getString(2);
                String date = cursor.getString(3);
                String day = cursor.getString(4);

                MessageHistory messageHistory = new MessageHistory(contactNumber, message, time, date, day);

                list.add(messageHistory);

            }while(cursor.moveToNext());
        }

        cursor.close();
        db.close();

        return list;
    }

    public List<ScheduleMessageDetails> getAllScheduleObj() {
        List<ScheduleMessageDetails> list = new ArrayList<>();

        String qurey = "SELECT * FROM " + SCHEDULE_OBJ_TABLE + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        Cursor cursor = db.rawQuery(qurey, null);

        if(cursor.moveToFirst()) {
            do{
                String message = cursor.getString(0);
                String no_of_messages = cursor.getString(1);
                String date = cursor.getString(2);
                String time = cursor.getString(3);

                ScheduleMessageDetails scheduleMessageDetails = new ScheduleMessageDetails(message, no_of_messages, date, time);
                list.add(scheduleMessageDetails);
            } while(cursor.moveToNext());


        }
        cursor.close();
        db.close();

        return list;
    }

    public void deleteAllRecord() {
        String query = "DELETE FROM " + TABLE_NAME + ";";
        SQLiteDatabase db = this.getReadableDatabase();
        db.execSQL(query);
        db.close();
    }
}
