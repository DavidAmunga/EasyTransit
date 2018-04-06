package com.buttercell.easytransit.client.book;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.buttercell.easytransit.R;
import com.buttercell.easytransit.common.Common;
import com.buttercell.easytransit.model.Trip;
import com.jaredrummler.materialspinner.MaterialSpinner;
import com.wdullaer.materialdatetimepicker.date.DatePickerDialog;

import java.util.Calendar;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

import static android.content.ContentValues.TAG;

public class TripOptions extends AppCompatActivity {
    @BindView(R.id.bookingType)
    MaterialSpinner spinnerType;
    @BindView(R.id.fromDeparture)
    TextView fromDeparture;
    @BindView(R.id.toArrival)
    TextView toArrival;
    @BindView(R.id.txtClass)
    TextView txtClass;
    @BindView(R.id.txtAdultNo)
    TextView txtAdultNo;
    @BindView(R.id.txtChildNo)
    TextView txtChildNo;
    @BindView(R.id.txtInfantNo)
    TextView txtInfantNo;
    @BindView(R.id.btnNext)
    Button btnNext;


    @BindView(R.id.departLayout)
    LinearLayout departLayout;
    @BindView(R.id.returnLayout)
    LinearLayout returnLayout;
    @BindView(R.id.fromLayout)
    LinearLayout fromLayout;
    @BindView(R.id.toLayout)
    LinearLayout toLayout;
    @BindView(R.id.classLayout)
    LinearLayout classLayout;
    @BindView(R.id.departDate)
    TextView departDate;
    @BindView(R.id.returnDate)
    TextView returnDate;

    private String bookingType = "";
    private int dt;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_trip_options);
        ButterKnife.bind(this, this);

        getSupportActionBar().setTitle("Trip Options");
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        setPassengers();

        if (spinnerType.getSelectedIndex() == 0) {
            returnLayout.setVisibility(View.INVISIBLE);
        }



        spinnerType.setItems("One-way", "Return");

        spinnerType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, Object item) {
                if (item.equals("One-way")) {
                    bookingType = item.toString();
                    returnLayout.setVisibility(View.INVISIBLE);
                    returnDate.setText("");
                } else {
                    bookingType = item.toString();
                    returnLayout.setVisibility(View.VISIBLE);

                }
            }
        });

        selectListeners();


        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String fromDep = fromDeparture.getText().toString().trim();
                String toArr = toArrival.getText().toString().trim();
                String tripClass = txtClass.getText().toString().trim();
                String depDate = departDate.getText().toString().trim();


                if (!TextUtils.isEmpty(fromDep) && !TextUtils.isEmpty(toArr) && !TextUtils.isEmpty(depDate)) {
                    String retDate = "";
                    if (spinnerType.getSelectedIndex() == 1) {
                        Log.d(TAG, "Return");
                        retDate = returnDate.getText().toString().trim();


                    } else {
                        retDate = "One-way";
                    }

                    Trip trip = new Trip();
                    trip.setDeparture(fromDep);
                    trip.setArrival(toArr);
                    trip.setTrainClass(tripClass);
                    trip.setDate(depDate);

                    Common.retDate=retDate;
                    Common.currentTripDetails=trip;

                    startActivity(new Intent(TripOptions.this, TripResults.class)
                    );
                } else {
                    Toast.makeText(TripOptions.this, "Please select all the trip details", Toast.LENGTH_SHORT).show();
                }


            }
        });
    }

    private void setPassengers() {
        if (Common.adultNo != 0) {
            txtAdultNo.setText(String.valueOf(Common.adultNo));
            txtChildNo.setText(String.valueOf(Common.childNo));
            txtInfantNo.setText(String.valueOf(Common.infantNo));
        }
    }

    private void selectListeners() {

        txtClass.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String[] tripClass = getResources().getStringArray(R.array.tripClass);
                final ArrayAdapter<String> classList =
                        new ArrayAdapter<String>(TripOptions.this, android.R.layout.simple_list_item_1, tripClass);

                AlertDialog.Builder mBuilder = new AlertDialog.Builder(TripOptions.this);
                mBuilder.setTitle("Choose Departure Station");
                mBuilder.setSingleChoiceItems(classList, -1, new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        txtClass.setText(classList.getItem(i));
                        dialogInterface.dismiss();
                    }
                });

                AlertDialog mDialog = mBuilder.create();
                mDialog.show();
            }
        });

        fromLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickStationDialog("departure");
            }
        });

        toLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showPickStationDialog("arrival");
            }
        });


        departLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dt = 1;
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                Log.d(TAG, "month " + month);
                                String date = String.format("%02d", dayOfMonth) + "-" + String.format("%02d", month) + "-" + year;
                                Log.d(TAG, "onDateSet: " + date);
                                departDate.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setTitle("Pick a Departure Date");

                Calendar calendarNow = Calendar.getInstance();

                int month = calendarNow.get(Calendar.MONTH);


                Calendar calendarMax = Calendar.getInstance();
                calendarMax.set(Calendar.MONTH, month + 1);

                datePickerDialog.setMinDate(calendarNow);
                datePickerDialog.setMaxDate(calendarMax);
                datePickerDialog.show(
                        getFragmentManager(), "DatePicker");
            }
        });
        returnLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dt = 2;
                Calendar now = Calendar.getInstance();
                com.wdullaer.materialdatetimepicker.date.DatePickerDialog datePickerDialog = com.wdullaer.materialdatetimepicker.date.DatePickerDialog.newInstance(
                        new DatePickerDialog.OnDateSetListener() {
                            @Override
                            public void onDateSet(DatePickerDialog view, int year, int monthOfYear, int dayOfMonth) {
                                int month = monthOfYear + 1;
                                Log.d(TAG, "month " + month);
                                String date = String.format("%02d", dayOfMonth) + "-" + String.format("%02d", month) + "-" + year;
                                Log.d(TAG, "onDateSet: " + date);
                                returnDate.setText(date);
                            }
                        },
                        now.get(Calendar.YEAR),
                        now.get(Calendar.MONTH),
                        now.get(Calendar.DAY_OF_MONTH)
                );
                datePickerDialog.setTitle("Pick a Return Date");

                Calendar calendarNow = Calendar.getInstance();

                int day = calendarNow.get(Calendar.DAY_OF_MONTH);
                calendarNow.set(Calendar.DAY_OF_MONTH, day + 1);


                int month = calendarNow.get(Calendar.MONTH);
                Calendar calendarMax = Calendar.getInstance();
                calendarMax.set(Calendar.MONTH, month + 1);

                datePickerDialog.setMinDate(calendarNow);
                datePickerDialog.setMaxDate(calendarMax);
                datePickerDialog.show(getFragmentManager(), "DatePicker");

            }
        });


    }

    private void showPickStationDialog(String location) {
        if (location.equals("departure")) {

            final ArrayAdapter<String> stationList =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Common.stationList);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("Choose Departure Station");
            mBuilder.setSingleChoiceItems(stationList, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    fromDeparture.setText(stationList.getItem(i));
                    dialogInterface.dismiss();
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        } else if (location.equals("arrival")) {

            final ArrayAdapter<String> stationList =
                    new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, Common.stationList);

            AlertDialog.Builder mBuilder = new AlertDialog.Builder(this);
            mBuilder.setTitle("Choose Arrival Station");
            mBuilder.setSingleChoiceItems(stationList, -1, new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialogInterface, int i) {
                    toArrival.setText(stationList.getItem(i));
                    dialogInterface.dismiss();
                }
            });

            AlertDialog mDialog = mBuilder.create();
            mDialog.show();
        }
    }
}
