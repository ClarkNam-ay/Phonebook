package com.example.phonebook;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
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

public class Edit_Contact extends AppCompatActivity {

    private String id, name, image, mobile_number;
    private Uri imageUri;
    private EditText nameEdit;
    private EditText numberEdit;
    private DbHelper dbHelper;
    private ImageView profileEdit;

    //permission constant
    private static final int CAMERA_PERMISSION_CODE = 100;
    private static final int STORAGE_PERMISSION_CODE = 200;
    private static final int IMAGE_FROM_GALLERY_CODE = 300;
    private static final int IMAGE_FROM_CAMERA_CODE = 400;

    private String[] cameraPermission;
    private String[] storagePermission;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);

        dbHelper = new DbHelper(this);  // Initialize DbHelper

        //init permission
        cameraPermission = new String[]{android.Manifest.permission.CAMERA, android.Manifest.permission.WRITE_EXTERNAL_STORAGE};
        storagePermission = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE};

        // Get data from the Intent
        Intent intent = getIntent();
        if (intent != null) {
            id = intent.getStringExtra("ID");
            name = intent.getStringExtra("NAME");
            image = intent.getStringExtra("IMAGE");
            mobile_number = intent.getStringExtra("MOBILE_NUMBER");

            // Now you have the data, you can use it as needed
            // For example, set the values to EditText fields in your layout
             nameEdit = findViewById(R.id.nameEdit);
             numberEdit = findViewById(R.id.numberEdit);
             profileEdit = findViewById(R.id.profileEdit);
            ImageView edited = findViewById(R.id.edited);

            edited.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    saveData();
                }
            });

            profileEdit.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    showImagePickerDialog();
                }
            });

            nameEdit.setText(name);
            numberEdit.setText(mobile_number);

            imageUri = Uri.parse(image);

            if (image.equals("")){
                profileEdit.setImageResource(R.drawable.user_5);
            }else {
                profileEdit.setImageURI(imageUri);
            }

        }


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

    private void requestStoragePermission() {
            ActivityCompat.requestPermissions(this,storagePermission,STORAGE_PERMISSION_CODE);
    }

    private void requestCameraPermission() {
            ActivityCompat.requestPermissions(this,cameraPermission,CAMERA_PERMISSION_CODE); //handle request permission on override method
    }

    private boolean checkStoragePermission() {
            boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

            return result1;
    }

    private boolean checkCameraPermission() {
            boolean result = ContextCompat.checkSelfPermission(this,Manifest.permission.CAMERA) == (PackageManager.PERMISSION_GRANTED);
            boolean result1 = ContextCompat.checkSelfPermission(this,Manifest.permission.WRITE_EXTERNAL_STORAGE) == (PackageManager.PERMISSION_GRANTED);

            return result & result1;
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
        name = nameEdit.getText().toString();
        mobile_number = numberEdit.getText().toString();

        //

        //check filed data
        if (!name.isEmpty() || !mobile_number.isEmpty()){
            //save data, if user have only one data
            //function for save data on SQLite Database

            dbHelper.updatePhonebook(
                    ""+id,
                    ""+newImageUri,
                    ""+name,
                    ""+mobile_number
            );

            Toast.makeText(getApplicationContext(), "Updated Successfully", Toast.LENGTH_SHORT).show();
            goToHomePage();

        }else {
            // show toast message
            Toast.makeText(getApplicationContext(), "Nothing to save..", Toast.LENGTH_SHORT).show();
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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults){
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
    String newImageUri = "";

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == RESULT_OK){
            if (requestCode == IMAGE_FROM_GALLERY_CODE) {
                Uri pickedImageUri = data.getData();
                profileEdit.setImageURI(pickedImageUri);
                newImageUri = pickedImageUri.toString();  // Update the new image URI
            } else if (requestCode == IMAGE_FROM_CAMERA_CODE) {
                profileEdit.setImageURI(imageUri);
                newImageUri = imageUri.toString();  // Update the new image URI
            }
        }
    }


    public void closeEditClick(View view) {
        Intent intent = new Intent(this, Home.class);
        startActivity(intent);
    }
}