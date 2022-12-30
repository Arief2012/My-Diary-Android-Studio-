package com.pmo.mydiary;

import android.app.DatePickerDialog;
import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseAuthInvalidCredentialsException;
import com.google.firebase.auth.FirebaseAuthUserCollisionException;
import com.google.firebase.auth.FirebaseAuthWeakPasswordException;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Locale;


public class RegisterActivity extends AppCompatActivity {
    private EditText editNama, editEmail, editAlamat, editNO, editTempatlahir, editTanggallahir,
            editPassword, editPasswordConf;
    private RadioGroup radioGroupreg;
    private RadioButton radiobuttonregselected;
    Calendar calendar;
    private TextView btnLogin;
    private Button btnRegister;
    private ProgressDialog progressDialog;
    private CheckBox cbSetuju;
    private static final String TAG = "RegisterActivity";

    DatePickerDialog.OnDateSetListener date;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        editNama = findViewById(R.id.edregnama);
        editAlamat = findViewById(R.id.edregalamat);
        editNO = findViewById(R.id.edregno_hp);
        editTempatlahir = findViewById(R.id.edregTmplhr);
        editTanggallahir = findViewById(R.id.edregtanggallhr);
        editEmail = findViewById(R.id.edregemail);
        editPassword = findViewById(R.id.edregpass);
        editPasswordConf = findViewById(R.id.edregpass_conf);
        cbSetuju = findViewById(R.id.checkbox);
        btnRegister = findViewById(R.id.btn_register);
        btnLogin = findViewById(R.id.btn_login);

        radioGroupreg = findViewById(R.id.rbGrupregjk);
        radioGroupreg.clearCheck();

