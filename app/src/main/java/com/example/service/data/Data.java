package com.example.service.data;

import com.example.service.models.Organization;
import com.example.service.models.User;

public class Data {

    public static Organization org = null;
    public static String address = "Click to Enter Address";
    public static double lat;
    public static double lng;
    public static User toUser;

    Data() {

    }

    public static void setOrg(Organization organization) {
        org = organization;
    }

    public static Organization getOrg() {
        return org;
    }

    public static void setAddress(String newAddress) {
        address = newAddress;
    }

    public static void clearAddress() {
        address = "Click to Enter Address";
    }

    public static String getAddress() {
        return address;
    }

    public static void setLat(double latitude) {
        lat = latitude;
    }

    public static double getLat() {
        return lat;
    }

    public static void setLng(double longitude) {
        lng = longitude;
    }

    public static double getLng() {
        return lng;
    }

    public static User getToUser() {
        return toUser;
    }

    public static void setToUser(User pUser) {
        toUser = pUser;
    }

}
