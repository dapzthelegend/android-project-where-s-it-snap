package com.gamecodeschool.whereitssnap;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

public class ContactDbHelper extends SQLiteOpenHelper {
    public static final String DB_NAME = "wis_db_2";
    public  static final int DB_VERSION = 2;
    public static final String CREATE_TABLE_PHOTO = "create table " + ContactContract.ContractEntry.TABLE_PHOTO_NAME +
            "(" + ContactContract.ContractEntry.TABLE_PHOTO_ID + " integer primary key autoincrement not null," +
            ContactContract.ContractEntry.TABLE_PHOTO_IMAGETITLE + " text not null," +
            ContactContract.ContractEntry.TABLE_PHOTO_URI + " text not null," +
            ContactContract.ContractEntry.TABLE_ROW_LAT + " real," +
            ContactContract.ContractEntry.TABLE_ROW_LONG +" real,"+
            ContactContract.ContractEntry.TABLE_PHOTO_TAG1 + " text not null," +
            ContactContract.ContractEntry.TABLE_PHOTO_TAG2 + " text not null," +
            ContactContract.ContractEntry.TABLE_PHOTO_TAG3 + " text not null);";

    public  static final String CREATE_TABLE = "create table " + ContactContract.ContractEntry.TABLE_NAME +
            "(" + ContactContract.ContractEntry.TABLE_PHOTO_ID+ " integer primary key autoincrement not null," +
            ContactContract.ContractEntry.TABLE_TAGS_TAG + " text not null);";

    public  static  final String DROP_TABLE_PHOTO= "drop table if exists " +
            ContactContract.ContractEntry.TABLE_PHOTO_NAME;
    public static final String DROP_TABLE_TAG = "drop table if exists " +
            ContactContract.ContractEntry.TABLE_NAME;


    public ContactDbHelper(Context context){
        super(context,DB_NAME,null,DB_VERSION);


    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_TABLE);

     db.execSQL(CREATE_TABLE_PHOTO);
     Log.e("Tag", "Upgrading");





    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        String addLongColumn = "ALTER TABLE " + ContactContract.ContractEntry.TABLE_PHOTO_NAME +
                " ADD " + ContactContract.ContractEntry.TABLE_ROW_LONG + " real;";

        db.execSQL(addLongColumn);

        String addLatColumn = "ALTER TABLE " + ContactContract.ContractEntry.TABLE_PHOTO_NAME
                + " ADD " + ContactContract.ContractEntry.TABLE_ROW_LAT + " real;";

