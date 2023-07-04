package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.ArrayList;

public class ShowProfile extends AppCompatActivity {

    private RecyclerView contactRecView;
    FirebaseAuth auth;
    MyAdapter3 myAdapter;
    FloatingActionButton fab_add;
    FloatingActionButton edit_btn;
    FloatingActionButton delete_btn;
    //ArrayList<Contact> contacts ;

    ArrayList<Profile> profiles ;
    DocumentReference db;
    FirebaseUser user;
    int position;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_profile);
        contactRecView = findViewById(R.id.recyclerview);
        profiles = new ArrayList<>();
        user = FirebaseAuth.getInstance().getCurrentUser();
        edit_btn = findViewById(R.id.edit);
        delete_btn = findViewById(R.id.delete);
        db = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        db.collection("profile").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    if(task.getResult().isEmpty()){
                        // Si la collection est vide, rediriger vers Create_Profile
                        startActivity(new Intent(ShowProfile.this, CreateProfile.class));
                        finish();
                        return;
                    }

                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Profile c = new Profile(doc.get("nom").toString(), doc.get("prenom").toString(), doc.get("adresse").toString(), doc.get("tel").toString(), doc.get("dateNai").toString(), doc.get("imgUrl").toString());
                        profiles.add(c);
                        Log.d("cloud", doc.getId()+" "+doc.getData());

                        // ----------------------------------------------------------
                        edit_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                editData(c.getTel(),c.getNom(),c.getPrenom(), c.getAdresse(), c.getDateNai());

                            }
                        });
                        //--------------------------------------------------
                        delete_btn.setOnClickListener(new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {
                                deleteData(c.getTel());
                            }
                        });


                    }
                    myAdapter = new MyAdapter3(profiles, ShowProfile.this);
                    myAdapter.notifyDataSetChanged();
                    contactRecView.setAdapter(myAdapter);
                    contactRecView.setLayoutManager(new LinearLayoutManager(ShowProfile.this));

                }
            }
        });

    }
    private void editData(String id, String nom, String prenom, String adresse, String dateNai){
        int position= 1;
        Intent intent = new Intent(ShowProfile.this, EditProfile.class);
        intent.putExtra("nom",nom);
        intent.putExtra("prenom", prenom);
        intent.putExtra("adresse", adresse);
        intent.putExtra("tel", id);
        intent.putExtra("dateNai", dateNai);
        intent.putExtra("position", position);
        startActivity(intent);
    }

    private void deleteData(String id){
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        db.collection("profile").document(id).delete().addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(ShowProfile.this, "Profile deleted", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(ShowProfile.this, MainActivity.class));
                } else {
                    Toast.makeText(ShowProfile.this, "Failed!", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }


    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.em_contacts:
                startActivity(new Intent(ShowProfile.this, List_contact.class));
                return true;
            case R.id.profile:
                startActivity(new Intent(ShowProfile.this, ShowProfile.class));
                return true;
            case R.id.logout:
                auth.signOut();
                Toast.makeText(ShowProfile.this,"LoggedOut",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(ShowProfile.this, Login.class));
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

}