package com.buttercell.easytransit.admin;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.buttercell.easytransit.R;
import com.buttercell.easytransit.model.Station;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class AddStation extends AppCompatActivity {


    @BindView(R.id.img_station)
    ImageView imgStation;
    @BindView(R.id.txt_station_name)
    TextView txt_station_name;
    @BindView(R.id.txt_station_info)
    TextView txt_station_info;
    @BindView(R.id.btnAdd)
    Button btnAdd;

    private ProgressDialog progressDialog;
    private FirebaseFirestore firestore;
    private Uri imageUri;
    private int PICK_IMAGE = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_station);

        ButterKnife.bind(this,this);

        imageUri = null;
        progressDialog = new ProgressDialog(this);
        firestore = FirebaseFirestore.getInstance();
        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                add_station();
            }
        });
        imgStation.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setType("image/*");
                intent.setAction(Intent.ACTION_GET_CONTENT);
                startActivityForResult(Intent.createChooser(intent, "Select Station Image"), PICK_IMAGE);
            }
        });

    }

    private void add_station() {
        final String name = txt_station_name.getText().toString().trim();
        final String info = txt_station_info.getText().toString().trim();

        if (!TextUtils.isEmpty(name) && !TextUtils.isEmpty(info) && imageUri != null) {
            progressDialog.setMessage("Adding Station.....");
            progressDialog.setCancelable(false);
            progressDialog.show();


            final String id = UUID.randomUUID().toString();

            StorageReference station_ref = FirebaseStorage.getInstance().getReference("Station").child(id + ".jpg");
            station_ref.putFile(imageUri).addOnCompleteListener(new OnCompleteListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onComplete(@NonNull Task<UploadTask.TaskSnapshot> task) {

                    String downloadUrl = task.getResult().getDownloadUrl().toString();

                    Station station = new Station(name, info, downloadUrl);


                    firestore.collection("Stations").document(id).set(station).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            Toast.makeText(AddStation.this, "Station added", Toast.LENGTH_SHORT).show();
                            startActivity(new Intent(AddStation.this,AdminHome.class).putExtra("fragment","stations"));
                            finish();
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(AddStation.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });

                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(AddStation.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });


        }
        else
        {
            Toast.makeText(this, "Please fill all the fields!", Toast.LENGTH_SHORT).show();
        }


    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE) {
            imageUri = data.getData();
            imgStation.setImageURI(imageUri);
        }
    }
}
