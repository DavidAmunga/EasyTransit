package com.buttercell.easytransit.admin;


import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.roundcornerprogressbar.RoundCornerProgressBar;
import com.buttercell.easytransit.AddTrip;
import com.buttercell.easytransit.R;
import com.buttercell.easytransit.client.book.PassengerOptions;
import com.buttercell.easytransit.model.Trip;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;


/**
 * A simple {@link Fragment} subclass.
 */
public class AdminTrips extends Fragment {
    private static final String TAG = "AdminMain";
    RecyclerView tripList;
    FirebaseFirestore firestore;
    FirestoreRecyclerAdapter<Trip, TripViewHolder> adapter;

    public AdminTrips() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_trips, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        setHasOptionsMenu(true);
        getActivity().setTitle("");


        tripList = view.findViewById(R.id.tripList);
        tripList.setLayoutManager(new LinearLayoutManager(getContext()));
        tripList.setHasFixedSize(true);

        firestore = FirebaseFirestore.getInstance();


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddTrip.class));
            }
        });


        getTrips();


    }


    private void getTrips() {

        Log.d(TAG, "getTrips: Start");

        Query query = firestore.collection("Trips").orderBy("timestamp", Query.Direction.DESCENDING);


        FirestoreRecyclerOptions<Trip> tripOptions = new FirestoreRecyclerOptions.Builder<Trip>()
                .setQuery(query, Trip.class)
                .build();
        adapter = new FirestoreRecyclerAdapter<Trip, TripViewHolder>(tripOptions) {
            @Override
            protected void onBindViewHolder(@NonNull TripViewHolder holder, final int position, @NonNull final Trip model) {
                holder.txtArrival.setText(model.getArrival());
                holder.txtClass.setText(model.getTrainClass());
                holder.txtDeparture.setText(model.getDeparture());
                holder.txtType.setText(model.getTrainType());
                holder.txtEndTime.setText(model.getEndTime());
                holder.txtStartTime.setText(model.getStartTime());

                holder.cap.setMax(7);
                holder.cap.setProgress(model.getCapacity());

                Log.d(TAG, "onBindViewHolder: " + model);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {

                        String tripId = getSnapshots().getSnapshot(position).getId();

                        Intent intent = new Intent(getContext(), TripDetails.class);
                        intent.putExtra("trip", model);
                        intent.putExtra("tripId", tripId);
                        startActivity(intent);
                    }
                });

            }

            @Override
            public TripViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.trip_layout, parent, false);

                return new TripViewHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("onError: ", e.getMessage());
            }
        };
        adapter.notifyDataSetChanged();
        tripList.setAdapter(adapter);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        menu.clear();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clickss here. The action bar wiasls
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement

        return super.onOptionsItemSelected(item);
    }

    public static class TripViewHolder extends RecyclerView.ViewHolder {
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


        public TripViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

        public void setTxtDeparture(String departure)
        {
            txtDeparture.setText(departure);
        }
        public void setTxtArrival(String arrival)
        {
            txtArrival.setText(arrival);
        }
        public void setTxtStartTime(String startTime)
        {
            txtStartTime.setText(startTime);
        }
        public void setTxtEndTime(String endTime)
        {
            txtEndTime.setText(endTime);
        }
        public void setTxtClass(String tripClass)
        {
            txtClass.setText(tripClass);
        }
        public void setTxtType(String tripType)
        {
            txtType.setText(tripType);
        }
        public void setCapacity(int capacity)
        {
            cap.setMax(7);
            cap.setProgress(capacity);
        }
    }

    @Override
    public void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    public void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
