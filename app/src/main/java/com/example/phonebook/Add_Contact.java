package com.example.phonebook;
import android.Manifest;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;


public class Add_Contact extends AppCompatActivity {

    private ImageView profileIV;
    private EditText nameET,mobilenumberET;
    private ImageView submitADD;

    //String variable
    private String name,mobilenumber;

    //action bar
    //ActionBar actionBar;

    //permission constant
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;

    //string array of permission
    private String[] cameraPermission;
    private String[] storagePermission;

    //Image uri var
    private Uri imageUri;

    //database helper
    private DbHelper dbHelper;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_contact);

        //init db
        dbHelper = new DbHelper(this);

        //init permission
        cameraPermission = new String[]{Manifest.permission.CAMERA,Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};


        //init actionBar
        //actionBar = getSupportActionBar();

        //set title in action bar
        //actionBar.setTitle("Add Contact");

        //back button
        //actionBar.setDisplayHomeAsUpEnabled(true);
        //actionBar.setDisplayShowHomeEnabled(true);

        //init view
        profileIV = findViewById(R.id.profileIV);
        nameET = findViewById(R.id.nameET);
        mobilenumberET = findViewById(R.id.mobilenumberET);
        submitADD = findViewById(R.id.edited);

        // add event handler
        submitADD.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                saveData();
            }
        });

        profileIV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showImagePickerDialog();
            }
        });
    }

    private void showImagePickerDialog() {
        //option for dialog
        String options[] = {"Camera","Gallery"};

        //Alert dialog builder
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        //setTitle
        builder.setTitle("Choose An Option");
        builder.setItems(options, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                //handle item click
                if (i == 0){ //start from 0 index
                    //camera selected
                    if (!checkCameraPermission()){
                        //request camera permission
                        requestCameraPermission();
                    }else {
                        pickFromCamera();
                    }

                }else if (i == 1){
                    //gallery selected
                    if (!checkStoragePermission()){
                       //request storage permission
                        requestStoragePermission();
                    }else {
                        pickFromGallery();
                    }

                }
            }
        }).create().show();
    }

    private void pickFromGallery() {
        //intent for taking image from gallery
        Intent galleryIntent = new Intent(Intent.ACTION_PICK);
        galleryIntent.setType("image/*"); //only image

        startActivityForResult(galleryIntent,IMAGE_FROM_GALLERY_CODE);
    }

    private void pickFromCamera() {

        //ContentValues for image info
        ContentValues values = new ContentValues();
        values.put(MediaStore.Images.Media.TITLE,"IMAGE_TITLE");
        values.put(MediaStore.Images.Media.DESCRIPTION,"IMAGE_DETAIL");

        //save imageUri
        imageUri = getContentResolver().insert(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,values);

        //intent to open camera
        Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT,imageUri);

        startActivityForResult(cameraIntent,IMAGE_FROM_CAMERA_CODE);
    }

    private void saveData() {

        //take user giver data in variable
        name = nameET.getText().toString();
        mobilenumber = mobilenumberET.getText().toString();

        //

        //check filed data
        if (!name.isEmpty() || !mobilenumber.isEmpty()){
            //save data, if user have only one data
            //function for save data on SQLite Database

            long id = dbHelper.insertPhonebook(
                    ""+imageUri,
                    ""+name,
                    ""+mobilenumber);
            if (id > 0) {
                // Data inserted successfully, show a toast message
                Toast.makeText(getApplicationContext(), "Contact Added Successfully", Toast.LENGTH_SHORT).show();

                // Navigate to the home page
                goToHomePage();
            } else {
                // Data insertion failed, show a toast message
                Toast.makeText(getApplicationContext(), "Failed to insert data", Toast.LENGTH_SHORT).show();
            }
        }else {
            // show toast message
            Toast.makeText(getApplicationContext(), "Nothing to save", Toast.LENGTH_SHORT).show();
        }

    }

    private void goToHomePage() {
        // Create an Intent to navigate to the Home activity
        Intent intent = new Intent(this, Home.class);

        // Add any extra data to the intent if needed
        // intent.putExtra("key", "value");

        // Start the Home activity
        startActivity(intent);

        // Finish the current activity to remove it from the back stack
        finish();
    }

    //ctr + O

    //back button click
    //@Override
    //public boolean onSupportNavigateUp() {
        //onBackPressed();
        //return super.onSupportNavigateUp();
    //}

    //check camera permission
    private boolean checkCameraPermission(){
        boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result & result1;
    }

    //request for camera permission
    private void requestCameraPermission(){
        ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_PERMISSION_CODE); //handle request permission on override method
    }

    //check storage permission
    private boolean checkStoragePermission(){
        boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

        return result1;
    }

    //request for storage permission
    private void requestStoragePermission(){
        ActivityCompat.requestPermissions(this,storagePermission,STORAGE_PERMISSION_CODE);
    }


    //handle request permission
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        switch (requestCode){
            case CAMERA_PERMISSION_CODE:
                if (grantResults.length >0){

                    //if all permission allowed return true , otherwise false
                    boolean cameraAccepted = grantResults[0] == PackageManager.PERMISSION_GRANTED;
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (cameraAccepted && storageAccepted){
                        //both permission granted
                        pickFromCamera();
                    }else {
                        //permission not granted
                        Toast.makeText(getApplicationContext(), "Camera and Storage Permission needed.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;

            case STORAGE_PERMISSION_CODE:
                if (grantResults.length >0){

                    //if all permission allowed return true , otherwise false
                    boolean storageAccepted = grantResults[1] == PackageManager.PERMISSION_GRANTED;

                    if (storageAccepted){
                        //permission granted
                        pickFromGallery();
                    }else {
                        //permission not granted
                        Toast.makeText(getApplicationContext(), "Storage Permission needed.", Toast.LENGTH_SHORT).show();
                    }
                }
                break;
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK) {
            if (requestCode == IMAGE_FROM_GALLERY_CODE) {
                // Picked image from gallery
                Uri pickedImageUri = data.getData();
                // Set the picked image URI to the ImageView
                profileIV.setImageURI(pickedImageUri);
                // Save the image URI
                imageUri = pickedImageUri;
            } else if (requestCode == IMAGE_FROM_CAMERA_CODE) {
                // Set the image URI for the camera result
                profileIV.setImageURI(imageUri);
            }
        }
    }


    //create view object in java file

    //Profile image taking with user permission and crop functionality
    //first permission from manifest,check,request permission
    // by clicking profileIV open dialog to choose image
    //pickImage and save in ImageUri variable
    //SQLite database and add data

    //Create a class  called "Constant" for database and table filed title

    //now insert data in database from AddContact Class

    //done for insert function
    //successfully inserted


    public void CloseClick(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}