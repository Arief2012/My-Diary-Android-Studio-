package com.pmo.mydiary;

public class ModelActivity {
    private String tanggal;
    private String pagi;
    private String siang;
    private String malam;
    private String rbgrup;
    private String spkategori;
    private String judul;
    private String catatan;
    private String key;

    public ModelActivity() {

    }

    public ModelActivity(String tanggal, String pagi, String siang, String malam, String rbgrup, String spkategori, String judul, String catatan) {
        this.tanggal = tanggal;
        this.pagi = pagi;
        this.siang = siang;
        this.malam = malam;
        this.rbgrup = rbgrup;
        this.spkategori = spkategori;
        this.judul = judul;
        this.catatan = catatan;

    }

    public String getTanggal() {
        return tanggal;
    }

    public void setTanggal(String tanggal) {
        this.tanggal = tanggal;
    }

    public String getPagi() {
        return pagi;
    }

    public void setPagi(String pagi) {
        this.pagi = pagi;
    }

    public String getSiang() {
        return siang;
    }

    public void setSiang(String siang) {
        this.siang = siang;
    }

    public String getMalam() {
        return malam;
    }

    public void setMalam(String malam) {
        this.malam = malam;
    }

    public String getRbgrup() {
        return rbgrup;
    }

    public void setRbgrup(String rbgrup) {
        this.rbgrup = rbgrup;
    }

    public String getSpkategori() {
        return spkategori;
    }

    public void setSpkategori(String spkategori) {
        this.spkategori = spkategori;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }

    public String getCatatan() {
        return catatan;
    }

    public void setCatatan(String catatan) {
        this.catatan = catatan;
    }

    public String getKey() {
        return key;
    }

    public void setKey(String key) {
        this.key = key;
    }
}