        progressDialog = new ProgressDialog(RegisterActivity.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu");
        progressDialog.setCancelable(false);

        cbSetuju.setOnCheckedChangeListener((compoundButton, b) -> {
            btnRegister.setEnabled(cbSetuju.isChecked());
        });

        btnLogin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });


        btnRegister.setOnClickListener(v -> {

            int selectedGenderId = radioGroupreg.getCheckedRadioButtonId();
            radiobuttonregselected = findViewById(selectedGenderId);

            String textnama = editNama.getText().toString();
            String textalamat = editAlamat.getText().toString();
            String textnotlp = editNO.getText().toString();
            String texttempatlahir = editTempatlahir.getText().toString();
            String texttanggallahir = editTanggallahir.getText().toString();
            String textemail = editEmail.getText().toString();
            String textpass = editPassword.getText().toString();
            String textpassconf = editPasswordConf.getText().toString();

            String textgender;

            if (TextUtils.isEmpty(textnama)) {
                Toast.makeText(this, "Silahkan isi Nama anda", Toast.LENGTH_SHORT).show();
                editNama.setError("Nama belum diisi");
                editNama.requestFocus();
            } else if (TextUtils.isEmpty(textalamat)) {
                Toast.makeText(this, "Silahkan isi Alamat anda", Toast.LENGTH_SHORT).show();
                editAlamat.setError("Alamat belum diisi");
                editAlamat.requestFocus();
            } else if (TextUtils.isEmpty(textnotlp)) {
                Toast.makeText(this, "Silahkan isi No Telp anda", Toast.LENGTH_SHORT).show();
                editNO.setError("No Telp belum diisi");
                editNO.requestFocus();
            } else if (textnotlp.length() < 12) {
                Toast.makeText(this, "No Telp tidak valid", Toast.LENGTH_SHORT).show();
                editNO.setError("NoTelp tidak valid");
                editNO.requestFocus();
            } else if (TextUtils.isEmpty(texttempatlahir)) {
                Toast.makeText(this, "Silahkan isi Tempat Lahir anda", Toast.LENGTH_SHORT).show();
                editTempatlahir.setError("Tempat Lahir belum diisi");
                editTempatlahir.requestFocus();
            } else if (TextUtils.isEmpty(texttanggallahir)) {
                Toast.makeText(this, "Silahkan isi Tanggal Lahir anda", Toast.LENGTH_SHORT).show();
                editTanggallahir.setError("Tanggal Lahir belum diisi");
                editTanggallahir.requestFocus();
            } else if (radioGroupreg.getCheckedRadioButtonId() == -1) {
                Toast.makeText(this, "Silahkan pilih jenis kelamin", Toast.LENGTH_SHORT).show();
                radiobuttonregselected.setError("Jenis kelamin belum dipilih");
                radiobuttonregselected.requestFocus();
            } else if (TextUtils.isEmpty(textemail)) {
                Toast.makeText(this, "Silahkan isi Email anda", Toast.LENGTH_SHORT).show();
                editEmail.setError("Email belum diisi");
                editEmail.requestFocus();
            } else if (!Patterns.EMAIL_ADDRESS.matcher(textemail).matches()) {
                Toast.makeText(this, "please re-enter your email", Toast.LENGTH_SHORT).show();
                editEmail.setError("Valid Email is required");
                editEmail.requestFocus();
            } else if (textpass.length() < 6) {
                Toast.makeText(this, "Password harus 8 digit", Toast.LENGTH_SHORT).show();
                editPassword.setError("Password harus 8 digit");
                editPassword.requestFocus();
            } else if (TextUtils.isEmpty(textpassconf)) {
                Toast.makeText(this, "Konfirmasi Password anda", Toast.LENGTH_SHORT).show();
                editPasswordConf.setError("password konfirmasi gagal");
                editPasswordConf.requestFocus();
            } else if (!textpass.equals(textpassconf)) {
                Toast.makeText(this, "Samakan password anda", Toast.LENGTH_SHORT).show();
                editPasswordConf.setError("password konfirmasi gagal");
                editPasswordConf.requestFocus();
//            clear password
                editPassword.clearComposingText();
                editPasswordConf.clearComposingText();
            } else {
                textgender = radiobuttonregselected.getText().toString();
                register(textnama, textalamat, textnotlp, texttempatlahir, texttanggallahir, textgender, textemail, textpass);
            }
        });

        calendar = Calendar.getInstance();
        date = new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker view, int year, int montOfYear, int dayOfMonth) {
                calendar.set(Calendar.YEAR, year);
                calendar.set(Calendar.MONTH, montOfYear);
                calendar.set(Calendar.DAY_OF_MONTH, dayOfMonth);

                EditText tanggal = findViewById(R.id.edregtanggallhr);
                String myFormat = "dd/MMMM/yyy";
                SimpleDateFormat sdf = new SimpleDateFormat(myFormat, Locale.ENGLISH);
                tanggal.setText(sdf.format(calendar.getTime()));

            }
        };
        editTanggallahir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new DatePickerDialog(RegisterActivity.this, date,
                        calendar.get(Calendar.YEAR),
                        calendar.get(calendar.MONTH),
                        calendar.get(calendar.DAY_OF_MONTH)).show();
            }
        });


    }


    private void register(String textnama, String textalamat, String textnotlp, String texttempatlahir, String texttanggallahir, String textgender, String textemail, String textpass) {

        progressDialog.show();

        FirebaseAuth auth = FirebaseAuth.getInstance();
        auth.createUserWithEmailAndPassword(textemail, textpass).addOnCompleteListener(RegisterActivity.this, new OnCompleteListener<AuthResult>() {
            @Override
            public void onComplete(@NonNull Task<AuthResult> task) {
                if (task.isSuccessful()) {
                    Toast.makeText(RegisterActivity.this, "Registrasi Berhasil", Toast.LENGTH_SHORT).show();

                    FirebaseUser firebaseUser = auth.getCurrentUser();

                    ReadWriteDataUser writeDataUser = new ReadWriteDataUser(textalamat, textnotlp, texttempatlahir, texttanggallahir, textgender);

                    DatabaseReference referenceprofil = FirebaseDatabase.getInstance().getReference("Data User");

                    referenceprofil.child(firebaseUser.getUid()).setValue(writeDataUser).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            firebaseUser.sendEmailVerification();
                            if (firebaseUser != null) {
                                UserProfileChangeRequest request = new UserProfileChangeRequest.Builder()
                                        .setDisplayName(textnama)
                                        .build();
                                firebaseUser.updateProfile(request).addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        reload();

                                        auth.signOut();

                                        startActivity(new Intent(RegisterActivity.this, LoginActivity.class));
                                        finish();


                                    }
                                });
                            } else {
                                Toast.makeText(RegisterActivity.this, "Register gagal", Toast.LENGTH_SHORT).show();
                            }

//                    Intent intent = new Intent(RegisterActivity.this,LoginActivity.class);
//                    intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK
//                    | Intent.FLAG_ACTIVITY_NEW_TASK);
//                    startActivity(intent);
//                    finish();
                        }
                    });

                } else {
                    try {
                        throw task.getException();
                    } catch (FirebaseAuthWeakPasswordException e) {
                        editPassword.setError("Password terlalu lemah");
                        editPassword.requestFocus();
                    } catch (FirebaseAuthInvalidCredentialsException e) {
                        editPassword.setError("Email invalid");
                        editPassword.requestFocus();
                    } catch (FirebaseAuthUserCollisionException e) {
                        editPassword.setError("Email telah digunakan, Gunakan Email lain");
                        editPassword.requestFocus();
                    } catch (Exception e) {
                        Log.e(TAG, e.getMessage());
                        Toast.makeText(RegisterActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                    }
                }
                progressDialog.dismiss();

            }

        });

    }


    private void reload() {
        startActivity(new Intent(getApplicationContext(), MainActivity.class));
    }

    @Override
    public void onStart() {
        super.onStart();
        FirebaseAuth auth = FirebaseAuth.getInstance();
        // Check if user is signed in (non-null) and update UI accordingly.
        FirebaseUser currentUser = auth.getCurrentUser();
        if (currentUser != null) {
            reload();
        }
    }
}


