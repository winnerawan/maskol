package com.groges.wiskulmokerguide.DetailKuliner;

public class ListUlasan {
    private String Id, NmPengguna, IdKuliner,Ulasan;
    private Float Rating;

    public ListUlasan() {
    }

    public ListUlasan(String id, String nmPengguna, String idKuliner, String ulasan, Float rating) {
        Id = id;
        NmPengguna = nmPengguna;
        IdKuliner = idKuliner;
        Ulasan = ulasan;
        Rating = rating;
    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNmPengguna() {
        return NmPengguna;
    }

    public void setNmPengguna(String nmPengguna) {
        NmPengguna = nmPengguna;
    }

    public String getIdKuliner() {
        return IdKuliner;
    }

    public void setIdKuliner(String idKuliner) {
        IdKuliner = idKuliner;
    }

    public String getUlasan() {
        return Ulasan;
    }

    public void setUlasan(String ulasan) {
        Ulasan = ulasan;
    }

    public Float getRating() {
        return Rating;
    }

    public void setRating(Float rating) {
        Rating = rating;
    }
}
