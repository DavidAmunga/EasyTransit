package com.buttercell.easytransit.client.book;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buttercell.easytransit.R;
import com.buttercell.easytransit.common.Common;
import com.buttercell.easytransit.model.Booking;
import com.google.firebase.auth.FirebaseAuth;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PassengerDetails extends AppCompatActivity {
    private static final String TAG = "PassengerDetails";

    @BindView(R.id.passenger_details_layout)
    LinearLayout passengerDetailsLayout;


    View passengerAdultView;
    View passengerChildView;
    @BindView(R.id.btnNext)
    Button btnNext;

    String returnKey = "";
    String departKey = "";
    String userID;

    List<Booking> departureBookingList = new ArrayList<>();
    List<Booking> returnBookingList = new ArrayList<>();

    int i = 0;
    int j = 0;
    private int count = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_details);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Passenger Details");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        if (getIntent().getExtras() != null) {
            if (getIntent().getExtras().getString("returnTripKey") != null) {
                returnKey = getIntent().getExtras().getString("returnTripKey");
            }
            departKey = getIntent().getExtras().getString("currentTripKey");
        }


        LayoutInflater inflater = (LayoutInflater) PassengerDetails.this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        passengerAdultView = inflater.inflate(R.layout.passenger_adult_layout, null);
        passengerChildView = inflater.inflate(R.layout.passenger_child_layout, null);
        // Add the new row before the add field button.


        final int size = Common.adultNo + Common.childNo;
        passengerDetailsLayout.removeAllViews();

        Log.d(TAG, "onCreate: Adult No" + Common.adultNo);


        passengerDetailsLayout.addView(passengerAdultView);


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (count <= size) {
                    Log.d(TAG, "onClick: Count Item" + count);
                    count++;
                    Toast.makeText(PassengerDetails.this, "Enter for passenger " + (count+1), Toast.LENGTH_SHORT).show();
                    if (i < Common.adultNo) {
                        Log.d(TAG, "onClick: Adult");

                        if (Common.childNo == 0 && count == Common.adultNo - 1) {
                            btnNext.setText("FINISH");
                        }

                        Booking departureBooking = getAdultPassengerDetails(passengerAdultView, "Depart");


                        departureBookingList.add(departureBooking);


                        if (!Common.retDate.equals("One-way")) {
                            Booking returnBooking = getAdultPassengerDetails(passengerAdultView, "Return");

                            returnBookingList.add(returnBooking);
                        }

                        i++;
                    }
                    if (Common.childNo > 0) {
                        if (count >= Common.adultNo) {
//                Start Child Booking

                            passengerDetailsLayout.removeAllViews();

                            passengerDetailsLayout.addView(passengerChildView);

                            if (j < Common.childNo) {
                                if (j == Common.childNo - 1) {
                                    btnNext.setText("FINISH");
                                }
                                Log.d(TAG, "onClick: Count" + count);
                                if (count >= Common.adultNo + 1) {
                                    Log.d(TAG, "onClick: Child");


                                    Booking departureBooking = getChildPassengerDetails(passengerChildView, "Depart");


                                    departureBookingList.add(departureBooking);

                                    if (!Common.retDate.equals("One-way")) {
                                        Booking returnBooking = getChildPassengerDetails(passengerChildView, "Return");


                                        returnBookingList.add(returnBooking);
                                    }


                                    j++;
                                }

                            }

                        }
                    }


                }
                if (count >= size) {
                    if (!Common.retDate.equals("One-way")) {

                        Toast.makeText(PassengerDetails.this, "Done!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PassengerDetails.this, BookingReview.class)
                                .putExtra("departureBookingList", (Serializable) departureBookingList)
                                .putExtra("departKey", departKey)
                                .putExtra("returnKey", returnKey)
                                .putExtra("returnBookingList", (Serializable) returnBookingList));
                    }
                    else
                    {
                        Toast.makeText(PassengerDetails.this, "Done!", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(PassengerDetails.this, BookingReview.class)
                                .putExtra("departKey", departKey)
                                .putExtra("departureBookingList", (Serializable) departureBookingList));
                    }

                }


            }
        });


    }

    private Booking getAdultPassengerDetails(View passengerAdultView, String status) {
        final TextView txtPassengerName = passengerAdultView.findViewById(R.id.txtPassengerName);
        final TextView txtPassengerID = passengerAdultView.findViewById(R.id.txtPassengerID);


        String passengerName = txtPassengerName.getText().toString().trim();
        String passengerID = txtPassengerID.getText().toString().trim();

        Booking booking;

        if (status.equals("Depart")) {
            booking = new Booking(Common.currentTrip.getFee(), passengerName, passengerID, departKey, Common.currentTrip.getDeparture(), Common.currentTrip.getArrival(), Common.currentTrip.getStartTime(), Common.currentTrip.getEndTime(), Common.currentTrip.getTrainClass(), Common.currentTrip.getTrainType(), Common.currentTrip.getDate(), userID);

        } else {
            booking = new Booking(Common.returnTrip.getFee(), passengerName, passengerID, departKey, Common.returnTrip.getDeparture(), Common.returnTrip.getArrival(), Common.returnTrip.getStartTime(), Common.returnTrip.getEndTime(), Common.returnTrip.getTrainClass(), Common.returnTrip.getTrainType(), Common.returnTrip.getDate(), userID);
        }

        return booking;

    }

    private Booking getChildPassengerDetails(View passengerAdultView, String status) {
        final TextView txtPassengerName = passengerAdultView.findViewById(R.id.txtPassengerName);

        String passengerName = txtPassengerName.getText().toString().trim();



        Booking booking;

        if (status.equals("Depart")) {
            int childFee = Common.currentTrip.getFee() / 2;
            booking = new Booking(childFee, passengerName, "Under-age", departKey, Common.currentTrip.getDeparture(), Common.currentTrip.getArrival(), Common.currentTrip.getStartTime(), Common.currentTrip.getEndTime(), Common.currentTrip.getTrainClass(), Common.currentTrip.getTrainType(), Common.currentTrip.getDate(), userID);

        } else {
            int childFee = Common.returnTrip.getFee() / 2;
            booking = new Booking(childFee, passengerName, "Under-age", departKey, Common.returnTrip.getDeparture(), Common.returnTrip.getArrival(), Common.returnTrip.getStartTime(), Common.returnTrip.getEndTime(), Common.returnTrip.getTrainClass(), Common.returnTrip.getTrainType(), Common.returnTrip.getDate(), userID);
        }
        return booking;

    }
}
