package com.example.recyclerview;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

public class MyAdapter3 extends RecyclerView.Adapter<MyAdapter3.ViewHolder> {
    ArrayList<Profile> profiles = new ArrayList<>();
    Context context;

    public MyAdapter3 (ArrayList<Profile> profiles, Context context) {
        this.profiles= profiles;
        this.context = context;
    }


    @NonNull
    @Override
    public MyAdapter3.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.profile_items,parent,false);
        MyAdapter3.ViewHolder viewHolder = new MyAdapter3.ViewHolder(view);
        return viewHolder;
    }



    @Override
    public void onBindViewHolder(@NonNull MyAdapter3.ViewHolder holder, @SuppressLint("RecyclerView") int position) {
        holder.c = profiles.get(position);
        holder.position = position;
        holder.txt_name.setText(profiles.get(position).getNom()+" "+profiles.get(position).getPrenom());
        holder.txt_tel.setText(profiles.get(position).getTel());
        holder.txt_adresse.setText(profiles.get(position).getAdresse());
        holder.txt_dateNai.setText(profiles.get(position).getDateNai());

        Glide.with(context)
                .asBitmap()
                .load(profiles.get(position).getImgUrl())
                .into(holder.img);

    }

    @Override
    public int getItemCount() {
        return profiles.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        private TextView txt_name, txt_dateNai, txt_tel, txt_adresse;
        private ImageView img;

        Profile c;
        int position;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            txt_name = itemView.findViewById(R.id.contact_name);
            txt_dateNai = itemView.findViewById(R.id.contact_dateNai);
            txt_adresse = itemView.findViewById(R.id.contact_adresse);
            txt_tel = itemView.findViewById(R.id.contact_tel);
            img = itemView.findViewById(R.id.contact_image);
            itemView.setOnClickListener(this);


        }

        @Override
        public void onClick(View v) {

        }
    }


}
