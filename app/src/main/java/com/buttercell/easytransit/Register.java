package com.buttercell.easytransit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import io.paperdb.Paper;

public class Register extends AppCompatActivity{

    private static final String TAG = "Register";

    Button btnRegister;
    EditText txtEmail, txtPass, txtConfirmPass, txtUserName, txtPhoneNo;
    TextView txtSignIn;

    FirebaseAuth mAuth;
    ProgressDialog progressDialog;
    FirebaseFirestore firestore;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        initViews();
        mAuth = FirebaseAuth.getInstance();
        progressDialog = new ProgressDialog(this);
        Paper.init(this);
        firestore = FirebaseFirestore.getInstance();

        btnRegister.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
//                Sign up user
                signUp();
            }
        });
        txtSignIn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getBaseContext(), Login.class));
                finish();
            }
        });
    }

    private void signUp() {
        final String email = txtEmail.getText().toString().trim();
        final String password = txtPass.getText().toString().trim();
        final String userName = txtUserName.getText().toString().trim();
        String confirmPass = txtConfirmPass.getText().toString().trim();
        final String mobileNo = txtPhoneNo.getText().toString().trim();

        if (!TextUtils.isEmpty(email) && !TextUtils.isEmpty(password) && !TextUtils.isEmpty(userName) && !TextUtils.isEmpty(confirmPass)) {
            if (!confirmPass.equals(password)) {
                Toast.makeText(this, "Please Confirm your Correct Password", Toast.LENGTH_SHORT).show();
            } else {
                progressDialog.setMessage("Signing up...");
                progressDialog.setCancelable(false);
                progressDialog.show();
                mAuth.createUserWithEmailAndPassword(email, password)
                        .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "createUserWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Toast.makeText(Register.this, task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    progressDialog.dismiss();
                                } else {

                                    final String id = FirebaseAuth.getInstance().getCurrentUser().getUid();

                                    Map<String, Object> userMap = new HashMap<>();
                                    userMap.put("name", userName);
                                    userMap.put("mobileNo", mobileNo);
                                    userMap.put("email", email);
                                    userMap.put("pass", password);
                                    userMap.put("userRole", "user");


                                    firestore.collection("Users").document(id).set(userMap).addOnSuccessListener(new OnSuccessListener<Void>() {
                                        @Override
                                        public void onSuccess(Void aVoid) {
                                            Paper.book().write("email", email);
                                            Paper.book().write("pass", password);
                                            Paper.book().write("user_id", id);

                                            FirebaseUser user = FirebaseAuth.getInstance().getCurrentUser();
                                            UserProfileChangeRequest profileUpdates = new UserProfileChangeRequest.Builder()
                                                    .setDisplayName(userName).build();

                                            user.updateProfile(profileUpdates);


                                            progressDialog.dismiss();
                                            Toast.makeText(Register.this, "Great Now Log in!", Toast.LENGTH_SHORT).show();
                                            Intent intent = new Intent(Register.this, Login.class);

                                            startActivity(intent);

                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(Register.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });


                                }


                            }
                        });


            }


        } else {
            Toast.makeText(this, "Please fill out all the Fields!", Toast.LENGTH_SHORT).show();
        }
    }


    private void initViews() {
        btnRegister = findViewById(R.id.btnRegister);
        txtEmail = findViewById(R.id.txt_email);
        txtUserName = findViewById(R.id.txt_userName);
        txtPass = findViewById(R.id.txt_pass);
        txtConfirmPass = findViewById(R.id.txt_confirm_pass);
        txtPhoneNo = findViewById(R.id.txt_phoneNo);
        txtSignIn = findViewById(R.id.txt_signIn);
    }
}
