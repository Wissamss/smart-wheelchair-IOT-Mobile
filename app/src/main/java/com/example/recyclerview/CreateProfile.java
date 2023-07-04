package com.example.recyclerview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

public class CreateProfile extends AppCompatActivity {

    private static final int PICK_IMAGE_REQUEST = 1;
    Button addBtn, uploadBtn;
    EditText nom, prenom, tel, adresse, dateNai;
    Profile c;
    Uri imgUri;
    ImageView img;
    DocumentReference db ;
    StorageReference ref;
    static String downloadUrl;
    String txt_tel;
    FirebaseUser user;



    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_profile);
        user = FirebaseAuth.getInstance().getCurrentUser();
        db = FirebaseFirestore.getInstance().collection("users").document(user.getEmail());
        ref = FirebaseStorage.getInstance().getReference("pictures");
        addBtn = findViewById(R.id.add);
        uploadBtn = findViewById(R.id.upload);
        nom = findViewById(R.id.nom);
        prenom = findViewById(R.id.prenom);
        tel = findViewById(R.id.tel);
        dateNai = findViewById(R.id.dateNai);
        adresse = findViewById(R.id.adresse);
        img = findViewById(R.id.img);
        addBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String txt_nom = nom.getText().toString();
                String txt_prenom = prenom.getText().toString();
                txt_tel = tel.getText().toString();
                String txt_adresse = adresse.getText().toString();
                String txt_dateNai = dateNai.getText().toString();
                uploadImage();
                ref.child(txt_tel).getDownloadUrl()
                        .addOnSuccessListener(new OnSuccessListener<Uri>() {
                            @Override
                            public void onSuccess(Uri uri) {
                                downloadUrl = uri.toString();
                                c = new Profile(txt_nom, txt_prenom, txt_adresse, txt_tel, txt_dateNai, downloadUrl);
                                db.collection("profile").document(c.getTel()).set(c).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if(task.isSuccessful()){
                                            Toast.makeText(CreateProfile.this, "Profile created", Toast.LENGTH_SHORT).show();
                                            startActivity(new Intent(CreateProfile.this, ShowProfile.class));

                                        }else{
                                            Toast.makeText(CreateProfile.this, "Profile not created", Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });

                            }
                        })
                        .addOnFailureListener(new OnFailureListener() {
                            @Override
                            public void onFailure(@NonNull Exception e) {
                                Toast.makeText(CreateProfile.this, "image not available", Toast.LENGTH_SHORT).show();
                            }
                        });
            }
        });

        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();
            }
        });

    }

    private void uploadImage() {
        if (imgUri != null){
            StorageReference fileRef = ref.child(txt_tel);
            fileRef.putFile(imgUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Toast.makeText(CreateProfile.this,"Upload successed",Toast.LENGTH_SHORT).show();
                        }

                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(CreateProfile.this,"Upload failed",Toast.LENGTH_SHORT).show();
                        }
                    });
        }
    }

    private void openFileChooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null) {
            imgUri = data.getData();
            img = findViewById(R.id.img);
            Glide.with(this)
                    .asBitmap()
                    .load(imgUri)
                    .into(img);
        }
    }
    private String getFileExtension(Uri uri) {
        ContentResolver cR = getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));
    }
}