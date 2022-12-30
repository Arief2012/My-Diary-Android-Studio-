package com.pmo.mydiary;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;

public class tambahActivity extends AppCompatActivity {
    EditText edTanggal, edJudul, edCatatan;
    CheckBox cbPagi, cbSiang, cbMalam;
    Spinner spKategori;
    Button btn_simpan;
    Calendar calendar;
    RadioGroup radioGroup;
    RadioButton radioHigh, radioMedium, radioLow;
    BottomNavigationView navigationView;


    //    final  int KodeGallery = 100, KodeKamera = 99;
    DatePickerDialog.OnDateSetListener date;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_tambah);
        //ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);


        navigationView = findViewById(R.id.botom_nav);
        navigationView.setSelectedItemId(R.id.nav_tambah);

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        startActivity(new Intent(tambahActivity.this, MainActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.nav_tambah:
                        return true;

                    case R.id.nav_profil:
                        startActivity(new Intent(tambahActivity.this, ProfilActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
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


        edTanggal = findViewById(R.id.edtanggal);
        edJudul = findViewById(R.id.edjudul);
        cbPagi = findViewById(R.id.checboxPagi);
        cbSiang = findViewById(R.id.checboxSiang);
        cbMalam = findViewById(R.id.checkboxMalam);
        spKategori = findViewById(R.id.spkategori);
        edCatatan = findViewById(R.id.edcatatan);
        radioGroup = findViewById(R.id.rbGrup);
        radioHigh = findViewById(R.id.rbhigh);
        radioMedium = findViewById(R.id.rbmedium);
        radioLow = findViewById(R.id.rblow);
        btn_simpan = findViewById(R.id.btn_simpan);

        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String tanggal = edTanggal.getText().toString();
//                checkbox
                String pagi = null;
                if (cbPagi.isChecked()) {
                    pagi = cbPagi.getText().toString();
                }
                String siang = null;
                if (cbSiang.isChecked()) {
                    siang = cbSiang.getText().toString();
                }
                String malam = null;
                if (cbMalam.isChecked()) {
                    malam = cbMalam.getText().toString();
                }
//                radiobutton
                String rbgrup = null;
                if (radioHigh.isChecked()) {
                    rbgrup = radioHigh.getText().toString();
                }
                if (radioMedium.isChecked()) {
                    rbgrup = radioMedium.getText().toString();
                }
                if (radioLow.isChecked()) {
                    rbgrup = radioLow.getText().toString();
                }
//                spinner
                String kategori = spKategori.getSelectedItem().toString();
                String judul = edJudul.getText().toString();
                String catatan = edCatatan.getText().toString();


                if (tanggal.isEmpty()) {
                    edTanggal.setError("Masukan Tanggal");
                } else if (kategori.equalsIgnoreCase("Pilih Kategori")) {
                    Toast.makeText(tambahActivity.this, "anda belum memilih Kategori", Toast.LENGTH_SHORT).show();
                    spKategori.requestFocusFromTouch();
                } else if (judul.isEmpty()) {
                    edJudul.setError("Masukan Title");
                } else if (catatan.isEmpty()) {
                    edCatatan.setError("Isi catatan anda");
                } else {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    DatabaseReference referenceprofil = FirebaseDatabase.getInstance().getReference("Data User");

                    referenceprofil.child(firebaseUser.getUid()).child("Kegiatan").push().setValue(new ModelActivity(tanggal, pagi, siang, malam, rbgrup, kategori, judul, catatan)).addOnSuccessListener(new OnSuccessListener<Void>() {
                        @Override
                        public void onSuccess(Void unused) {
                            Toast.makeText(tambahActivity.this, "Catatan anda berhasil disimpan", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(tambahActivity.this, MainActivity.class));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(tambahActivity.this, "Catatan anda gagal disimpan ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });

        calendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int montOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, montOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                EditText tanggal = findViewById(R.id.edtanggal);
                String myFormat = "dd/MMMM/yyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                tanggal.setText(sdf.format(calendar.getTime()));

            }
        };
        edTanggal.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(tambahActivity.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(calendar.MONTH),
                        calendar.get(calendar.DAY_OF_MONTH)).show();
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
