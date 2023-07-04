package com.example.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.constraintlayout.widget.ConstraintAttribute;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter2 extends RecyclerView.Adapter<MyAdapter2.ViewHolder> {
    ArrayList<Contact> contacts = new ArrayList<>();
    Context context;
    private OnItemClickListener onItemClickListener;

    public interface OnItemClickListener {
        void onItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.onItemClickListener = listener;
    }

    public MyAdapter2(ArrayList<Contact> contacts, Context context) {
        this.contacts = contacts;
        this.context = context;
    }

    public void setFilteredContacts(ArrayList<Contact> filteredContacts) {
        this.contacts = filteredContacts;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public MyAdapter2.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.contact_items, parent, false);
        MyAdapter2.ViewHolder viewHolder = new MyAdapter2.ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(@NonNull MyAdapter2.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.c = contacts.get(position);
        holder.position = position;
        holder.txt_name.setText(contacts.get(position).getNom() + " " + contacts.get(position).getPrenom());
        holder.txt_service.setText(contacts.get(position).getService());


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (onItemClickListener != null) {
                    onItemClickListener.onItemClick(position);
                }
            }
        });

        Glide.with(context)
                .asBitmap()
                .load(contacts.get(position).getImgUrl())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return contacts.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_name, txt_service;
        private ImageView img;

        Contact c;
        int position;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.contact_name);
            txt_service = itemView.findViewById(R.id.contact_service);
            img = itemView.findViewById(R.id.contact_image);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            if (onItemClickListener != null) {
                onItemClickListener.onItemClick(position);

                //Intent intent = new Intent(context, Contact_details.class);
                //intent.putExtra("contact",  c);
                //intent.putExtra("position", position);
                //context.startActivity(intent);
            }


        }

    }
}
