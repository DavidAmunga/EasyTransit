package com.buttercell.easytransit.model;

import com.google.firebase.firestore.ServerTimestamp;

import java.io.Serializable;
import java.util.Date;

/**
 * Created by amush on 02-Feb-18.
 */

public class Trip implements Serializable {
    private String departure, arrival, startTime, endTime, trainClass, trainType, date, discount;
    private int fee, capacity;
    @ServerTimestamp
    private Date mTimestamp;


    public Trip(String departure, String arrival, String startTime, String endTime, String trainClass, String trainType, String date, String discount, int fee, int capacity) {
        this.departure = departure;
        this.arrival = arrival;
        this.startTime = startTime;
        this.endTime = endTime;
        this.trainClass = trainClass;
        this.trainType = trainType;
        this.date = date;
        this.discount = discount;
        this.fee = fee;
        this.capacity = capacity;

    }

    public Trip() {
    }

    public String getDeparture() {
        return departure;
    }

    public void setDeparture(String departure) {
        this.departure = departure;
    }

    public String getArrival() {
        return arrival;
    }

    public void setArrival(String arrival) {
        this.arrival = arrival;
    }


    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public String getTrainClass() {
        return trainClass;
    }

    public void setTrainClass(String trainClass) {
        this.trainClass = trainClass;
    }

    public String getTrainType() {
        return trainType;
    }

    public void setTrainType(String trainType) {
        this.trainType = trainType;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getDiscount() {
        return discount;
    }

    public void setDiscount(String discount) {
        this.discount = discount;
    }

    public int getFee() {
        return fee;
    }

    public void setFee(int fee) {
        this.fee = fee;
    }

    public int getCapacity() {
        return capacity;
    }

    public void setCapacity(int capacity) {
        this.capacity = capacity;
    }


}
