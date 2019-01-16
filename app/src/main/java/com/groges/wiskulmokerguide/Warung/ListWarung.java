package com.groges.wiskulmokerguide.Warung;

import com.groges.wiskulmokerguide.ProfileUser.PosisiUser;

public class ListWarung {
    private String Id, Nama, Alamat, Telpon, UriImgTempat;
    private Double Jarak,Lat,Lng;
    private Double Rating;

    public ListWarung() {
    }

    public ListWarung(String id, String nama, String alamat, String telpon, String uriImgTempat,  Double lat, Double lng, Double rating) {
        Id = id;
        Nama = nama;
        Alamat = alamat;
        UriImgTempat = uriImgTempat;
        Telpon = telpon;
        Lat = lat;
        Lng = lng;


        Jarak = distFrom(PosisiUser.getLat(), PosisiUser.getLng(),lat,lng)/1000;
        Rating = rating;
    }

    public String getId() {
        return Id;
    }

    public String getNama() {
        return Nama;
    }

    public String getAlamat() {
        return Alamat;
    }

    public String getTelpon() {
        return Telpon;
    }

    public String getUriImgTempat() {
        return UriImgTempat;
    }

    public Double getJarak() {
        return Jarak;
    }

    public Double getLat() {
        return Lat;
    }

    public Double getLng() {
        return Lng;
    }

    public Float getRating() {
        if (Rating!=null) {
            return Rating.floatValue();
        }else{
            return null;
        }
    }
    public static double distFrom (Double lat1, Double lng1, Double lat2, Double lng2 )
    {
        double earthRadius = 6371000;
        double dLat = Math.toRadians(lat2-lat1);
        double dLng = Math.toRadians(lng2-lng1);
        double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
                Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
                        Math.sin(dLng/2) * Math.sin(dLng/2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
        double dist = earthRadius * c;

        //int meterConversion = 1609;

        //return new Float(dist * meterConversion).floatValue();
        //return new Float(dist).floatValue();
        return dist;
    }

}
