package com.example.phonebook;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class AdapterContact extends RecyclerView.Adapter<AdapterContact.ContactViewHolder> {

    private Context context;
    private ArrayList<ModelContact> contactList;

    private DbHelper dbHelper;

    //add constructor
    //alt + ins

    public AdapterContact(Context context, ArrayList<ModelContact> contactList) {
        this.context = context;
        this.contactList = contactList;
        dbHelper = new DbHelper(context);
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(context).inflate(R.layout.row_contact_item,parent,false);
        ContactViewHolder vh = new ContactViewHolder(view);
        return vh;
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {

        ModelContact modelContact = contactList.get(position);

        //get data, we need only 3 data
        String id = modelContact.getId();
        String image = modelContact.getImage();
        String name = modelContact.getName();
        String mobile_number = modelContact.getMobile_number();

        //set data in view
        holder.mobileNumber.setText(mobile_number);
        holder.contactName.setText(name);
        if (image.equals("")){
            holder.contactImage.setImageResource(R.drawable.user_5);
        }else {
            holder.contactImage.setImageURI(Uri.parse(image));
        }


        //handle item click and show contact
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Create intent to view
                Intent intent = new Intent(context,ViewContact.class);
                intent.putExtra("contactId",id);
                context.startActivity(intent); //now get data from detail
            }
        });

        //handle edit button click
        holder.edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
               Intent intent = new Intent(context, Edit_Contact.class);
                //pass the value of the current position
                intent.putExtra("ID",id);
                intent.putExtra("NAME",name);
                intent.putExtra("IMAGE",image);
                intent.putExtra("MOBILE_NUMBER",mobile_number);

                //pass a boolean data to define it is for edit purpose
                intent.putExtra("isEditMode", true);

                //start intent
                context.startActivity(intent);


            }
        });

        //handle delete button click
        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //we need database helper class reference
                dbHelper.deletePhonebook(id);

                //refresh data by calling resume state of Home activity
                ((Home)context).onResume();
            }
        });

    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public void filterList(ArrayList<ModelContact> filteredList) {
        contactList = filteredList;
        notifyDataSetChanged();
    }

    class ContactViewHolder extends RecyclerView.ViewHolder{

        //Ari ibutang ang delete ug edit
        //view for row contact item
        ImageView contactImage, edit, delete;
        TextView contactName;
        TextView mobileNumber;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);

            //init view
            contactImage = itemView.findViewById(R.id.contact_image);
            contactName = itemView.findViewById(R.id.contact_name);
            mobileNumber = itemView.findViewById(R.id.mobile_number);
            edit = itemView.findViewById(R.id.edit);
            delete = itemView.findViewById(R.id.delete);
        }
    }
}
