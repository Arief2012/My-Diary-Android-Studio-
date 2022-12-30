package com.pmo.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class ProfilActivity extends AppCompatActivity {

    private TextView tvwelcome, tvnama, tvalamat, tvnotlp, tvtmplahir, tvtgllahir, tvgender, tvemail;
    private ProgressBar progressBar;
    private String nama, alamat, notlp, tmplahir, tgllahir, gender, email;
    private ImageView imageView;
    private FirebaseAuth authprofil;

    BottomNavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);
        //ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        tvwelcome = findViewById(R.id.tvProfil_welcome);
        tvnama = findViewById(R.id.tvProfil_nama);
        tvalamat = findViewById(R.id.tvProfil_alamat);
        tvnotlp = findViewById(R.id.tvProfil_no);
        tvtmplahir = findViewById(R.id.tvPofil_tmplahir);
        tvtgllahir = findViewById(R.id.tvProfil_tgllahir);
        tvgender = findViewById(R.id.tvProfil_gender);
        tvemail = findViewById(R.id.tvProfil_email);
        progressBar = findViewById(R.id.progressbar_profil);

//        Image
        imageView = findViewById(R.id.imageProfil);
        imageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ProfilActivity.this, UploadImageActivity.class);
                startActivity(intent);
            }
        });

        authprofil = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = authprofil.getCurrentUser();

        if (firebaseUser == null) {
            Toast.makeText(this, "Profil tidak tersedia saat ini", Toast.LENGTH_LONG).show();
        } else {
            progressBar.setVisibility(View.VISIBLE);
            showUserProfil(firebaseUser);
        }

        navigationView = findViewById(R.id.botom_nav);
        navigationView.setSelectedItemId(R.id.nav_profil);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(ProfilActivity.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.nav_tambah:
                        startActivity(new Intent(ProfilActivity.this, tambahActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.nav_profil:

                        return true;

                    case R.id.nav_logout:
                        FirebaseAuth.getInstance().signOut();
                        startActivity(new Intent(getApplicationContext(), LoginActivity.class));
                        finish();
                        return true;
                }

                return false;
            }
        });
    }

    private void showUserProfil(FirebaseUser firebaseUser) {
        String userID = firebaseUser.getUid();

        DatabaseReference referenceprofil = FirebaseDatabase.getInstance().getReference("Data User");
        referenceprofil.child(userID).addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                ReadWriteDataUser readDataUser;
                readDataUser = snapshot.getValue(ReadWriteDataUser.class);
                if (readDataUser != null) {
                    nama = firebaseUser.getDisplayName();
                    alamat = readDataUser.alamat;
                    notlp = readDataUser.notlp;
                    tmplahir = readDataUser.tempatlahir;
                    tgllahir = readDataUser.tanggallahir;
                    gender = readDataUser.gender;
                    email = firebaseUser.getEmail();

                    tvwelcome.setText("Hello " + nama);
                    tvnama.setText(nama);
                    tvalamat.setText(alamat);
                    tvnotlp.setText(notlp);
                    tvtmplahir.setText(tmplahir);
                    tvtgllahir.setText(tgllahir);
                    tvgender.setText(gender);
                    tvemail.setText(email);

                    Uri uri = firebaseUser.getPhotoUrl();

                    Picasso.with(ProfilActivity.this).load(uri).into(imageView);
                }else{
                    Toast.makeText(ProfilActivity.this, "Foto profil eror", Toast.LENGTH_SHORT).show();
                }
                progressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ProfilActivity.this, "Profil eror", Toast.LENGTH_SHORT).show();
                progressBar.setVisibility(View.GONE);
            }
        });
    }

    boolean doubleBackToExitPressedOnce = false;

    @Override
    public void onBackPressed() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed();
            return;
        }

        this.doubleBackToExitPressedOnce = true;
        Toast.makeText(this, "Please click BACK again to exit", Toast.LENGTH_SHORT).show();

        new Handler(Looper.getMainLooper()).postDelayed(new Runnable() {

            @Override
            public void run() {
                doubleBackToExitPressedOnce=false;
            }
        }, 2000);
    }
}