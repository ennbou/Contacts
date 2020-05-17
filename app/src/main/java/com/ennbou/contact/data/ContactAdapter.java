package com.ennbou.contact.data;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.ennbou.contact.R;

import java.util.List;

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.ContactVH> {

    private final LayoutInflater inflater;
    private List<Contact> contacts;

    public ContactAdapter(Context context) {
        inflater = LayoutInflater.from(context);
    }

    public void setContact(List<Contact> contacts) {
        this.contacts = contacts;
        notifyDataSetChanged();
    }


    @NonNull
    @Override
    public ContactVH onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View contactItem = inflater.inflate(R.layout.contact_item, parent, false);
        return new ContactVH(contactItem);
    }

    @Override
    public void onBindViewHolder(@NonNull ContactVH holder, int position) {
        if (contacts != null) {
            Contact contact = contacts.get(position);
            String fullName = contact.getFirstName() + " " + contact.getLastName();
            holder.fullName.setText(fullName);
            holder.phone.setText(contact.getPhoneNumber());
        }
    }

    @Override
    public int getItemCount() {
        return (contacts != null) ? contacts.size() : 0;
    }

    public Contact getItem(int position) {
        return contacts.get(position);
    }

    static class ContactVH extends RecyclerView.ViewHolder {
        TextView fullName;
        TextView phone;
        ImageView image;

        ContactVH(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.txt_img);
            fullName = itemView.findViewById(R.id.full_name);
            phone = itemView.findViewById(R.id.phone);
        }
    }


}
