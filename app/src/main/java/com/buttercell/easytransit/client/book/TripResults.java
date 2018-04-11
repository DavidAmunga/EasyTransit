package com.buttercell.easytransit.client.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.buttercell.easytransit.R;
import com.buttercell.easytransit.admin.AdminTrips;
import com.buttercell.easytransit.common.Common;
import com.buttercell.easytransit.model.Trip;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TripResults extends AppCompatActivity {

    private static final String TAG = "TripResults";

    @BindView(R.id.departureTrips)
    LinearLayout departureTrips;
    @BindView(R.id.departureList)
    RecyclerView departureList;
    @BindView(R.id.returnTrips)
    LinearLayout returnTrips;
    @BindView(R.id.returnList)
    RecyclerView returnList;

    String returnKey="";
    String departKey="";

    FirestoreRecyclerAdapter<Trip, AdminTrips.TripViewHolder> departureAdapter;

    FirestoreRecyclerAdapter<Trip, AdminTrips.TripViewHolder> returnAdapter;

    Query query;
    FirestoreRecyclerOptions<Trip> options;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_results);

        ButterKnife.bind(this, this);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Trip Results");
        query = FirebaseFirestore.getInstance()
                .collection("Trips")
                .orderBy("capacity")
                .orderBy("timestamp")
                .whereLessThan("capacity", 20)
                .whereEqualTo("date", Common.currentTripDetails.getDate());
        Log.d(TAG, "Trip Date" + Common.currentTripDetails.getDate());

        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot snapshot,
                                @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    // Handle error
                    //...
                    return;
                }

                // Convert query snapshot to a list of chats
                List<Trip> trips = snapshot.toObjects(Trip.class);
                Log.d(TAG, "onEvent: Trip Objects " + trips.toString());
                // Update UI
                // ...
            }
        });


        options = new FirestoreRecyclerOptions.Builder<Trip>()
                .setQuery(query, Trip.class)
                .build();


        if (!Common.retDate.equals("One-way")) {
            Log.d(TAG, "setMenuVisibility: Return On");
            returnList.setVisibility(View.VISIBLE);
            returnTrips.setVisibility(View.VISIBLE);

            showReturnAdapter();
        } else {
            returnList.setVisibility(View.GONE);
            returnTrips.setVisibility(View.GONE);


        }


        departureList.setLayoutManager(new LinearLayoutManager(this));
        departureList.setHasFixedSize(true);


        departureAdapter = new FirestoreRecyclerAdapter<Trip, AdminTrips.TripViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminTrips.TripViewHolder holder, final int position, @NonNull final Trip model) {
                holder.setTxtArrival(model.getArrival());
                holder.setTxtClass(model.getTrainClass());
                holder.setTxtDeparture(model.getDeparture());
                holder.setTxtType(model.getTrainType());
                holder.setTxtEndTime(model.getEndTime());
                holder.setTxtStartTime(model.getStartTime());
                holder.setCapacity(model.getCapacity());
                holder.setTxtDate(model.getDate());

                Log.d(TAG, "onBindViewHolder: " + model);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String tripId = getSnapshots().getSnapshot(position).getId();

                        Common.currentTrip = model;

                        departKey=tripId;

                        Log.d(TAG, "onClick: Dep Key"+departKey);


                        if (!Common.retDate.equals("One-way")) {
                            if(TextUtils.isEmpty(Common.returnTrip.getDate()))  {
                                Toast.makeText(TripResults.this, "Select Return Date", Toast.LENGTH_SHORT).show();
                            } else {
                                startActivity(new Intent(TripResults.this, PassengerDetails.class)
                                .putExtra("currentTripKey",departKey)
                                .putExtra("returnTripKey",returnKey));

                            }

                        }
                        else {
                            startActivity(new Intent(TripResults.this, PassengerDetails.class)
                                    .putExtra("currentTripKey",departKey));
                        }
                    }
                });
            }

            @Override
            public AdminTrips.TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_layout, parent, false);
                return new AdminTrips.TripViewHolder(view);
            }
        };


        departureList.setAdapter(departureAdapter);
    }

    private void showReturnAdapter() {

        Log.d(TAG, "Return date :" + Common.retDate);
        Query query = FirebaseFirestore.getInstance()
                .collection("Trips")
                .orderBy("capacity")
                .whereEqualTo("date", Common.retDate)
                .whereLessThan("capacity", 20)
                .whereEqualTo("arrival", Common.currentTripDetails.getDeparture());
        FirestoreRecyclerOptions<Trip> options = new FirestoreRecyclerOptions.Builder<Trip>()
                .setQuery(query, Trip.class)
                .build();

        returnList.setLayoutManager(new LinearLayoutManager(this));
        returnList.setHasFixedSize(true);

        returnAdapter = new FirestoreRecyclerAdapter<Trip, AdminTrips.TripViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull AdminTrips.TripViewHolder holder, final int position, @NonNull final Trip model) {
                holder.setTxtArrival(model.getArrival());
                holder.setTxtClass(model.getTrainClass());
                holder.setTxtDeparture(model.getDeparture());
                holder.setTxtType(model.getTrainType());
                holder.setTxtEndTime(model.getEndTime());
                holder.setTxtStartTime(model.getStartTime());
                holder.setCapacity(model.getCapacity());
                holder.setTxtDate(model.getDate());

                Log.d(TAG, "onBindViewHolder: " + model);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String returnTripId = getSnapshots().getSnapshot(position).getId();

                        returnKey=returnTripId;

                        Common.returnTrip = model;

                        Log.d(TAG, "onClick: Ret Key"+returnKey);
                        if (TextUtils.isEmpty(Common.currentTrip.getDate())) {
                            Toast.makeText(TripResults.this, "Select Departure Trip", Toast.LENGTH_SHORT).show();
                        } else {
                            startActivity(new Intent(TripResults.this, PassengerDetails.class)
                                    .putExtra("currentTripKey",departKey)
                                    .putExtra("returnTripKey",returnKey));
                        }



                    }
                });
            }

            @Override
            public AdminTrips.TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_layout, parent, false);
                return new AdminTrips.TripViewHolder(view);
            }
        };
        returnAdapter.startListening();

        returnList.setAdapter(returnAdapter);
    }

    @Override
    public void onStart() {
        super.onStart();
        departureAdapter.startListening();


    }


    @Override
    public void onStop() {
        super.onStop();
        departureAdapter.stopListening();
        if(returnAdapter!=null)
        {
            returnAdapter.stopListening();
        }

    }

}
