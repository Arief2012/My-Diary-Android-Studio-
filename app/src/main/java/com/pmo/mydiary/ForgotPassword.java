package com.pmo.mydiary;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.util.Patterns;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;

public class ForgotPassword extends AppCompatActivity {

    private EditText edForgotpass;
    private Button btnForgotpass;
    private TextView texttologin;
    private ProgressDialog progressDialog;

    FirebaseAuth auth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgot_password);

        edForgotpass = findViewById(R.id.edemailforgot);
        btnForgotpass = findViewById(R.id.btn_forgotpass);
        texttologin = findViewById(R.id.text_forgottologin);

        progressDialog = new ProgressDialog(ForgotPassword.this);
        progressDialog.setTitle("Loading");
        progressDialog.setMessage("Silahkan tunggu");
        progressDialog.setCancelable(false);


        auth = FirebaseAuth.getInstance();

        texttologin.setOnClickListener(v -> {
            startActivity(new Intent(getApplicationContext(), LoginActivity.class));
        });

        btnForgotpass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                resetPassword();
            }
        });

    }

    private void resetPassword() {

        String email = edForgotpass.getText().toString().trim();

        if (email.isEmpty()) {
            edForgotpass.setError("Silahkan memasukan Email");
            edForgotpass.requestFocus();
            return;
        }

        if (!Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            edForgotpass.setError("Silahkan berikan Email yang valid");
            edForgotpass.requestFocus();
            return;
        }

        progressDialog.show();

        auth.sendPasswordResetEmail(email).addOnCompleteListener(new OnCompleteListener<Void>() {
            @Override
            public void onComplete(@NonNull Task<Void> task) {

                if (task.isSuccessful()) {
                    Toast.makeText(ForgotPassword.this, "Cek Email anda untuk mereset Password!", Toast.LENGTH_LONG).show();
                    showAlertDialog();
                } else {
                    Toast.makeText(ForgotPassword.this, "Terjadi kesalahan, Silahkan coba kembali!", Toast.LENGTH_LONG).show();
                }

                progressDialog.dismiss();
            }
        });
    }

    private void showAlertDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(ForgotPassword.this);
        builder.setTitle("Apakah anda ingin mengganti password?");
        builder.setMessage("Silahkan cek email terlebih dahulu untuk mengganti password");

        builder.setPositiveButton("Lanjutkan", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                Intent intent = new Intent(Intent.ACTION_MAIN);
                intent.addCategory(Intent.CATEGORY_APP_EMAIL);
                intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                startActivity(intent);
            }
        });
        builder.setNegativeButton("Batal", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                dialogInterface.cancel();
            }
        });
        AlertDialog alertDialog = builder.create();

        alertDialog.show();
    }
}