package com.buttercell.easytransit;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.buttercell.easytransit.admin.AdminHome;
import com.buttercell.easytransit.client.book.PassengerOptions;
import com.buttercell.easytransit.client.book.TripOptions;
import com.buttercell.easytransit.common.Common;
import com.buttercell.easytransit.model.Station;
import com.buttercell.easytransit.model.Trip;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;
import com.wdullaer.materialdatetimepicker.time.TimePickerDialog;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.List;
import java.util.UUID;

public class AddTrip extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener, DatePickerDialog.OnDateSetListener {

    MaterialSpinner spinnerArr, spinnerDep, spinnerClass, spinnerType;
    List<String> locations = new ArrayList<>();
    List<String> types = new ArrayList<>();
    List<String> classes = new ArrayList<>();
    Button btnAdd;
    EditText edtDiscount, edtFee;
    TextView txtStart, txtEnd, txtDate;
    private int time;
    FirebaseFirestore firestore;
    ProgressDialog progressDialog;

    private static final String TAG = "AddTrip";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_trip);
        progressDialog = new ProgressDialog(this);

        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        Query query = FirebaseFirestore.getInstance().collection("Stations");


        query.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(QuerySnapshot documentSnapshots, FirebaseFirestoreException e) {
                for (DocumentChange doc : documentSnapshots.getDocumentChanges()) {
                    if (doc.getType() == DocumentChange.Type.ADDED) {

                        Station station = doc.getDocument().toObject(Station.class);
                        locations.add(station.getStation_name());

                        spinnerArr.setItems(locations);
                        spinnerDep.setItems(locations);
                    }
                }
            }
        });

        initViews();

        spinnerDep.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                Toast.makeText(AddTrip.this, locations.get(position), Toast.LENGTH_SHORT).show();
            }
        });

        btnAdd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String discount = edtDiscount.getText().toString();
                String trainType = types.get(spinnerType.getSelectedIndex());
                String trainClass = classes.get(spinnerClass.getSelectedIndex());
                String departureTime = txtStart.getText().toString().trim();
                String arrivalTime = txtEnd.getText().toString().trim();
                final String date = txtDate.getText().toString().trim();
                int fee = Integer.parseInt(edtFee.getText().toString().trim());
                String arrival = locations.get(spinnerArr.getSelectedIndex());
                String departure = locations.get(spinnerDep.getSelectedIndex());


                final Trip trip = new Trip(departure, arrival, departureTime, arrivalTime, trainClass, trainType, date, discount, fee, 0);

                final String tripId = UUID.randomUUID().toString();

                firestore.collection("Trips").document(tripId).set(trip).addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {

                        firestore.collection("Trips").document(tripId).update("timestamp", FieldValue.serverTimestamp());
                        Toast.makeText(AddTrip.this, "Trip Added", Toast.LENGTH_SHORT).show();
                        startActivity(new Intent(AddTrip.this, AdminHome.class));
                        finish();


                    }
                });


                Toast.makeText(AddTrip.this, locations.get(spinnerArr.getSelectedIndex()), Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void initViews() {
        firestore = FirebaseFirestore.getInstance();
        txtStart = findViewById(R.id.txtStartTime);
        txtEnd = findViewById(R.id.txtEndTime);
        txtDate = findViewById(R.id.txtDate);

        spinnerArr = findViewById(R.id.spinnerArrival);
        spinnerDep = findViewById(R.id.spinnerDeparture);
        spinnerClass = findViewById(R.id.spinnerClass);
        spinnerType = findViewById(R.id.spinnerType);
        edtDiscount = findViewById(R.id.txtDiscount);
        edtFee = findViewById(R.id.txtFee);
        btnAdd = findViewById(R.id.btnAdd);


        types = Arrays.asList(getResources().getStringArray(R.array.typesArray));
        classes = Arrays.asList(getResources().getStringArray(R.array.classesArray));


        spinnerClass.setItems(classes);
        spinnerType.setItems(types);


        txtStart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 1;
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        AddTrip.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                ); //True is 24 Hrs , False is 12 hours
                timePickerDialog.setTitle("Pick a time");
                timePickerDialog.show(getFragmentManager(), "Time Picker");
            }
        });
        txtEnd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                time = 2;
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.time.TimePickerDialog timePickerDialog = com.wdullaer.materialdatetimepicker.time.TimePickerDialog.newInstance(
                        AddTrip.this,
                        now.get(Calendar.HOUR_OF_DAY),
                        now.get(Calendar.MINUTE),
                        false
                ); //True is 24 Hrs , False is 12 hours
                timePickerDialog.setTitle("Pick a time");
                timePickerDialog.show(getFragmentManager(), "Time Picker");
            }
        });

        txtDate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        AddTrip.this,
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setTitle("Pick a Date");
                datePickerDialog.show(getFragmentManager(), "DatePicker");
            }
        });

    }

    @Override
    public void onTimeSet(TimePickerDialog view, int hourOfDay, int minute, int second) {
        if (time == 1) {
            String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
            Log.d(TAG, "onTimeSet: " + time);
            txtStart.setText(time);
        } else {
            String time = String.format("%02d", hourOfDay) + ":" + String.format("%02d", minute);
            Log.d(TAG, "onTimeSet: " + time);
            txtEnd.setText(time);
        }
    }

    @Override
    public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
        int month = monthOfYear + 1;
        Log.d(TAG, "month " + month);
        String date = String.format("%02d", dayOfMonth) + "-" + String.format("%02d", month) + "-" + year;
        Log.d(TAG, "onDateSet: " + date);
        txtDate.setText(date);
    }
}
