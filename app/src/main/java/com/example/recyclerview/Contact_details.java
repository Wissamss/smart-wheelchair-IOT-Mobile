package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class Contact_details extends AppCompatActivity {

    Contact c;
    int position;
    ImageView img, call;
    TextView txt_name, txt_tel, txt_email, txt_service;
    String id;
    FloatingActionButton edit_btn;
    FloatingActionButton delete_btn;
    DocumentReference db;
    FirebaseUser user;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_details);
        user = FirebaseAuth.getInstance().getCurrentUser();
        img = findViewById(R.id.contact_img);
        txt_name = findViewById(R.id.contact_name);
        txt_tel = findViewById(R.id.contact_tel);
        txt_email = findViewById(R.id.contact_email);
        txt_service = findViewById(R.id.contact_service);
        db = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        c = (Contact) getIntent().getSerializableExtra("contact");
        position = (int) getIntent().getSerializableExtra("position");
        edit_btn = findViewById(R.id.edit);
        delete_btn = findViewById(R.id.delete);
        txt_name.setText(c.getNom()+" "+c.getPrenom());
        txt_email.setText(c.getEmail());
        txt_tel.setText(c.getTel());
        txt_service.setText(c.getService());
        delete_btn.setOnClickListener(new View.OnClickListener() {
                                          @Override
                                          public void onClick(View v) {
                                              db.collection("emergency-contacts").document(c.getTel()).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
                                                  @Override
                                                  public void onComplete(@NonNull Task<Void> task) {
                                                      if (task.isSuccessful()) {
                                                          Toast.makeText(Contact_details.this, "Contact deleted", Toast.LENGTH_SHORT).show();
                                                          Intent intent = new Intent(Contact_details.this, List_contact.class);
                                                          intent.putExtra("fromButton", false);
                                                          startActivity(intent);
                                                      } else {
                                                          Toast.makeText(Contact_details.this, "Failed!", Toast.LENGTH_SHORT).show();
                                                      }
                                                  }
                                              });
                                          }
        });

        edit_btn.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent(Contact_details.this, Edit_contact.class);
                        intent.putExtra("nom", c.getNom());
                        intent.putExtra("prenom", c.getPrenom());
                        intent.putExtra("email", c.getEmail());
                        intent.putExtra("tel", c.getTel());
                        intent.putExtra("service", c.getService());
                        intent.putExtra("position", position);
                        startActivity(intent);

                    }
                });

        call = findViewById(R.id.call);
        call.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent callIntent = new Intent(Intent.ACTION_DIAL);
                callIntent.setData(Uri.parse("tel:" + c.getTel()));
                startActivity(callIntent);
            }
        });
        Glide.with(this)
                .asBitmap()
                .load(c.getImgUrl())
                .into(img);
    }
}
