package com.example.phonebook;

public class Constant {

    //Database or db name
    public static final String DATABASE_NAME = "PHONEBOOK_DB";
    //database version
    public static final int DATABASE_VERSION = 1;

    //table name
    public static final String TABLE_NAME = "PHONEBOOK_TABLE";

    //table column or field name
    public static final String C_ID = "ID";
    public static final String C_IMAGE = "IMAGE";
    public static final String C_NAME = "NAME";
    public static final String C_MOBILE_NUMBER = "MOBILE_NUMBER";

    //query for create table
    public static final String  CREATE_TABLE = "CREATE TABLE " + TABLE_NAME + "( "
            + C_ID + " INTEGER PRIMARY KEY AUTOINCREMENT, "
            + C_IMAGE + " TEXT, "
            + C_NAME + " TEXT, "
            + C_MOBILE_NUMBER + " TEXT"
            + " );";


    //Create database helper class fro CRUD query and database creation
}
