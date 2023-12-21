package com.example.phonebook;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import androidx.annotation.Nullable;

import java.util.ArrayList;

//class for database helper
public class DbHelper extends SQLiteOpenHelper {
    public DbHelper(@Nullable Context context) {
        super(context, Constant.DATABASE_NAME, null, Constant.DATABASE_VERSION);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {

        //create table on database
        db.execSQL(Constant.CREATE_TABLE);

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

        //upgrade table if any structure change in db

        //drop table if exists
        db.execSQL("DROP TABLE IF EXISTS "+Constant.TABLE_NAME);

        //create table again
        onCreate(db);

    }

    //insert function to insert in database
    public long insertPhonebook(String image, String name, String mobile_number){

        //get writable database to write data on db
        SQLiteDatabase db = this.getWritableDatabase();

        //create content value class object to save data
        ContentValues contentValues = new ContentValues();

        //id will save automatically as we write query
        contentValues.put(Constant.C_IMAGE,image);
        contentValues.put(Constant.C_NAME,name);
        contentValues.put(Constant.C_MOBILE_NUMBER,mobile_number);

        //insert data in row, it will return id of record
        long id = db.insert(Constant.TABLE_NAME,null,contentValues);

        //close db
        db.close();

        //return id
        return id;

    }

    //update function to update data in database
    public void updatePhonebook(String id, String newImage, String name, String mobile_number){
        //get writable database to write data on db
        SQLiteDatabase db = this.getWritableDatabase();

        //create content value class object to save data
        ContentValues contentValues = new ContentValues();

        // Update other columns if needed
        contentValues.put(Constant.C_NAME, name);
        contentValues.put(Constant.C_MOBILE_NUMBER, mobile_number);

        // Check if the new image URI is not empty before updating it
        if (!newImage.isEmpty()) {
            contentValues.put(Constant.C_IMAGE, newImage);
        }

        //update data in row, it will return the number of affected rows
        int rowsAffected = db.update(Constant.TABLE_NAME, contentValues, Constant.C_ID + " =? ", new String[]{id});

        if (rowsAffected > 0) {
            // Successfully updated, you can log or show a message if needed
        } else {
            // Update failed, you can log or show a message if needed
        }

        //close db
        db.close();
    }

    // delete data by id
    public void deletePhonebook(String id){
        // get writable database
        SQLiteDatabase db = getWritableDatabase();

        db.delete(Constant.TABLE_NAME,Constant.C_ID + " =?", new String[]{id});

        db.close();
    }

    //delete all data
    public void deleteAllPhonebook(){
        //get writable database
        SQLiteDatabase db = getWritableDatabase();

        db.execSQL("DELETE FROM "+Constant.TABLE_NAME);

        db.close();
    }

    //get data
    public ArrayList<ModelContact> getAllData(){
        //create arraylist
        ArrayList<ModelContact> arrayList = new ArrayList<>();
        //sql command query
        String selectQuery = "SELECT * FROM "+Constant.TABLE_NAME;

        //get readable db
        SQLiteDatabase db = getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery, null);

        //looping through all record and to list
        if (cursor.moveToFirst()){
            do {
                ModelContact modelContact = new ModelContact(
                        // only id is the integer type
                        ""+cursor.getInt(cursor.getColumnIndexOrThrow(Constant.C_ID)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constant.C_IMAGE)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constant.C_NAME)),
                        ""+cursor.getString(cursor.getColumnIndexOrThrow(Constant.C_MOBILE_NUMBER))
                );
                arrayList.add(modelContact);

            }while (cursor.moveToNext());
        }

        db.close();
        return arrayList;
    }
}
