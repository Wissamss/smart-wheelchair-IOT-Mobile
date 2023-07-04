package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MvtControl extends AppCompatActivity {
    FirebaseAuth auth;
    DatabaseReference databaseRef;
    DocumentReference db1;
    Button start, stop, left, right;
    ImageView speak;

    private ProgressBar progressB;
    private TextView progressT;
    FirebaseUser user;
    DocumentReference db;
    String phone;

    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvt_control);
        auth = FirebaseAuth.getInstance();
        user = FirebaseAuth.getInstance().getCurrentUser();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        db = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        start = findViewById(R.id.start);
        stop = findViewById(R.id.stop);
        left = findViewById(R.id.left);
        right = findViewById(R.id.right);
        speak = findViewById(R.id.speak);
        speak.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MvtControl.this, MvtControlVoice.class));
            }
        });
        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        phone = document.getString("phone");
                        databaseRef.child(phone).child("battery").setValue(100);
                    }
                }
            }
        });

        start.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création de la variable "start" avec une valeur de 0 dans la base de données Firebase
                databaseRef.child(phone).child("mouvement").child("commande").setValue(4);
            }
        });

        stop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création de la variable "start" avec une valeur de 0 dans la base de données Firebase
                databaseRef.child(phone).child("mouvement").child("commande").setValue(0);
            }
        });

        left.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création de la variable "start" avec une valeur de 0 dans la base de données Firebase
                databaseRef.child(phone).child("mouvement").child("commande").setValue(1);
            }
        });

        right.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Création de la variable "start" avec une valeur de 0 dans la base de données Firebase
                databaseRef.child(phone).child("mouvement").child("commande").setValue(3);
            }
        });
        progressB = findViewById(R.id.progressB);
        progressT = findViewById(R.id.progressT);
        db1 = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());

        db1.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        String phone = document.getString("phone");
                        databaseRef = FirebaseDatabase.getInstance().getReference("users");
                        databaseRef.child(phone).get().addOnCompleteListener(new OnCompleteListener<DataSnapshot>() {
                            @Override
                            public void onComplete(@NonNull Task<DataSnapshot> task) {
                                if (task.isSuccessful()) {
                                    DataSnapshot dataSnapshot = task.getResult();
                                    if (dataSnapshot.exists()) {
                                        int level = dataSnapshot.child("battery").getValue(Integer.class);
                                        int progression = calculateProgression(level);
                                        progressB.setProgress(progression);
                                        progressT.setText(String.valueOf(progression + "%"));
                                    }
                                }
                            }
                        });

                    } else {
                        // User document does not exist
                        Toast.makeText(MvtControl.this, "There's no battery detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error occurred while fetching the user document
                    Exception exception = task.getException();
                    // TODO: Handle the error case
                }
            }
        });




    }
    private int calculateProgression(int level) {
        int maxDataValue = 100;
        int progression = (int) ((double) level / maxDataValue * 100);
        return progression;
    };

}
