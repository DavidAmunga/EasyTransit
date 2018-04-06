package com.buttercell.easytransit.client.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.buttercell.easytransit.R;
import com.buttercell.easytransit.adapter.BookingAdapter;
import com.buttercell.easytransit.common.Common;
import com.buttercell.easytransit.model.Booking;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class BookingReview extends AppCompatActivity {
    private static final String TAG = "BookingReview";
    @BindView(R.id.booking_list)
    RecyclerView bookingList;
    @BindView(R.id.txtAdultNo)
    TextView txtAdultNo;
    @BindView(R.id.txtChildNo)
    TextView txtChildNo;
    @BindView(R.id.txtInfantNo)
    TextView txtInfantNo;
    @BindView(R.id.txtDeparture)
    TextView txtDeparture;
    @BindView(R.id.txtArrival)
    TextView txtArrival;
    @BindView(R.id.txtStartTime)
    TextView txtStartTime;
    @BindView(R.id.txtEndTime)
    TextView txtEndTime;
    @BindView(R.id.txtClass)
    TextView txtClass;
    @BindView(R.id.txtType)
    TextView txtType;
    @BindView(R.id.cap)
    RoundCornerProgressBar cap;
    @BindView(R.id.txtAmount)
    TextView txtAmount;
    @BindView(R.id.txtReturnDeparture)
    TextView txtReturnDeparture;
    @BindView(R.id.txtReturnArrival)
    TextView txtReturnArrival;
    @BindView(R.id.txtReturnStartTime)
    TextView txtReturnStartTime;
    @BindView(R.id.txtReturnEndTime)
    TextView txtReturnEndTime;
    @BindView(R.id.txtReturnClass)
    TextView txtReturnClass;
    @BindView(R.id.txtReturnType)
    TextView txtReturnType;
    @BindView(R.id.returnCap)
    RoundCornerProgressBar returnCap;
    @BindView(R.id.txtReturnAmount)
    TextView txtReturnAmount;
    @BindView(R.id.returnCard)
    CardView returnCard;
    @BindView(R.id.btnFinish)
    Button btnFinish;

    List<Booking> departure_list_booking = new ArrayList<>();
    private List<Booking> return_list_booking = new ArrayList<>();
    BookingAdapter bookingAdapter;

    int returnAmount = 0;
    int departureAmount = 0;
    private String returnKey="";
    private String departKey="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_booking_review);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Booking Review");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        if (getIntent().getExtras() != null) {

            if (getIntent().getExtras().getString("returnKey") != null) {
                returnKey = getIntent().getExtras().getString("returnKey");
            }
            departKey = getIntent().getExtras().getString("departKey");



            departure_list_booking = (ArrayList<Booking>) getIntent().getSerializableExtra("departureBookingList");
            return_list_booking = (ArrayList<Booking>) getIntent().getSerializableExtra("returnBookingList");

            Log.d(TAG, "List: " + departure_list_booking.toString());
            bookingAdapter = new BookingAdapter(departure_list_booking, this);

            bookingList.setLayoutManager(new LinearLayoutManager(this));
            bookingList.setHasFixedSize(true);
            bookingList.setAdapter(bookingAdapter);
        }

        if (!Common.retDate.equals("One-way")) {
            returnCard.setVisibility(View.VISIBLE);

            txtReturnDeparture.setText(Common.returnTrip.getDeparture());
            txtReturnArrival.setText(Common.returnTrip.getArrival());
            txtReturnStartTime.setText(Common.returnTrip.getStartTime());
            txtReturnEndTime.setText(Common.returnTrip.getEndTime());
            txtReturnClass.setText(Common.returnTrip.getTrainClass());
            txtReturnType.setText(Common.returnTrip.getTrainType());
            returnCap.setMax(7);
            returnCap.setProgress(Common.returnTrip.getCapacity());

            for (int i = 0; i < return_list_booking.size(); i++) {
                returnAmount = returnAmount + return_list_booking.get(i).getPrice();

                Log.d(TAG, "onCreate: Return Amount" + return_list_booking.get(i).getPrice());
                txtReturnAmount.setText(String.valueOf("KES " + returnAmount));

            }

        }

        txtAdultNo.setText(String.valueOf(Common.adultNo));
        txtChildNo.setText(String.valueOf(Common.childNo));
        txtInfantNo.setText(String.valueOf(Common.infantNo));


        txtDeparture.setText(Common.currentTrip.getDeparture());
        txtArrival.setText(Common.currentTrip.getArrival());
        txtStartTime.setText(Common.currentTrip.getStartTime());
        txtEndTime.setText(Common.currentTrip.getEndTime());
        txtClass.setText(Common.currentTrip.getTrainClass());
        txtType.setText(Common.currentTrip.getTrainType());
        cap.setMax(7);
        cap.setProgress(Common.currentTrip.getCapacity());

        for (int i = 0; i < departure_list_booking.size(); i++) {
            departureAmount = departureAmount + departure_list_booking.get(i).getPrice();

            Log.d(TAG, "onCreate: Depart Amount" + departure_list_booking.get(i).getPrice());
            Log.d(TAG, "onCreate: Depart Amount" + departureAmount);


            txtAmount.setText(String.valueOf("KES " + departureAmount));

        }


        btnFinish.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                if (!Common.retDate.equals("One-way")) {
                    startActivity(new Intent(BookingReview.this, PayOptions.class)
                            .putExtra("dep_amount", departureAmount)
                            .putExtra("ret_amount", returnAmount)
                            .putExtra("departKey", departKey)
                            .putExtra("returnKey", returnKey)
                            .putExtra("dep_booking", (Serializable) departure_list_booking)
                            .putExtra("ret_booking", (Serializable) return_list_booking)
                    );
                } else {
                    startActivity(new Intent(BookingReview.this, PayOptions.class)
                            .putExtra("dep_amount", departureAmount)
                            .putExtra("departKey", departKey)
                            .putExtra("dep_booking", (Serializable) departure_list_booking)

                    );
                }
            }
        });


    }
}
