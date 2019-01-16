package com.groges.wiskulmokerguide;

public class ListMenu {
    private String Id, IdTempat, Nama, Tempat,UriImg, UriImgTempat,Ket;
    private Double Harga,Lat,Lng,Rating;

    public ListMenu() {
    }

    public ListMenu(String id,String idTempat, String nama, String tempat, String uriImg, String uriImgTempat, Double harga, Double lat, Double lng, Double rating, String ket) {
        Id = id;
        IdTempat= idTempat;
        Nama = nama;
        Tempat = tempat;
        UriImg = uriImg;
        UriImgTempat = uriImgTempat;
        Harga = harga;
        Lat = lat;
        Lng = lng;
        Ket= ket;
        Rating = rating;
    }

    public String getKet() {
        return Ket;
    }

    public Float getRating() {

        if (Rating!=null) {
            return Rating.floatValue();
        }else{
            return null;
        }

    }

    public Double getLat() {
        return Lat;
    }

    public Double getLng() {
        return Lng;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNama() {
        return Nama;
    }

    public void setNama(String nama) {
        Nama = nama;
    }

    public String getTempat() {
        return Tempat;
    }

    public void setTempat(String tempat) {
        Tempat = tempat;
    }

    public String getUriImg() {
        return UriImg;
    }

    public void setUriImg(String uriImg) {
        UriImg = uriImg;
    }

    public String getUriImgTempat() {
        return UriImgTempat;
    }

    public void setUriImgTempat(String uriImgTempat) {
        UriImgTempat = uriImgTempat;
    }

    public Double getHarga() {
        return Harga;
    }

    public void setHarga(Double harga) {
        Harga = harga;
    }

    public String getIdTempat() {
        return IdTempat;
    }
}
