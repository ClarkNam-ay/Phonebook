package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;

public class ViewContact extends AppCompatActivity {

    //view
    private TextView contactnametv,mobilenumbertv;
    private ImageView profileIv;

    private String id;

    //database helper
    private DbHelper dbHelper;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_contact);

        dbHelper = new DbHelper(this);

        //get data from intent
        Intent intent = getIntent();
        id = intent.getStringExtra("contactId");

        //init view
        contactnametv = findViewById(R.id.contactnametv);
        mobilenumbertv = findViewById(R.id.mobilenumbertv);

        profileIv = findViewById(R.id.profileIv);

        loadDataById();

    }

    private void loadDataById() {
        //get data from database
        //query for find data by id
        String selectQuery = "SELECT * FROM " + Constant.TABLE_NAME + " WHERE " + Constant.C_ID + " = \"" + id + "\"";

        SQLiteDatabase db = dbHelper.getReadableDatabase();
        Cursor cursor = db.rawQuery(selectQuery,null);

        if (cursor.moveToFirst()){
            do {
                        String image = cursor.getString(cursor.getColumnIndexOrThrow(Constant.C_IMAGE));
                        String name = cursor.getString(cursor.getColumnIndexOrThrow(Constant.C_NAME));
                       String mobile_number = cursor.getString(cursor.getColumnIndexOrThrow(Constant.C_MOBILE_NUMBER));

                       contactnametv.setText(name);
                       mobilenumbertv.setText(mobile_number);

                       if (image.equals("null")){
                           profileIv.setImageResource(R.drawable.user_5);
                       }else {
                           profileIv.setImageURI(Uri.parse(image));
                       }

            }while (cursor.moveToNext());
        }

        db.close();
    }

    public void XClick(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}