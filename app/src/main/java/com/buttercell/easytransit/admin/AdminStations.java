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
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.buttercell.easytransit.R;
import com.buttercell.easytransit.model.Station;
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
public class AdminStations extends Fragment {

    private static final String TAG = "AdminStations";
    FirebaseFirestore firestore;
    RecyclerView mList;
    FirestoreRecyclerAdapter<Station, StationViewHolder> adapter;

    public AdminStations() {
        // Required empty public constructor
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_admin_stations, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        getActivity().setTitle("");


        mList = view.findViewById(R.id.stationList);
        mList.setLayoutManager(new LinearLayoutManager(getContext()));
        mList.setHasFixedSize(true);

        firestore = FirebaseFirestore.getInstance();


        FloatingActionButton fab = view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getContext(), AddStation.class));
            }
        });


        Query query = firestore.collection("Stations");


        FirestoreRecyclerOptions<Station> stationOptions = new FirestoreRecyclerOptions.Builder<Station>()
                .setQuery(query, Station.class)
                .build();

        adapter = new FirestoreRecyclerAdapter<Station, StationViewHolder>(stationOptions) {
            @Override
            protected void onBindViewHolder(@NonNull StationViewHolder holder, int position, @NonNull Station model) {
                holder.txtStationName.setText(model.getStation_name());
                holder.txtStationInfo.setText(model.getStation_info());
                Log.d(TAG, "onBindViewHolder: " + model);

                Glide.with(getContext()).load(model.getStation_photo_url()).into(holder.imgStation);

            }

            @Override
            public StationViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_layout, parent, false);
                return new StationViewHolder(view);
            }

            @Override
            public void onError(@NonNull FirebaseFirestoreException e) {
                Log.e("onError: ", e.getLocalizedMessage());
            }
        };

        adapter.notifyDataSetChanged();
        mList.setAdapter(adapter);


    }


    public static class StationViewHolder extends RecyclerView.ViewHolder {
        @BindView(R.id.txtStationInfo)
        TextView txtStationInfo;
        @BindView(R.id.txtStationName)
        TextView txtStationName;
        @BindView(R.id.imgStation)
        ImageView imgStation;

        public StationViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
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
