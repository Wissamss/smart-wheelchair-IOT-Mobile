package com.example.recyclerview;

import android.content.Context;
import android.content.DialogInterface;
import android.app.AlertDialog;
import android.location.Location;
import android.location.LocationManager;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import androidx.annotation.NonNull;
import android.Manifest;
import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.core.app.ActivityCompat;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
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
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;


import java.util.Locale;

public class MainActivity extends AppCompatActivity {
    FirebaseAuth auth;
    DocumentReference db1;
    CardView contact,profile,logout,emergency,control;

    private DatabaseReference databaseRef;

    private FusedLocationProviderClient fusedLocationClient;
    private static final int REQUEST_LOCATION = 1;

    private static final int CONTACT_SELECTION_REQUEST = 1;
    //Button sms, monitor;
    LocationManager locationManager;

    String latitude, longitude;


    @SuppressLint("MissingInflatedId")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ActivityCompat.requestPermissions( this,
                new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        ActivityCompat.requestPermissions(MainActivity.this, new String[]{Manifest.permission.SEND_SMS, Manifest.permission.READ_SMS}, PackageManager.PERMISSION_GRANTED);
        auth = FirebaseAuth.getInstance();
        FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
        contact = findViewById(R.id.contact);
        control = findViewById(R.id.control);
        emergency = findViewById(R.id.emergency);
        profile = findViewById(R.id.profile);
        logout = findViewById(R.id.logout);logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                auth.signOut();
                Toast.makeText(MainActivity.this,"LoggedOut",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Login.class));
            }
        });
        profile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this,ShowProfile.class);
                startActivity(intent);
            }
        });
        emergency.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }
            }
        });
        contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(MainActivity.this, List_contact.class);
                intent.putExtra("fromButton", false);
                startActivity(intent);
            }
        });
        control.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MvtControl.class));
            }
        });

        //sms = (Button)findViewById(R.id.contact);
        //monitor =(Button)findViewById(R.id.monitor);
        /*
        sms.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation();
                }


            }
        });

        monitor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                locationManager = (LocationManager) getApplicationContext().getSystemService(Context.LOCATION_SERVICE);
                if (!locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER)) {
                    OnGPS();
                } else {
                    getLocation1();
                }
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
                                if(task.isSuccessful()){
                                    DataSnapshot dataSnapshot = task.getResult();
                                    if(dataSnapshot.exists()){
                                        int level = dataSnapshot.child("battery").getValue(Integer.class);
                                        int progression = calculateProgression(level);
                                        progressB.setProgress(progression);
                                        progressT.setText(String.valueOf(progression+"%"));
                                    }
                                }
                            }
                        });

                    } else {
                        // User document does not exist
                        Toast.makeText(MainActivity.this, "There's no battery detected", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    // Error occurred while fetching the user document
                    Exception exception = task.getException();
                    // TODO: Handle the error case
                }
            }
        });

         */



    }
    /*
    private int calculateProgression(int level) {
        int maxDataValue = 100;
        int progression = (int) ((double) level / maxDataValue * 100);
        return progression;
    };
     */

    private void OnGPS() {
        final AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Enable GPS").setCancelable(false).setPositiveButton("Yes", new  DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                startActivity(new Intent(Settings.ACTION_LOCATION_SOURCE_SETTINGS));
            }
        }).setNegativeButton("No", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();
            }
        });
        final AlertDialog alertDialog = builder.create();
        alertDialog.show();
    }
    private void getLocation() {
        if (ActivityCompat.checkSelfPermission(
                MainActivity.this,Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                MainActivity.this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            Location LocationGPS = locationManager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
            Location LocationNetwork = locationManager.getLastKnownLocation(LocationManager.NETWORK_PROVIDER);
            Location LocationPassive = locationManager.getLastKnownLocation(LocationManager.PASSIVE_PROVIDER);
            if (LocationGPS != null) {
                double lat = LocationGPS.getLatitude();
                double longi = LocationGPS.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                openListActivity(latitude, longitude);
            }
            else if(LocationNetwork != null){
                double lat = LocationNetwork.getLatitude();
                double longi = LocationNetwork.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                openListActivity(latitude, longitude);
            }
            else if(LocationPassive !=null){
                double lat = LocationPassive.getLatitude();
                double longi = LocationPassive.getLongitude();
                latitude = String.valueOf(lat);
                longitude = String.valueOf(longi);
                openListActivity(latitude, longitude);
            }
            else {
                Toast.makeText(this, "Unable to find location.", Toast.LENGTH_SHORT).show();
            }
        }
    }

    private void openListActivity(String latitude, String longitude) {
        Intent intent = new Intent(MainActivity.this, List_contact.class);
        intent.putExtra("fromButton", true);
        intent.putExtra("lat", latitude);
        intent.putExtra("longi", longitude);
        startActivity(intent);
    }
    /*
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case R.id.em_contacts:
                Intent intent = new Intent(MainActivity.this, List_contact.class);
                intent.putExtra("fromButton", false);
                startActivity(intent);
                return true;
            case R.id.profile:
                Intent intentt = new Intent(MainActivity.this,ShowProfile.class);
                startActivity(intentt);
                return true;
            case R.id.logout:
                auth.signOut();
                Toast.makeText(MainActivity.this,"LoggedOut",Toast.LENGTH_SHORT).show();
                startActivity(new Intent(MainActivity.this, Login.class));
                return true;
            case R.id.mvtcontrol:
                startActivity(new Intent(MainActivity.this, MvtControl.class));
            case R.id.mvtcontrolvoice:
                startActivity(new Intent(MainActivity.this, MvtControlVoice.class));
            default:
                return super.onOptionsItemSelected(item);
        }
    }
    */

    private void getLocation1() {
        // Initialize the FusedLocationProviderClient
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        // Create a location request
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // Update location every 5 seconds

        // Check for location permission
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            return;
        }

        // Request location updates
        fusedLocationClient.requestLocationUpdates(locationRequest, new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                // Retrieve the most recent location
                Location location = locationResult.getLastLocation();

                if (location != null) {
                    double latitude = location.getLatitude();
                    double longitude = location.getLongitude();

                    String uri = String.format(Locale.ENGLISH, "geo:%f,%f", latitude, longitude);
                    Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(uri));
                    startActivity(intent);
                } else {
                    Toast.makeText(MainActivity.this, "Unable to retrieve location", Toast.LENGTH_SHORT).show();
                }
            }
        }, null);
    }

}
