package com.example.automessageone;

// ContactAdapter.java

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactViewHolder> {

    private List<Contact> contactList;
    private Context context;

    public ContactAdapter(List<Contact> contactList, Context context) {
        this.contactList = contactList;
        this.context = context;
    }

    @NonNull
    @Override
    public ContactViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_contact, parent, false);
        return new ContactViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactViewHolder holder, int position) {
        Contact contact = contactList.get(position);
        holder.textViewName.setText(contact.getName());
        holder.textViewNumber.setText(contact.getNumber());

        Glide.with(context)
                .load(contact.getImageUri())
                .placeholder(R.drawable.default_image)
                .error(R.drawable.default_image)
                .into(holder.imageViewContact);

        // Set click listener for the remove button
        holder.btnRemoveContactFromList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                removeContact(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return contactList.size();
    }

    public class ContactViewHolder extends RecyclerView.ViewHolder {
        TextView textViewName;
        TextView textViewNumber;
        ImageView imageViewContact;
        Button btnRemoveContactFromList;

        public ContactViewHolder(@NonNull View itemView) {
            super(itemView);
            textViewName = itemView.findViewById(R.id.textViewName);
            textViewNumber = itemView.findViewById(R.id.textViewNumber);
            imageViewContact = itemView.findViewById(R.id.imageViewContact);
            btnRemoveContactFromList = itemView.findViewById(R.id.btnRemoveContactFromList);
        }
    }

    // Method to remove contact
    public void removeContact(int position) {
        contactList.remove(position);  // Remove contact from the list
        notifyItemRemoved(position);   // Notify the adapter to refresh the list
        notifyItemRangeChanged(position, contactList.size());  // Update the remaining items
    }
}