        db.execSQL(addLatColumn);



    }

    public void addPhoto(Photo photo,SQLiteDatabase db){

        String query = "INSERT INTO " + ContactContract.ContractEntry.TABLE_PHOTO_NAME + " (" +
                ContactContract.ContractEntry.TABLE_PHOTO_IMAGETITLE + ", " +
                ContactContract.ContractEntry.TABLE_PHOTO_URI + ", " +
                ContactContract.ContractEntry.TABLE_ROW_LAT + ", " +
                ContactContract.ContractEntry.TABLE_ROW_LONG + ", " +
                ContactContract.ContractEntry.TABLE_PHOTO_TAG1 + ", " +
                ContactContract.ContractEntry.TABLE_PHOTO_TAG2 + ", " +
                ContactContract.ContractEntry.TABLE_PHOTO_TAG3 +  ") " +
                "VALUES (" +    "'" + photo.getTitle() + "'" + ", " +
                "'" + photo.getStorageLocation() + "'" + ", " +
                photo.getGpsLocation().getLatitude() + ", " +
                photo.getGpsLocation().getLongitude() + ", " +
                "'" + photo.getTag1() + "'" + ", " +    "'" +
                photo.getTag2() + "'" + ", " +    "'" + photo.getTag3() + "'" +  ");";
        Log.i("addPhoto()", query);
        db.execSQL(query);
        String query2 = "INSERT INTO " + ContactContract.ContractEntry.TABLE_NAME +
                " (" + ContactContract.ContractEntry.TABLE_TAGS_TAG + ") " +
                "SELECT '" + photo.getTag1() + "' " +
                "WHERE NOT EXISTS ( SELECT 1 FROM " +
                ContactContract.ContractEntry.TABLE_NAME +    " WHERE " +
                ContactContract.ContractEntry.TABLE_TAGS_TAG + " = " +    "'" + photo.getTag1() + "');";

        db.execSQL(query2);
        String quer = "INSERT INTO " + ContactContract.ContractEntry.TABLE_NAME +
                " (" + ContactContract.ContractEntry.TABLE_TAGS_TAG + ") " +
                "SELECT '" + photo.getTag2() + "' " +
                "WHERE NOT EXISTS ( SELECT 1 FROM " +
                ContactContract.ContractEntry.TABLE_NAME +    " WHERE " +
                ContactContract.ContractEntry.TABLE_TAGS_TAG + " = " +    "'" + photo.getTag2() + "');";

        db.execSQL(quer);

        String quer1 = "INSERT INTO " + ContactContract.ContractEntry.TABLE_NAME +
                " (" + ContactContract.ContractEntry.TABLE_TAGS_TAG + ") " +
                "SELECT '" + photo.getTag3() + "' " +
                "WHERE NOT EXISTS ( SELECT 1 FROM " +
                ContactContract.ContractEntry.TABLE_NAME +    " WHERE " +
                ContactContract.ContractEntry.TABLE_TAGS_TAG + " = " +    "'" + photo.getTag3() + "');";

        db.execSQL(quer1);







    }


    public Cursor getTags(SQLiteDatabase db){
        Cursor c = db.rawQuery("SELECT " + ContactContract.ContractEntry.TABLE_PHOTO_ID +
                ", " + ContactContract.ContractEntry.TABLE_TAGS_TAG + " from " + ContactContract.ContractEntry.TABLE_NAME, null);
        c.moveToFirst();
        return c;

    }

    public Cursor getTitles(SQLiteDatabase db) {

        Cursor c = db.rawQuery("SELECT " + ContactContract.ContractEntry.TABLE_PHOTO_ID +
                ", " + ContactContract.ContractEntry.TABLE_PHOTO_IMAGETITLE + " from " + ContactContract.ContractEntry.TABLE_PHOTO_NAME,
                null);
        c.moveToFirst();
        Log.e("Title", "Getting titles from database");
        return c;
    }

    public Cursor getPhoto (int id, SQLiteDatabase db){
        Cursor c = db.rawQuery("SELECT * from " + ContactContract.ContractEntry.TABLE_PHOTO_NAME +
                " WHERE " + ContactContract.ContractEntry.TABLE_PHOTO_ID + " = " + id, null);
        c.moveToFirst();
        return c;

    }
    public Cursor getTitlesWithTag(String tag, SQLiteDatabase db) {
        Cursor c = db.rawQuery("SELECT " + ContactContract.ContractEntry.TABLE_PHOTO_ID + ", " +
                ContactContract.ContractEntry.TABLE_PHOTO_IMAGETITLE + " from " +
                ContactContract.ContractEntry.TABLE_PHOTO_NAME + " WHERE " +
                ContactContract.ContractEntry.TABLE_PHOTO_TAG1 + " = '" + tag + "' or " +
                ContactContract.ContractEntry.TABLE_PHOTO_TAG2 + " = '" + tag + "' or " +
                ContactContract.ContractEntry.TABLE_PHOTO_TAG3 + " = '" + tag + "';"    ,
                null);
        c.moveToFirst();
        return c;
    }

    public void deleteContacts(int id, SQLiteDatabase db){
        String selection = ContactContract.ContractEntry.TABLE_PHOTO_ID + " = " + id;
        db.delete(ContactContract.ContractEntry.TABLE_PHOTO_NAME, selection,null);
        Log.e("Delete" , "In on yes click listener" + id);
    }



}
