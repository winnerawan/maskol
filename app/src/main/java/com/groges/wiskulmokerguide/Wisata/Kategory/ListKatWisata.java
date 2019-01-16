package com.groges.wiskulmokerguide.Wisata.Kategory;

public class ListKatWisata {
    private String Id, Kategory,UriImg,Ket;
    public ListKatWisata() {
    }

    public ListKatWisata(String id, String kategory, String uriImg,String ket) {
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
