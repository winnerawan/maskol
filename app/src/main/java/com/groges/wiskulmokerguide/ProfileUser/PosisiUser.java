package com.groges.wiskulmokerguide.ProfileUser;

public class PosisiUser {
    public static double Lat;
    public static double Lng;

    public PosisiUser(double lat,double lng) {
        Lat = lat;
        Lng =lng;
    }

    public static double getLat() {
        return Lat;
    }

    public static void setLat(double lat) {
        Lat = lat;
    }

    public static double getLng() {
        return Lng;
    }

    public static void setLng(double lng) {
        Lng = lng;
    }
}
