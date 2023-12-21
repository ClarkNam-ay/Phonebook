package com.example.phonebook;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.View;
import android.widget.ImageView;
import androidx.appcompat.app.AlertDialog;
import android.content.DialogInterface;
import android.widget.SearchView;

import java.util.ArrayList;

public class Home extends AppCompatActivity {
    //view
    private RecyclerView phonebookRV;

    //db
    private DbHelper dbHelper;

    //adapter
    private AdapterContact adapterContact;
    private SearchView searchView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);

        //init db
        dbHelper = new DbHelper(this);

        searchView = findViewById(R.id.searchView);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterData(newText);
                return false;
            }
        });

        //initialization
        phonebookRV = findViewById(R.id.phonebookRV);

        phonebookRV.setLayoutManager(new LinearLayoutManager(this));

        phonebookRV.setHasFixedSize(true);

        // Load data here
        loadData();

        // Set click listener for your delete button
        ImageView deleteAll = findViewById(R.id.deleteAll); // Replace with your actual button ID
        deleteAll.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDeleteConfirmationDialog();
            }
        });


    }

    private void filterData(String query) {
        ArrayList<ModelContact> filteredList = new ArrayList<>();

        for (ModelContact contact : dbHelper.getAllData()) {
            if (contact.getName().toLowerCase().contains(query.toLowerCase())) {
                filteredList.add(contact);
            }
        }

        adapterContact.filterList(filteredList);
    }

    private void showDeleteConfirmationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Confirm Deletion");
        builder.setMessage("Are you sure you want to delete all contacts?");
        builder.setPositiveButton("Yes", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked Yes, delete all data
                dbHelper.deleteAllPhonebook();
                loadData();
                dialog.dismiss();
            }
        });
        builder.setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // User clicked No, dismiss the dialog
                dialog.dismiss();
            }
        });

        AlertDialog dialog = builder.create();
        dialog.show();
    }


    private void loadData() {
        //Log.d("HomeActivity", "loadData() called");

        //ArrayList<ModelContact> data = dbHelper.getAllData();
        //Log.d("HomeActivity", "Size of data loaded: " + data.size());

        adapterContact = new AdapterContact(this,dbHelper.getAllData());
        phonebookRV.setAdapter(adapterContact);
        //adapterContact.notifyDataSetChanged(); // Add this line
    }

    //karon ni
    @Override
    protected void onResume() {
        super.onResume();
        loadData();
    }



    public void AddClick(View view) {
        Intent intent = new Intent(this, Add_Contact.class);
        startActivity(intent);
    }
    //show SQLite data in recyclerview
    //add recyclerview in home class
    //create model class for data
    //create adapter class to show data in recyclerview

    //get data from sql and show data in recyclerview by adapter

    //to get data we need sql command in db helper
    //create activity for detail contact

    // edit or update nata
    //Search Function nata
}