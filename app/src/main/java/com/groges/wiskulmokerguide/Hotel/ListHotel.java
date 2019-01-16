package com.groges.wiskulmokerguide.Hotel;

public class ListHotel {
    private String Id, Nama,alamat, UriImg, ShortDesc, LongDesc;
    private Double Lat,Lng;

    public ListHotel() {
    }

    public ListHotel(String id, String nama, String alamat, String uriImg, String shortDesc, String longDesc, Double lat, Double lng) {
        Id = id;
        Nama = nama;
        this.alamat = alamat;
        UriImg = uriImg;
        ShortDesc = shortDesc;
        LongDesc = longDesc;
        Lat = lat;
        Lng = lng;
    }

    public String getId() {
        return Id;
    }

    public String getNama() {
        return Nama;
    }

    public String getAlamat() {
        return alamat;
    }

    public String getUriImg() {
        return UriImg;
    }

    public String getShortDesc() {
        return ShortDesc;
    }

    public String getLongDesc() {
        return LongDesc;
    }

    public Double getLat() {
        return Lat;
    }

    public Double getLng() {
        return Lng;
    }
}
