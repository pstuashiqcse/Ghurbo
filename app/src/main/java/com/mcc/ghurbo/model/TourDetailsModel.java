package com.mcc.ghurbo.model;


import android.os.Parcel;
import android.os.Parcelable;

import java.util.ArrayList;

public class TourDetailsModel implements Parcelable{

    private String tourId;
    private String tourTitle;
    private String tourDesc;
    private String tourStars;
    private String latitude;
    private String longitude;
    private String location;
    private String maxAdults;
    private String maxChild;
    private String maxInfant;
    private String adultPrice;
    private String childPrice;
    private String infantPrice;
    private String adultStatus;
    private String childStatus;
    private String infantStatus;
    private String tourDays;
    private String tourNights;
    private String thumbnailImage;
    private String phoneNumber;
    private boolean isFavorite;
    private ArrayList<String> allPhotos = null;
    private ArrayList<AmenityModel> amenities = null;

    public TourDetailsModel(String tourId, String tourTitle,
                            String tourDesc, String tourStars,
                            String latitude, String longitude,
                            String location, String maxAdults,
                            String maxChild, String maxInfant,
                            String adultPrice, String childPrice,
                            String infantPrice, String adultStatus,
                            String childStatus, String infantStatus,
                            String tourDays, String tourNights,
                            String thumbnailImage, String phoneNumber, boolean isFavorite,
                            ArrayList<String> allPhotos, ArrayList<AmenityModel> amenities) {
        this.tourId = tourId;
        this.tourTitle = tourTitle;
        this.tourDesc = tourDesc;
        this.tourStars = tourStars;
        this.latitude = latitude;
        this.longitude = longitude;
        this.location = location;
        this.maxAdults = maxAdults;
        this.maxChild = maxChild;
        this.maxInfant = maxInfant;
        this.adultPrice = adultPrice;
        this.childPrice = childPrice;
        this.infantPrice = infantPrice;
        this.adultStatus = adultStatus;
        this.childStatus = childStatus;
        this.infantStatus = infantStatus;
        this.tourDays = tourDays;
        this.tourNights = tourNights;
        this.thumbnailImage = thumbnailImage;
        this.phoneNumber = phoneNumber;
        this.isFavorite = isFavorite;
        this.allPhotos = allPhotos;
        this.amenities = amenities;
    }

    public String getTourId() {
        return tourId;
    }

    public String getTourTitle() {
        return tourTitle;
    }

    public String getTourDesc() {
        return tourDesc;
    }

    public String getTourStars() {
        return tourStars;
    }

    public String getLatitude() {
        return latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public String getLocation() {
        return location;
    }

    public String getMaxAdults() {
        return maxAdults;
    }

    public void setMaxAdults(String maxAdults) {
        this.maxAdults = maxAdults;
    }

    public String getMaxChild() {
        return maxChild;
    }

    public String getMaxInfant() {
        return maxInfant;
    }

    public String getAdultPrice() {
        return adultPrice;
    }

    public String getChildPrice() {
        return childPrice;
    }

    public String getInfantPrice() {
        return infantPrice;
    }

    public String getAdultStatus() {
        return adultStatus;
    }

    public String getChildStatus() {
        return childStatus;
    }

    public String getInfantStatus() {
        return infantStatus;
    }

    public String getTourDays() {
        return tourDays;
    }

    public String getTourNights() {
        return tourNights;
    }

    public String getThumbnailImage() {
        return thumbnailImage;
    }

    public ArrayList<String> getAllPhotos() {
        return allPhotos;
    }

    public ArrayList<AmenityModel> getAmenities() {
        return amenities;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public boolean isFavorite() {
        return isFavorite;
    }

    public void setFavorite(boolean favorite) {
        isFavorite = favorite;
    }

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(tourId);
        dest.writeString(tourTitle);
        dest.writeString(tourDesc);
        dest.writeString(tourStars);
        dest.writeString(latitude);
        dest.writeString(longitude);
        dest.writeString(location);
        dest.writeString(maxAdults);
        dest.writeString(maxChild);
        dest.writeString(maxInfant);
        dest.writeString(adultPrice);
        dest.writeString(childPrice);
        dest.writeString(infantPrice);
        dest.writeString(adultStatus);
        dest.writeString(childStatus);
        dest.writeString(infantStatus);
        dest.writeString(tourDays);
        dest.writeString(tourNights);
        dest.writeString(thumbnailImage);
        dest.writeString(phoneNumber);
        dest.writeByte((byte) (isFavorite ? 1 : 0));
        dest.writeStringList(allPhotos);
        dest.writeTypedList(amenities);
    }

    protected TourDetailsModel(Parcel in) {
        tourId = in.readString();
        tourTitle = in.readString();
        tourDesc = in.readString();
        tourStars = in.readString();
        latitude = in.readString();
        longitude = in.readString();
        location = in.readString();
        maxAdults = in.readString();
        maxChild = in.readString();
        maxInfant = in.readString();
        adultPrice = in.readString();
        childPrice = in.readString();
        infantPrice = in.readString();
        adultStatus = in.readString();
        childStatus = in.readString();
        infantStatus = in.readString();
        tourDays = in.readString();
        tourNights = in.readString();
        thumbnailImage = in.readString();
        phoneNumber = in.readString();
        isFavorite = in.readByte() != 0;
        allPhotos = in.createStringArrayList();
        amenities = in.createTypedArrayList(AmenityModel.CREATOR);
    }

    public static final Creator<TourDetailsModel> CREATOR = new Creator<TourDetailsModel>() {
        @Override
        public TourDetailsModel createFromParcel(Parcel in) {
            return new TourDetailsModel(in);
        }

        @Override
        public TourDetailsModel[] newArray(int size) {
            return new TourDetailsModel[size];
        }
    };
}
