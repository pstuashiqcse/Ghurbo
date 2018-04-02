package com.mcc.ghurbo.model;

/**
 * Created by Ashiq on 4/2/2018.
 */

public class LocationModel {

    private int hotelId;
    private String location;

    public LocationModel(int hotelId, String location) {
        this.hotelId = hotelId;
        this.location = location;
    }

    public int getHotelId() {
        return hotelId;
    }

    public String getLocation() {
        return location;
    }
}