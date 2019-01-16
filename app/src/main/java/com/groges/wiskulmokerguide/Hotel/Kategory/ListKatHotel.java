package com.groges.wiskulmokerguide.Hotel.Kategory;

public class ListKatHotel {
    private String Id, Kategory,UriImg,Ket;
    public ListKatHotel() {
    }

    public ListKatHotel(String id, String kategory, String uriImg, String ket) {
        Id = id;
        Kategory = kategory;
        UriImg = uriImg;
        Ket= ket;
    }

    public String getId() {
        return Id;
    }

    public String getKategory() {
        return Kategory;
    }

    public String getUriImg() {
        return UriImg;
    }

    public String getKet() {
        return Ket;
    }

}
