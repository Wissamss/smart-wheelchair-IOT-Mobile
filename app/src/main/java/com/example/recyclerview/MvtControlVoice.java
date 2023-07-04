package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.speech.RecognizerIntent;
import android.view.View;
import android.widget.TextView;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

public class MvtControlVoice extends AppCompatActivity {

    TextView textView;
    FirebaseAuth auth;
    String variable;
    DocumentReference db;
    FirebaseUser user;
    DatabaseReference databaseRef;
    String phone;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvt_control_voice);
        auth = FirebaseAuth.getInstance();
        textView = findViewById(R.id.textView);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        databaseRef = FirebaseDatabase.getInstance().getReference("users");
        db.get().addOnCompleteListener(new OnCompleteListener<DocumentSnapshot>() {
            @Override
            public void onComplete(@NonNull Task<DocumentSnapshot> task) {
                if (task.isSuccessful()) {
                    DocumentSnapshot document = task.getResult();
                    if (document.exists()) {
                        phone = document.getString("phone");
                    }
                }
            }
        });
    }

    public void speak(View view) {
        Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
        intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, "en-US"); // Ajoutez cette ligne pour détecter la langue anglaise

        intent.putExtra(RecognizerIntent.EXTRA_PROMPT, "Start Speaking");
        startActivityForResult(intent, 100);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        auth = FirebaseAuth.getInstance();
        databaseRef = FirebaseDatabase.getInstance().getReference("users");

        if(requestCode == 100 && resultCode == RESULT_OK){

            variable= data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS).get(0);

            textView.setText(variable);
            if (variable.equals("go")) {
                databaseRef.child(phone).child("mouvement").child("commande").setValue(4);
            }
            if (variable.equals("left")) {
                databaseRef.child(phone).child("mouvement").child("commande").setValue(3);
            }
            if (variable.equals("right")) {
                databaseRef.child(phone).child("mouvement").child("commande").setValue(1);
            }
            if (variable.equals("stop")) {
                databaseRef.child(phone).child("mouvement").child("commande").setValue(0);
            }

        }
    }
}