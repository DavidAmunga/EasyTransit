package com.buttercell.easytransit.client;


import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.buttercell.easytransit.R;
import com.buttercell.easytransit.admin.AdminTrips;
import com.buttercell.easytransit.common.Common;
import com.buttercell.easytransit.model.Booking;
import com.buttercell.easytransit.model.Trip;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * A simple {@link Fragment} subclass.
 */
public class ClientTrips extends Fragment {

    private static final String TAG = "ClientTrips";

    @BindView(R.id.tripList)
    RecyclerView tripList;
    Unbinder unbinder;

    String userID;
    FirestoreRecyclerAdapter<Booking, BookingTripsViewHolder> adapter;

    public ClientTrips() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_client_trips, container, false);
        unbinder = ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("");

        userID = FirebaseAuth.getInstance().getCurrentUser().getUid();

        Query query = FirebaseFirestore.getInstance()
                .collection("Users")
                .document(userID)
                .collection("userTrips");

        FirestoreRecyclerOptions<Booking> options = new FirestoreRecyclerOptions.Builder<Booking>()
                .setQuery(query, Booking.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Booking, BookingTripsViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull BookingTripsViewHolder holder, int position, @NonNull Booking model) {
                holder.setTxtArrival(model.getArrival());
                holder.setTxtClass(model.getTrainClass());
                holder.setTxtDate(model.getDate());
                holder.setTxtDocNo(model.getDocNo());
                holder.setTxtStartTime(model.getStartTime());
                holder.setTxtDeparture(model.getDeparture());
                holder.setTxtType(model.getTrainType());
                holder.setTxtUsername(model.getName());


                Log.d(TAG, "onBindViewHolder: True");
            }

            @Override
            public BookingTripsViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.booking_trip_layout, parent, false);
                return new BookingTripsViewHolder(view);
            }
        };

        tripList.setHasFixedSize(true);
        tripList.setLayoutManager(new LinearLayoutManager(getContext()));

        tripList.setAdapter(adapter);


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

    public static class BookingTripsViewHolder extends RecyclerView.ViewHolder {

        public BookingTripsViewHolder(View itemView) {
            super(itemView);
        }

        public void setTxtUsername(String name) {
            TextView txtUsername = itemView.findViewById(R.id.txtUsername);
            txtUsername.setText(name);
        }

        public void setTxtDocNo(String doc) {
            TextView txtDocNo = itemView.findViewById(R.id.txtDocNo);
            txtDocNo.setText(doc);
        }

        public void setTxtDeparture(String departure) {
            TextView txtDeparture = itemView.findViewById(R.id.txtDeparture);
            txtDeparture.setText(departure);
        }

        public void setTxtArrival(String arrival) {
            TextView txtArrival = itemView.findViewById(R.id.txtArrival);
            txtArrival.setText(arrival);
        }

        public void setTxtStartTime(String startTime) {
            TextView txtStartime = itemView.findViewById(R.id.txtStartTime);
            txtStartime.setText(startTime);
        }

        public void setTxtEndTime(String endTime) {
            TextView txtEndTime = itemView.findViewById(R.id.txtEndTime);
            txtEndTime.setText(endTime);
        }

        public void setTxtClass(String trainClass) {
            TextView txtClass = itemView.findViewById(R.id.txtClass);
            txtClass.setText(trainClass);
        }

        public void setTxtType(String trainType) {
            TextView txtType = itemView.findViewById(R.id.txtType);
            txtType.setText(trainType);
        }

        public void setTxtDate(String date) {
            TextView txtDate = itemView.findViewById(R.id.txtDate);
            txtDate.setText(date);
        }

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        unbinder.unbind();
    }
}
