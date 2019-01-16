package com.groges.wiskulmokerguide.Kuliner.Kategory;

public class ListKatKuliner {
    private String Id, Kategory,UriImg,Ket;
    public ListKatKuliner() {
    }

    public ListKatKuliner(String id, String kategory, String uriImg, String ket) {
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
