package com.buttercell.easytransit.model;

/**
 * Created by amush on 08-Feb-18.
 */

public class Station {
    private String station_name,station_info,station_photo_url;

    public Station(String station_name, String station_info, String station_photo_url) {
        this.station_name = station_name;
        this.station_info = station_info;
        this.station_photo_url = station_photo_url;
    }

    public Station() {
    }

    public String getStation_name() {
        return station_name;
    }

    public void setStation_name(String station_name) {
        this.station_name = station_name;
    }

    public String getStation_info() {
        return station_info;
    }

    public void setStation_info(String station_info) {
        this.station_info = station_info;
    }

    public String getStation_photo_url() {
        return station_photo_url;
    }

    public void setStation_photo_url(String station_photo_url) {
        this.station_photo_url = station_photo_url;
    }
}
