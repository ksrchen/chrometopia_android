package com.kchen.chrometopia;

/**
 * Created by kchen on 4/24/2015.
 */
public class Property {
    private String MLSNumber;
    private String Address;
    private double ROI;
    private double Price;

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
}
