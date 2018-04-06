package com.buttercell.easytransit;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.Toast;

import java.text.SimpleDateFormat;
import java.util.Calendar;

import devs.mulham.horizontalcalendar.HorizontalCalendar;
import devs.mulham.horizontalcalendar.utils.HorizontalCalendarListener;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        Calendar startDate = Calendar.getInstance();

        /* ends after 1 month from now */
        Calendar endDate = Calendar.getInstance();
        endDate.add(Calendar.MONTH, 1);

        HorizontalCalendar horizontalCalendar = new HorizontalCalendar.Builder(this, R.id.calendarView)
                .range(startDate, endDate)
                .datesNumberOnScreen(5)
                .build();

        horizontalCalendar.setCalendarListener(new HorizontalCalendarListener() {
            @Override
            public void onDateSelected(Calendar date, int position) {
//                Format Date
                SimpleDateFormat format1 = new SimpleDateFormat("dd-MM-yyyy");
                String currentDate=format1.format(date.getTime());

                Log.d(TAG, "onDateSelected: "+date);
                Toast.makeText(MainActivity.this, currentDate, Toast.LENGTH_SHORT).show();
            }
        });
        horizontalCalendar.selectDate(Calendar.getInstance(),true);
        horizontalCalendar.refresh();

    }

}
