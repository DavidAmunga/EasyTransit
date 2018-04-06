package com.buttercell.easytransit.admin;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.buttercell.easytransit.R;
import com.buttercell.easytransit.client.ClientTrips;
import com.buttercell.easytransit.model.Booking;
import com.buttercell.easytransit.model.Trip;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripDetails extends AppCompatActivity {

    private static final String TAG = "TripDetails";

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
    @BindView(R.id.card_trip)
    CardView cardTrip;
    @BindView(R.id.booking_list)
    RecyclerView bookingList;
    private Trip trip;
    private String tripId = "";

    FirestoreRecyclerAdapter<Booking, BookingViewHolder> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_details);
        ButterKnife.bind(this);

        getSupportActionBar().setTitle("Trip Details");
        if (getIntent().getExtras() != null) {
            tripId = getIntent().getStringExtra("tripId");
            trip = (Trip) getIntent().getSerializableExtra("trip");

        }


        cap.setMax(20);
        cap.setProgress(trip.getCapacity());
        txtType.setText(trip.getTrainType());
        txtClass.setText(trip.getTrainClass());
        txtArrival.setText(trip.getArrival());
        txtDeparture.setText(trip.getDeparture());
        txtStartTime.setText(trip.getStartTime());
        txtEndTime.setText(trip.getEndTime());


        Query query = FirebaseFirestore.getInstance()
                .collection("Trips")
                .document(tripId)
                .collection("userBookings");

        FirestoreRecyclerOptions<Booking> options = new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query, Booking.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Booking, BookingViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BookingViewHolder holder, int position, @NonNull Booking model) {
                holder.txtDocNo.setText(model.getDocNo());
                holder.txtUsername.setText(model.getName());


                Log.d(TAG, "onBindViewHolder: True");
            }

            @Override
            public BookingViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_layout, parent, false);
                return new BookingViewHolder(view);
            }
        };

        bookingList.setHasFixedSize(true);
        bookingList.setLayoutManager(new LinearLayoutManager(this));

        bookingList.setAdapter(adapter);


    }

    public static class BookingViewHolder extends RecyclerView.ViewHolder {
        private static final String TAG = "BookingViewHolder";
        TextView txtUsername, txtDocNo;

        public BookingViewHolder(View itemView) {
            super(itemView);

            txtUsername = itemView.findViewById(R.id.txtUsername);
            txtDocNo = itemView.findViewById(R.id.txtDocNo);
        }


    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
