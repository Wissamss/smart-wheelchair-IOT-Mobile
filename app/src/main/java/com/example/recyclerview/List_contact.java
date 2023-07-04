package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ListView;
import android.widget.SearchView;
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
import java.util.Locale;

public class List_contact extends AppCompatActivity {

    private RecyclerView contactRecView;
    SearchView search;
    FirebaseAuth auth;
    MyAdapter2 myAdapter;
    FloatingActionButton fab_add;
    ArrayList<Contact> contacts ;
    DocumentReference db;
    FirebaseUser user;
    boolean isFromButton;
    String longi, lat;
    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_contact);
        contactRecView = findViewById(R.id.recyclerview);
        contacts = new ArrayList<>();
        isFromButton = (boolean) getIntent().getSerializableExtra("fromButton");
        lat = (String) getIntent().getSerializableExtra("lat");
        longi = (String) getIntent().getSerializableExtra("longi");
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        db.collection("emergency-contacts").get().addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
            @Override
            public void onComplete(@NonNull Task<QuerySnapshot> task) {
                if(task.isSuccessful()){
                    for(QueryDocumentSnapshot doc : task.getResult()){
                        Contact c = new Contact(doc.get("nom").toString(), doc.get("prenom").toString(), doc.get("email").toString(), doc.get("tel").toString(), doc.get("service").toString(), doc.get("imgUrl").toString());
                        contacts.add(c);
                        Log.d("cloud", doc.getId()+" "+doc.getData());
                    }
                    myAdapter = new MyAdapter2(contacts, List_contact.this);
                    myAdapter.setOnItemClickListener(new MyAdapter2.OnItemClickListener() {
                        @Override
                        public void onItemClick(int position) {
                            Contact selectedContact = contacts.get(position);
                            if (isFromButton) {
                                String url = String.format(Locale.getDefault(), "https://maps.google.com/?q=%s,%s", lat, longi);
                                SmsManager mySmsManager = SmsManager.getDefault();
                                mySmsManager.sendTextMessage(selectedContact.getTel(),null,"HELP!! the addresse is :"+"\n" +url,null,null);
                                Toast.makeText(List_contact.this,"Message sent to "+ selectedContact.getNom()+" "+selectedContact.getPrenom(),Toast.LENGTH_SHORT).show();
                                finish(); // Finish the List_contact activity and return to the main activity
                            }
                            if(!isFromButton){
                                // Handle the action to view contact details
                                Intent intent = new Intent(List_contact.this, Contact_details.class);
                                intent.putExtra("contact", selectedContact);
                                intent.putExtra("position", position);
                                startActivity(intent);

                            }

                        }
                    });
                    contactRecView.setAdapter(myAdapter);
                    contactRecView.setLayoutManager(new LinearLayoutManager(List_contact.this));

                }
            }
        });

        auth = FirebaseAuth.getInstance();

        fab_add = findViewById(R.id.add);
        fab_add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(List_contact.this, Add_contact.class);
                startActivityForResult(intent, 1);
            }
        });

        search= findViewById(R.id.search);
        search.clearFocus();
        search.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                filterText(newText);
                return true;
            }
        });

    }

    private void filterText(String newText) {
      ArrayList<Contact>  filteredContacts = new ArrayList<>();
      for(Contact c : contacts){
          if((c.getNom().toLowerCase()+" "+c.getPrenom().toLowerCase()).contains(newText.toLowerCase())){
            filteredContacts.add(c);
          }
        }
      if(filteredContacts.isEmpty()){
          Toast.makeText(this, "Not found", Toast.LENGTH_SHORT).show();
      }else{
          myAdapter.setFilteredContacts(filteredContacts);
      }

    }



}