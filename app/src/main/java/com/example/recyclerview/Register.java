package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.button.MaterialButton;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;

public class Register extends AppCompatActivity {
    EditText email, pwd, confirmPwd, fullName, phone;

    MaterialButton register;
    TextView login;
    FirebaseAuth auth;
    CollectionReference db;
    User user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users");
        email = findViewById(R.id.email);
        fullName = findViewById(R.id.fullName);
        confirmPwd = findViewById(R.id.confirmPwd);
        phone = findViewById(R.id.phone);
        pwd = findViewById(R.id.pwd);
        login = findViewById(R.id.login);
        register= findViewById(R.id.register);
        auth = FirebaseAuth.getInstance();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(Register.this, Login.class));
            }
        });

        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_email = email.getText().toString();
                String txt_pwd = pwd.getText().toString();
                register(txt_email, txt_pwd);
            }
        });
    }

    private void register(String email, String pwd) {
        String txt_fullName = fullName.getText().toString();
        String txt_phone = phone.getText().toString();
        String txt_pwd = pwd.toString();
        String txt_confirm = confirmPwd.getText().toString();
        if (TextUtils.isEmpty(pwd) || pwd.length() < 8) {
            Toast.makeText(Register.this, "Password must be at least 8 characters", Toast.LENGTH_SHORT).show();
            return; // Return from the method to prevent registration
        }

        if (!txt_pwd.equals(txt_confirm)) {
            Toast.makeText(Register.this, "Passwords do not match", Toast.LENGTH_SHORT).show();
            return; // Return from the method to prevent registration
        }

        if (TextUtils.isEmpty(txt_phone) || !isValidPhoneNumber(txt_phone)) {
            Toast.makeText(Register.this, "Invalid phone number", Toast.LENGTH_SHORT).show();
            return; // Return from the method to prevent registration
        }
     auth.createUserWithEmailAndPassword(email, pwd)
             .addOnCompleteListener(new OnCompleteListener<AuthResult>() {
         @Override
         public void onComplete(@NonNull Task<AuthResult> task) {
             if(task.isSuccessful()){
                 user = new User(txt_fullName,txt_phone,email, pwd);
                 db.document(email).set(user).addOnCompleteListener(new OnCompleteListener<Void>() {
                     @Override
                     public void onComplete(@NonNull Task<Void> task) {
                         if(task.isSuccessful()){
                             Toast.makeText(Register.this,"Registration is successful",Toast.LENGTH_SHORT).show();
                             startActivity(new Intent(Register.this, Login.class));
                             finish();
                         }
                         else{
                             Toast.makeText(Register.this,"Registration is unsuccessful",Toast.LENGTH_SHORT).show();
                         }
                     }
                 });

             }

         }
     });

    }
    private boolean isValidPhoneNumber(String phoneNumber) {
        // You can implement your phone number validation logic here
        // For example, you can use a regular expression to validate the format

        // Dummy validation: Check if the phone number has exactly 10 digits
        return phoneNumber.length() == 10;
    }
}