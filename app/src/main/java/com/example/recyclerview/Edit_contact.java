package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.HashMap;
import java.util.Map;

public class Edit_contact extends AppCompatActivity {

    EditText nom, prenom, tel, email, service;
    //ImageView img;
    String txt_nom, txt_prenom, txt_tel, txt_email, txt_service;
    //Uri imageUrl;
    String nomEdit, prenomEdit, emailEdit, serviceEdit, telEdit;
    Button updateBtn;
    DocumentReference db;
    FirebaseUser user;
    int position;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_contact);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        nom = findViewById(R.id.contact_nom);
        prenom = findViewById(R.id.contact_prenom);
        email = findViewById(R.id.contact_email);
        tel = findViewById(R.id.contact_tel);
        service = findViewById(R.id.contact_service);
        //img = findViewById(R.id.img);
        updateBtn = findViewById(R.id.update);
        txt_nom = (String) getIntent().getSerializableExtra("nom");
        txt_prenom = (String) getIntent().getSerializableExtra("prenom");
        txt_tel = (String) getIntent().getSerializableExtra("tel");
        txt_email = (String) getIntent().getSerializableExtra("email");
        txt_service = (String) getIntent().getSerializableExtra("service");
        //imageUrl = (Uri) getIntent().getSerializableExtra("imageUrl");
        position = (int) getIntent().getSerializableExtra("position");
        nom.setText(txt_nom);
        prenom.setText(txt_prenom);
        email.setText(txt_email);
        tel.setText(txt_tel);
        service.setText(txt_service);
        //img.setImageURI(imageUrl);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomEdit = nom.getText().toString();
                prenomEdit = prenom.getText().toString();
                emailEdit = email.getText().toString();
                telEdit = tel.getText().toString();
                serviceEdit = service.getText().toString();
                if (!TextUtils.isEmpty(nomEdit) && !TextUtils.isEmpty(prenomEdit) && !TextUtils.isEmpty(telEdit) && !TextUtils.isEmpty(emailEdit) && !TextUtils.isEmpty(serviceEdit) ) {
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("nom", nomEdit);
                    updates.put("prenom", prenomEdit);
                    updates.put("email", emailEdit);
                    updates.put("tel", telEdit);
                    db.collection("emergency-contacts").document(txt_tel).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(Edit_contact.this, "Updated", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(Edit_contact.this, List_contact.class);
                                intent.putExtra("fromButton", false);
                                startActivity(intent);
                            }else{
                                Toast.makeText(Edit_contact.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
                }
            });
        }
}