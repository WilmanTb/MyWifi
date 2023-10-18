package com.mywifi.mywifi.Activity.user;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.AppCompatButton;

import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.provider.MediaStore;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.mywifi.mywifi.jClass.Data_Value;
import com.mywifi.mywifi.Model.Model_Verifikasi;
import com.mywifi.mywifi.R;
import com.mywifi.mywifi.jClass.LoadingDialog;
import com.squareup.picasso.Picasso;

import java.io.ByteArrayOutputStream;

public class Activity_Upload_Ktp extends AppCompatActivity {

    private ImageView img_ktp;
    private Button btn_open_camera, btn_upload;
    private FirebaseAuth userAuth;
    private DatabaseReference dbVerifikasi;
    private StorageReference firebaseStorage;
    private String UID, Url="";
    private Uri imageUri;
    private LoadingDialog loadingDialog;
    private static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload_ktp);

        initComponents();

        btn_open_camera.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openCamera(v);
            }
        });

        btn_upload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                loadingDialog.show();

                Handler handler = new Handler();
                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {
                        loadingDialog.dismiss();
                        uploadFoto(new CallbackFoto() {
                            @Override
                            public void onCallBackFoto(String URL) {
                                Model_Verifikasi model_verifikasi = new Model_Verifikasi(UID, URL, "true", "false");
                                dbVerifikasi.child("Verifikasi").child(UID).setValue(model_verifikasi);
                                Dialog dialog = new Dialog(Activity_Upload_Ktp.this);
                                dialog.setContentView(R.layout.pop_up_finish);
                                dialog.show();

                                AppCompatButton btn_ok = dialog.findViewById(R.id.btn_ok);

                                btn_ok.setOnClickListener(new View.OnClickListener() {
                                    @Override
                                    public void onClick(View v) {
                                        startActivity(new Intent(Activity_Upload_Ktp.this, Dashboard_User_Activity.class));
                                        finish();
                                    }
                                });
                            }
                        });
                    }
                };
                handler.postDelayed(runnable,2000);
            }
        });

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        startActivity(new Intent(this, Activity_Verifikasi_Akun.class));
        finish();
    }

    private void initComponents(){
        loadingDialog = new LoadingDialog(this);
        img_ktp = findViewById(R.id.img_ktp);
        btn_upload = findViewById(R.id.btn_upload);
        btn_open_camera = findViewById(R.id.btn_open_camera);
        userAuth = FirebaseAuth.getInstance();
        FirebaseUser firebaseUser = userAuth.getCurrentUser();
        UID = firebaseUser.getUid();
        firebaseStorage = FirebaseStorage.getInstance().getReference();
        dbVerifikasi = FirebaseDatabase.getInstance(Data_Value.dbUrl).getReference();
    }

    private void openCamera(View view) {
//        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
//        if (intent.resolveActivity(getPackageManager()) != null) {
//            startActivityForResult(intent, 1);
//        } else {
//            Toast.makeText(this, "Camera app not found", Toast.LENGTH_SHORT).show();
//        }
        Intent intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        startActivityForResult(intent, 1);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 1 && resultCode == RESULT_OK && data != null) {
            Bitmap imageBitmap = (Bitmap) data.getExtras().get("data");
            img_ktp.setVisibility(View.VISIBLE);
            if (imageBitmap != null) {
                imageUri = getImageUri(this, imageBitmap);
                Picasso.get().load(imageUri).into(img_ktp);
                btn_upload.setVisibility(View.VISIBLE);
                btn_open_camera.setVisibility(View.GONE);
            }
        }
    }

    private Uri getImageUri(Context context, Bitmap bitmap) {
        ByteArrayOutputStream bytes = new ByteArrayOutputStream();
        bitmap.compress(Bitmap.CompressFormat.JPEG, 100, bytes);
        String path = MediaStore.Images.Media.insertImage(context.getContentResolver(), bitmap, "Image", null);
        return Uri.parse(path);
    }

    private String getFileExtension(Uri uri) {
        ContentResolver contentResolver = getContentResolver();
        MimeTypeMap mimeTypeMap = MimeTypeMap.getSingleton();
        return mimeTypeMap.getExtensionFromMimeType(contentResolver.getType(uri));
    }

    private void uploadFoto(CallbackFoto callbackFoto) {
        if (imageUri != null) {
            StorageReference storageReference = firebaseStorage.child("ktp")
                    .child(System.currentTimeMillis() + "." + getFileExtension(imageUri));
            StorageTask<UploadTask.TaskSnapshot> mUploadTask = storageReference.putFile(imageUri)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            storageReference.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                                @Override
                                public void onSuccess(Uri uri) {
                                    String url = uri.toString();
                                    callbackFoto.onCallBackFoto(url);
                                }
                            });
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(Activity_Upload_Ktp.this, e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        } else {
            String url = "empty";
            callbackFoto.onCallBackFoto(url);
        }
    }

    private interface CallbackFoto {
        void onCallBackFoto(String URL);
    }
}