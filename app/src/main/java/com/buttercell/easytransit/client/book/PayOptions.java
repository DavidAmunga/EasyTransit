package com.buttercell.easytransit.client.book;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.buttercell.easytransit.R;
import com.buttercell.easytransit.client.ClientHome;
import com.buttercell.easytransit.common.Common;
import com.buttercell.easytransit.model.Booking;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PayOptions extends AppCompatActivity {
    private static final String TAG = "PayOptions";

    @BindView(R.id.txtTicketNo)
    TextView txtTicketNo;
    @BindView(R.id.txtAmount)
    TextView txtAmount;
    @BindView(R.id.btnPay)
    Button btnPay;
    private int dep_amount = 0;
    private int ret_amount = 0;
    private List<Booking> dep_booking = new ArrayList<>();
    private List<Booking> ret_booking = new ArrayList<>();

    ProgressDialog progressDialog;
    private String returnKey = "";
    private String departKey = "";
    private String userId = "";
    private int departureCapacity;
    private int returnCapacity;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pay_options);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Pay Options");

        departureCapacity = Common.currentTrip.getCapacity();
        returnCapacity = Common.returnTrip.getCapacity();
        progressDialog = new ProgressDialog(this);

        userId = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("returnKey") != null) {
                returnKey = getIntent().getExtras().getString("returnKey");
            }
            departKey = getIntent().getExtras().getString("departKey");

            Log.d(TAG, "onCreate: Depart Key " + departKey);

            dep_amount = getIntent().getExtras().getInt("dep_amount");
            dep_booking = (ArrayList<Booking>) getIntent().getExtras().getSerializable("dep_booking");

            if (!Common.retDate.equals("One-way")) {
                ret_amount = getIntent().getExtras().getInt("ret_amount");
                ret_booking = (ArrayList<Booking>) getIntent().getExtras().getSerializable("ret_booking");
            }
        }

        int ticketNo = Common.childNo + Common.adultNo;
        txtTicketNo.setText(String.valueOf(ticketNo));
        int full_amount = dep_amount + ret_amount;
        txtAmount.setText(String.valueOf("KES " + full_amount));

        btnPay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                progressDialog.setMessage("Paying....");
                progressDialog.setCancelable(false);
                progressDialog.show();

                for (int i = 0; i < dep_booking.size(); i++) {

                    final String id = UUID.randomUUID().toString();
                    //Add to Trips
                    final int finalI = i;
                    final int finalI1 = i;
                    FirebaseFirestore.getInstance().collection("Trips").
                            document(departKey).collection("userBookings").document(id).set(dep_booking.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {

                            //Add to User
                            FirebaseFirestore.getInstance().collection("Users").
                                    document(userId).collection("userTrips").document(id).set(dep_booking.get(finalI)).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    departureCapacity++;
                                    //Increment Capacity
                                    DocumentReference docRef = FirebaseFirestore.getInstance().collection("Trips").document(departKey);
                                    docRef.update("capacity", departureCapacity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                        @Override
                                        public void onComplete(@NonNull Task<Void> task) {

                                            if (ret_booking.size() == 0 && finalI1 >= dep_booking.size()) {
                                                Toast.makeText(PayOptions.this, "All Bookings Done!", Toast.LENGTH_SHORT).show();
                                                startActivity(new Intent(PayOptions.this, ClientHome.class));
                                                finish();
                                            } else if (finalI1 >= dep_booking.size()) {

                                                Toast.makeText(PayOptions.this, "Depart Bookings Done!", Toast.LENGTH_SHORT).show();


                                            }


                                        }
                                    }).addOnFailureListener(new OnFailureListener() {
                                        @Override
                                        public void onFailure(@NonNull Exception e) {
                                            progressDialog.dismiss();
                                            Toast.makeText(PayOptions.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                        }
                                    });
                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(PayOptions.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });


                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PayOptions.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
                }
                if (ret_booking.size() != 0) {
                    doReturnOptions();
                }


            }
        });


    }

    private void doReturnOptions() {

        for (int i = 0; i < ret_booking.size(); i++) {

            final String id = UUID.randomUUID().toString();
            //Add to Trips
            final int finalI = i;
            FirebaseFirestore.getInstance().collection("Trips").
                    document(returnKey).collection("userBookings").document(id).set(ret_booking.get(i)).addOnCompleteListener(new OnCompleteListener<Void>() {
                @Override
                public void onComplete(@NonNull Task<Void> task) {

                    //Add to User
                    FirebaseFirestore.getInstance().collection("Users").
                            document(userId).collection("userTrips").document(id).set(ret_booking.get(finalI)).addOnCompleteListener(new OnCompleteListener<Void>() {
                        @Override
                        public void onComplete(@NonNull Task<Void> task) {
                            returnCapacity++;
                            //Increment Capacity
                            DocumentReference docRef = FirebaseFirestore.getInstance().collection("Trips").document(returnKey);
                            docRef.update("capacity", returnCapacity).addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    progressDialog.dismiss();
                                    Toast.makeText(PayOptions.this, "All Bookings Done!", Toast.LENGTH_SHORT).show();
                                    startActivity(new Intent(PayOptions.this, ClientHome.class));
                                    finish();


                                }
                            }).addOnFailureListener(new OnFailureListener() {
                                @Override
                                public void onFailure(@NonNull Exception e) {
                                    progressDialog.dismiss();
                                    Toast.makeText(PayOptions.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                                }
                            });
                        }
                    }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(PayOptions.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });


                }
            }).addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    progressDialog.dismiss();
                    Toast.makeText(PayOptions.this, e.getLocalizedMessage(), Toast.LENGTH_SHORT).show();
                }
            });
        }
    }

}
