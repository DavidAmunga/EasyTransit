package com.buttercell.easytransit.model;

import java.io.Serializable;

/**
 * Created by amush on 08-Feb-18.
 */

public class Rating implements Serializable {
    private String userComments,userName,userRating,userTripId;


    public Rating(String userComments, String userName, String userRating, String userTripId) {
        this.userComments = userComments;
        this.userName = userName;
        this.userRating = userRating;
        this.userTripId = userTripId;
    }

    public Rating() {
    }

    public String getUserComments() {
        return userComments;
    }

    public void setUserComments(String userComments) {
        this.userComments = userComments;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRating() {
        return userRating;
    }

    public void setUserRating(String userRating) {
        this.userRating = userRating;
    }

    public String getUserTripId() {
        return userTripId;
    }

    public void setUserTripId(String userTripId) {
        this.userTripId = userTripId;
    }
}
