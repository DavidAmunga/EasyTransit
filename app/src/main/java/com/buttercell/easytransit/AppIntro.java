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
import android.widget.ProgressBar;
import android.widget.Toast;

import com.buttercell.easytransit.admin.AdminHome;
import com.buttercell.easytransit.client.ClientHome;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;

import butterknife.BindView;
import butterknife.ButterKnife;
import io.paperdb.Paper;

public class AppIntro extends AppCompatActivity {
    private static final String TAG = "AppIntro";
    FirebaseAuth mAuth;
    @BindView(R.id.pb)
    ProgressBar pb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_app_intro);
        ButterKnife.bind(this);

        Paper.init(this);


        getWindow().setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN);

        mAuth = FirebaseAuth.getInstance();


        String em = Paper.book().read("email");
        String pwd = Paper.book().read("pass");
        if (em != null && pwd != null) {
            pb.setVisibility(View.VISIBLE);
            if (!TextUtils.isEmpty(em) && !TextUtils.isEmpty(pwd)) {
                mAuth.signInWithEmailAndPassword(em, pwd)
                        .addOnCompleteListener(AppIntro.this, new OnCompleteListener<AuthResult>() {
                            @Override
                            public void onComplete(@NonNull Task<AuthResult> task) {
                                Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                                // If sign in fails, display a message to the user. If sign in succeeds
                                // the auth state listener will be notified and logic to handle the
                                // signed in user can be handled in the listener.
                                if (!task.isSuccessful()) {
                                    Log.w(TAG, "signInWithEmail:failed", task.getException());
                                    Toast.makeText(AppIntro.this, task.getException().getLocalizedMessage(),
                                            Toast.LENGTH_SHORT).show();
                                    pb.setVisibility(View.GONE);
                                } else {
                                    Log.d(TAG, "onComplete: Sign In Success!");
                                    Paper.book().write("user_id", FirebaseAuth.getInstance().getCurrentUser().getUid());

                                    verify_user(FirebaseAuth.getInstance().getCurrentUser().getUid());

                                }

                            }
                        });


            }
        } else {
            startActivity(new Intent(AppIntro.this, Login.class));
        }


    }

    private void verify_user(String uid) {
        FirebaseFirestore.getInstance().collection("Users").document(uid).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                String userRole = documentSnapshot.getString("userRole");
                String userName = documentSnapshot.getString("name");
                if (userRole.equals("user")) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(AppIntro.this, "Welcome " + userName + "!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AppIntro.this, ClientHome.class));
                    finish();
                } else if (userRole.equals("admin")) {
                    pb.setVisibility(View.GONE);
                    Toast.makeText(AppIntro.this, "Welcome " + userName + "!", Toast.LENGTH_SHORT).show();
                    startActivity(new Intent(AppIntro.this, AdminHome.class));
                    finish();
                }
            }
        });

    }
}
