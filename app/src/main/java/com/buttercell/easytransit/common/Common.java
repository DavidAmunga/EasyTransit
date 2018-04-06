package com.buttercell.easytransit.common;

import com.buttercell.easytransit.model.Booking;
import com.buttercell.easytransit.model.Trip;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by amush on 11-Feb-18.
 */

public class Common {
    public static int childNo=0;
    public static int adultNo=0;
    public static int infantNo=0;

    public static Booking currentBooking;


    public static List<String> stationList=new ArrayList<>();

    public static String retDate="";
    public static Trip currentTripDetails=new Trip();
    public static Trip returnTrip =new Trip();
    public static Trip currentTrip=new Trip();
}
