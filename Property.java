package com.kchen.chrometopia;

/**
 * Created by kchen on 4/24/2015.
 */
public class Property {
    private String MLSNumber;
    private String Address;
    private double ROI;
    private double Price;

    private String ListingKey;
    private String MediaUrl;
    private double NumberOfUnites;

    public String getMLSNumber() {
        return MLSNumber;
    }

    public void setMLSNumber(String MLSNumber) {
        this.MLSNumber = MLSNumber;
    }

    public String getAddress() {
        return Address;
    }

    public void setAddress(String address) {
        Address = address;
    }

    public double getROI() {
        return ROI;
    }

    public void setROI(double ROI) {
        this.ROI = ROI;
    }

    public double getPrice() {
        return Price;
    }

    public void setPrice(double price) {
        Price = price;
    }

    public String getListingKey() {
        return ListingKey;
    }

    public void setListingKey(String listingKey) {
        ListingKey = listingKey;
    }

    public String getMediaUrl() {
        return MediaUrl;
    }

    public void setMediaUrl(String mediaUrl) {
        MediaUrl = mediaUrl;
    }

    public double getNumberOfUnites() {
        return NumberOfUnites;
    }

    public void setNumberOfUnites(double numberOfUnites) {
        NumberOfUnites = numberOfUnites;
    }
}
