package com.pmo.mydiary;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.Selection;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Locale;

public class DialogForm extends DialogFragment {
    String tanggal, pagi, siang, malam, radiopilihan, kategori, judul, catatan, key, pilih;
    DatabaseReference database = FirebaseDatabase.getInstance().getReference();

    public DialogForm(String tanggal, String pagi, String siang, String malam, String radiopilihan, String kategori, String judul, String catatan, String key, String pilih) {
        this.tanggal = tanggal;
        this.pagi = pagi ;
        this.siang = siang;
        this.malam = malam;
        this.radiopilihan = radiopilihan;
        this.kategori = kategori;
        this.judul = judul;
        this.catatan = catatan;
        this.key = key;
        this.pilih = pilih;

    }

    EditText ttanggal, tjudul, tcatatan;
    Spinner tkategori;
    Button btn_simpan;
    CheckBox cbPagi, cbSiang, cbMalam;
    RadioButton radiohigh, radiomedium, radiolow;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        final View view = inflater.inflate(R.layout.activity_update, container, false);
        ttanggal = view.findViewById(R.id.edtanggal);
        cbPagi = view.findViewById(R.id.checboxPagi);
        cbSiang = view.findViewById(R.id.checboxSiang);
        cbMalam = view.findViewById(R.id.checkboxMalam);
        radiohigh = view.findViewById(R.id.rbhigh);
        radiomedium = view.findViewById(R.id.rbmedium);
        radiolow = view.findViewById(R.id.rblow);
        tkategori = view.findViewById(R.id.spkategori);
        tjudul = view.findViewById(R.id.edjudul);
        tcatatan = view.findViewById(R.id.edcatatan);
        btn_simpan = view.findViewById(R.id.btn_simpan);

        ttanggal.setText(tanggal);

        if (pagi != null && pagi.equalsIgnoreCase("Pagi")) {
            cbPagi.setChecked(true);
        }
        if (siang != null && siang.equalsIgnoreCase("Siang")) {
            cbSiang.setChecked(true);
        }
        if (malam != null && malam.equalsIgnoreCase("Malam")) {
            cbMalam.setChecked(true);
        }

        if(radiopilihan.equals("Highest"))
            radiohigh.setChecked(true);
        if(radiopilihan.equals("Medium"))
            radiomedium.setChecked(true);
        if(radiopilihan.equals("Lowest"))
            radiolow.setChecked(true);

        String[] setkategori = getResources().getStringArray(R.array.Kategori);
        tkategori.setSelection(Arrays.asList(setkategori).indexOf(kategori));
        tjudul.setText(judul);
        tcatatan.setText(catatan);
        btn_simpan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String tanggal = ttanggal.getText().toString();
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
                String rbgrup = null;
                if (radiohigh.isChecked()) {
                    rbgrup = radiohigh.getText().toString();
                } else if (radiomedium.isChecked()) {
                    rbgrup = radiomedium.getText().toString();
                } else if (radiolow.isChecked()) {
                    rbgrup = radiolow.getText().toString();
                }
                String kategori = tkategori.getSelectedItem().toString();
                String judul = tjudul.getText().toString();
                String catatan = tcatatan.getText().toString();

                if (pilih.equals("ubah")) {
                    FirebaseAuth auth = FirebaseAuth.getInstance();
                    FirebaseUser firebaseUser = auth.getCurrentUser();
                    DatabaseReference referenceprofil = FirebaseDatabase.getInstance().getReference("Data User");

                    referenceprofil.child(firebaseUser.getUid()).child("Kegiatan").child(key)
                            .setValue(new ModelActivity(tanggal, pagi, siang, malam, rbgrup, kategori, judul, catatan))
                            .addOnSuccessListener(new OnSuccessListener<Void>() {
                                @Override
                                public void onSuccess(Void unused) {
                                    Toast.makeText(view.getContext(), "Berhasil di Update data", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(getActivity(), MainActivity.class));

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(view.getContext(), "gagal update data ", Toast.LENGTH_SHORT).show();
                        }
                    });
                }

            }
        });


        return view;
    }


    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            dialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

        }
    }
}
