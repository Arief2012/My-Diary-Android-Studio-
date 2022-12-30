package com.pmo.mydiary;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

public class UploadImageActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private ProgressBar progressBar;
    private ImageView ivUplodefoto;
    private FirebaseAuth authprofil;
    private StorageReference storageReference;
    private FirebaseUser firebaseUser;
    private static final int PICK_IMAGE_REQUEST = 1;
    private Uri uriImage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_image);

        actionBar = getSupportActionBar();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Upload Foto Profil");

        Button buttonpilihfoto = findViewById(R.id.button_pilihimage);
        Button buttonUploadfoto = findViewById(R.id.button_uploadimage);
        progressBar = findViewById(R.id.progressbar_profil);
        ivUplodefoto = findViewById(R.id.imageview_uploadimage);

        authprofil = FirebaseAuth.getInstance();
        firebaseUser = authprofil.getCurrentUser();

        storageReference = FirebaseStorage.getInstance().getReference("Foto Profil");

        Uri uri = firebaseUser.getPhotoUrl();

        Picasso.with(UploadImageActivity.this).load(uri).into(ivUplodefoto);

        buttonpilihfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                openfilechooser();
            }
        });

        buttonUploadfoto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressBar.setVisibility(View.VISIBLE);
                Uploadfoto();
            }
        });
    }


    private void openfilechooser() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent, PICK_IMAGE_REQUEST);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            uriImage = data.getData();
            ivUplodefoto.setImageURI(uriImage);
        }
    }

    private void Uploadfoto() {
        if (uriImage != null) {
            StorageReference fileReference = storageReference.child(authprofil.getCurrentUser().getUid() + "."
                    + getFileExtension(uriImage));


            fileReference.putFile(uriImage).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    fileReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            Uri downloadUri = uri;
                            firebaseUser = authprofil.getCurrentUser();

                            UserProfileChangeRequest profileChangeRequest = new UserProfileChangeRequest.Builder()
                                    .setPhotoUri(downloadUri).build();
                            firebaseUser.updateProfile(profileChangeRequest);
                        }
                    });

                    progressBar.setVisibility(View.GONE);
                    Toast.makeText(UploadImageActivity.this, "Foto berhasil diupload", Toast.LENGTH_SHORT).show();

                    startActivity(new Intent(UploadImageActivity.this, ProfilActivity.class));
                    finish();
                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    Toast.makeText(UploadImageActivity.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        } else {
            progressBar.setVisibility(View.GONE);
            Toast.makeText(this, "Foto belum dipilih", Toast.LENGTH_SHORT).show();
        }
    }

    private String getFileExtension(Uri uriImage) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uriImage));
    }

    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}