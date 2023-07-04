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
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

public class EditProfile extends AppCompatActivity {

    EditText nom, prenom, tel, adresse, dateNai;
    //ImageView img;
    String txt_nom, txt_prenom, txt_tel, txt_adresse, txt_dateNai;
    //Uri imageUrl;
    String nomEdit, prenomEdit, adresseEdit, dateNaiEdit, telEdit;
    Button updateBtn;
    DocumentReference db;
    FirebaseUser user;
    int position;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        nom = findViewById(R.id.contact_nom);
        prenom = findViewById(R.id.contact_prenom);
        adresse = findViewById(R.id.contact_adresse);
        tel = findViewById(R.id.contact_tel);
        dateNai = findViewById(R.id.contact_dateNai);
        //img = findViewById(R.id.img);
        updateBtn = findViewById(R.id.update);
        txt_nom = (String) getIntent().getSerializableExtra("nom");
        txt_prenom = (String) getIntent().getSerializableExtra("prenom");
        txt_tel = (String) getIntent().getSerializableExtra("tel");
        txt_adresse = (String) getIntent().getSerializableExtra("adresse");
        txt_dateNai = (String) getIntent().getSerializableExtra("dateNai");
        //imageUrl = (Uri) getIntent().getSerializableExtra("imageUrl");
        position = (int) getIntent().getSerializableExtra("position");
        nom.setText(txt_nom);
        prenom.setText(txt_prenom);
        adresse.setText(txt_adresse);
        tel.setText(txt_tel);
        dateNai.setText(txt_dateNai);
        //img.setImageURI(imageUrl);
        updateBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nomEdit = nom.getText().toString();
                prenomEdit = prenom.getText().toString();
                adresseEdit = adresse.getText().toString();
                telEdit = tel.getText().toString();
                dateNaiEdit = dateNai.getText().toString();
                if (!TextUtils.isEmpty(nomEdit) && !TextUtils.isEmpty(prenomEdit) && !TextUtils.isEmpty(telEdit) && !TextUtils.isEmpty(adresseEdit) && !TextUtils.isEmpty(dateNaiEdit) ) {
                    Map<String, Object> updates = new HashMap<>();
                    updates.put("nom", nomEdit);
                    updates.put("prenom", prenomEdit);
                    updates.put("adresse", adresseEdit);
                    updates.put("tel", telEdit);
                    updates.put("dateNai", dateNaiEdit);
                    db.collection("profile").document(txt_tel).update(updates).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            if(task.isSuccessful()){
                                Toast.makeText(EditProfile.this, "Updated", Toast.LENGTH_SHORT).show();
                                startActivity(new Intent(EditProfile.this, ShowProfile.class));
                            }else{
                                Toast.makeText(EditProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                            }
                        }
                    });
                }
            }
        });
    }
}