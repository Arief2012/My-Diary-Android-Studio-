package com.pmo.mydiary;

public class ReadWriteDataUser {
    public String alamat, notlp, tempatlahir, tanggallahir, gender;

    public  ReadWriteDataUser(){}

    public ReadWriteDataUser(String textalamat, String textnotlp, String texttempatlahir, String texttanggallahir, String textgender){
        this.alamat = textalamat;
        this.notlp = textnotlp;
        this.tempatlahir = texttempatlahir;
        this.tanggallahir = texttanggallahir;
        this.gender = textgender;
    }

}
