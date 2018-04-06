package com.buttercell.easytransit.client.book;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.buttercell.easytransit.R;
import com.buttercell.easytransit.common.Common;
import com.buttercell.easytransit.model.Station;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.shawnlin.numberpicker.NumberPicker;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class PassengerOptions extends AppCompatActivity {


    @BindView(R.id.txtAdultNo)
    NumberPicker txtAdultNo;
    @BindView(R.id.txtChildNo)
    NumberPicker txtChildNo;
    @BindView(R.id.txtInfantNo)
    NumberPicker txtInfantNo;
    @BindView(R.id.btnNext)
    Button btnNext;
    private List<String> stationList=new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_passenger_options);

        ButterKnife.bind(this, this);

        getSupportActionBar().setTitle("Passenger Options");

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                int adultNo = txtAdultNo.getValue();
                int childNo = txtChildNo.getValue();
                int infantNo = txtInfantNo.getValue();


                Common.adultNo=adultNo;
                Common.childNo=childNo;
                Common.infantNo=infantNo;


                Query query = FirebaseFirestore.getInstance().collection("Stations");

                query.addSnapshotListener(new EventListener<QuerySnapshot>() {
                    @Override
                    public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                        for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                            if (doc.getType() == DocumentChange.Type.ADDED) {

                                Station station= doc.getDocument().toObject(Station.class);
                                stationList.add(station.getStation_name());

                                Common.stationList=stationList;

                                startActivity(new Intent(PassengerOptions.this,TripOptions.class)
                                        );
                            }
                        }
                    }
                });




            }
        });
    }
}
