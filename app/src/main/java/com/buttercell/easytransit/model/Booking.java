package com.buttercell.easytransit.model;

import java.io.Serializable;

/**
 * Created by amush on 11-Feb-18.
 */

public class Booking implements Serializable {
    private int price;
    private String name, docNo, tripId, departure, arrival, startTime, endTime, trainClass, trainType, date, userId;


    public Booking() {
    }

    public Booking(int price, String name, String docNo, String tripId, String departure, String arrival, String startTime, String endTime, String trainClass, String trainType, String date, String userId) {
        this.price = price;
        this.name = name;
        this.docNo = docNo;
        this.tripId = tripId;
        this.departure = departure;
        this.arrival = arrival;
        this.startTime = startTime;
        this.endTime = endTime;
        this.trainClass = trainClass;
        this.trainType = trainType;
        this.date = date;
        this.userId = userId;

    }

    public int getPrice() {
        return price;
    }

    public void setPrice(int price) {
        this.price = price;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }


    public String getDocNo() {
        return docNo;
    }

    public void setDocNo(String docNo) {
        this.docNo = docNo;
    }


    public String getTripId() {
        return tripId;
    }

    public void setTripId(String tripId) {
        this.tripId = tripId;
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

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }


}
