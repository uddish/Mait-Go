package com.app.uddishverma22.mait_go.Models;

/**
 * Created by uddishverma on 07/07/17.
 */

public class ResultHeader {

    public String creditPerc;
    public String cgpa;
    public String univRank;
    public String colRank;

    public ResultHeader(String creditPerc, String cgpa, String univRank, String colRank) {
        this.creditPerc = creditPerc;
        this.cgpa = cgpa;
        this.univRank = univRank;
        this.colRank = colRank;
    }

    public ResultHeader() {
    }
}
