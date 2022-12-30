package com.pmo.mydiary;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.ImageView;
import android.widget.SearchView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.app.AppCompatDelegate;
import androidx.appcompat.widget.SwitchCompat;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.gms.ads.AdRequest;
import com.google.android.gms.ads.AdView;
import com.google.android.gms.ads.MobileAds;
import com.google.android.gms.ads.initialization.InitializationStatus;
import com.google.android.gms.ads.initialization.OnInitializationCompleteListener;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    private FirebaseUser firebaseUser;
    private TextView textyourname;


    AdapterActivity adapter_activity;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();
    ArrayList<ModelActivity> mlist;
    RecyclerView tv_tampil;

    BottomNavigationView navigationView;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
//ads
        MobileAds.initialize(this, new OnInitializationCompleteListener() {
            @Override
            public void onInitializationComplete(InitializationStatus initializationStatus) {
            }
        });

        AdView mAdView = findViewById(R.id.adView);
        AdRequest adRequest = new AdRequest.Builder().build();
        mAdView.loadAd(adRequest);

// double click


        navigationView = findViewById(R.id.botom_nav);
        navigationView.setSelectedItemId(R.id.nav_home);

        textyourname = findViewById(R.id.yourname);
        firebaseUser = FirebaseAuth.getInstance().getCurrentUser();


        if (firebaseUser != null) {
            textyourname.setText("Hello, " + firebaseUser.getDisplayName());
        } else {
            textyourname.setText("Login gagal");
        }

        navigationView.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {

                switch (item.getItemId()) {
                    case R.id.nav_home:
                        return true;

                    case R.id.nav_tambah:
                        startActivity(new Intent(MainActivity.this, tambahActivity.class));
                        overridePendingTransition(0, 0);
                        finish();
                        return true;

                    case R.id.nav_profil:
                        startActivity(new Intent(MainActivity.this, ProfilActivity.class));
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

        tv_tampil = findViewById(R.id.tv_tampil);
        RecyclerView.LayoutManager mLayout = new LinearLayoutManager(this);
        tv_tampil.setLayoutManager(mLayout);
        tv_tampil.setItemAnimator(new DefaultItemAnimator());

        tampilData();
    }

    private void tampilData() {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference referenceprofil = FirebaseDatabase.getInstance().getReference("Data User");

        referenceprofil.child(firebaseUser.getUid()).child("Kegiatan").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    ModelActivity kng = item.getValue(ModelActivity.class);
                    kng.setKey(item.getKey());
                    mlist.add(kng);
                }
                adapter_activity = new AdapterActivity(mlist, MainActivity.this);
                tv_tampil.setAdapter(adapter_activity);

            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.searchmenu, menu);
        MenuItem item = menu.findItem(R.id.search);
        SearchView searchView = (SearchView) item.getActionView();
        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                processeacrh(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                processeacrh(s);
                return false;
            }
        });
        return super.onCreateOptionsMenu(menu);
    }

    private void processeacrh(String s) {
        FirebaseAuth auth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = auth.getCurrentUser();
        DatabaseReference referenceprofil = FirebaseDatabase.getInstance().getReference("Data User");

        referenceprofil.child(firebaseUser.getUid()).child("Kegiatan").orderByChild("spkategori").startAt(s).endAt(s + "\uf8ff").addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mlist = new ArrayList<>();
                for (DataSnapshot item : snapshot.getChildren()) {
                    ModelActivity kng = item.getValue(ModelActivity.class);
                    kng.setKey(item.getKey());
                    mlist.add(kng);
                }
                adapter_activity = new AdapterActivity(mlist, MainActivity.this);
                tv_tampil.setAdapter(adapter_activity);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {

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