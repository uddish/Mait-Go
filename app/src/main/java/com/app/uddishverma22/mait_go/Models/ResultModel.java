package com.app.uddishverma22.mait_go.Models;

/**
 * Created by uddishverma22 on 12/04/17.
 */

public class ResultModel{

    public String subName;
    public String intMarks;
    public String extMarks;
    public String totMarks;
    public String credits;
    public String percentage;
    public String creditPerc;
    public String cgpa;
    public String univRank;
    public String colRank;
    public String sem;

    public ResultModel(String subName, String intMarks, String extMarks, String totMarks, String credits,
                       String percentage, String creditPerc, String cgpa, String univRank, String colRank) {
        this.subName = subName;
        this.intMarks = intMarks;
        this.extMarks = extMarks;
        this.totMarks = totMarks;
        this.credits = credits;
        this.percentage = percentage;
        this.cgpa = cgpa;
        this.creditPerc = creditPerc;
        this.univRank = univRank;
        this.colRank = colRank;
    }

    public ResultModel() {
    }

    public String getUnivRank() {
        return univRank;
    }

    public void setUnivRank(String univRank) {
        this.univRank = univRank;
    }

    public String getColRank() {
        return colRank;
    }

    public void setColRank(String colRank) {
        this.colRank = colRank;
    }

    public String getPercentage() {
        return percentage;
    }

    public void setPercentage(String percentage) {
        this.percentage = percentage;
    }

    public String getCreditPerc() {
        return creditPerc;
    }

    public void setCreditPerc(String creditPerc) {
        this.creditPerc = creditPerc;
    }

    public String getCgpa() {
        return cgpa;
    }

    public void setCgpa(String cgpa) {
        this.cgpa = cgpa;
    }

    public String getSubName() {
        return subName;
    }

    public void setSubName(String subName) {
        this.subName = subName;
    }

    public String getIntMarks() {
        return intMarks;
    }

    public void setIntMarks(String intMarks) {
        this.intMarks = intMarks;
    }

    public String getExtMarks() {
        return extMarks;
    }

    public void setExtMarks(String extMarks) {
        this.extMarks = extMarks;
    }

    public String getTotMarks() {
        return totMarks;
    }

    public void setTotMarks(String totMarks) {
        this.totMarks = totMarks;
    }

    public String getCredits() {
        return credits;
    }

    public void setCredits(String credits) {
        this.credits = credits;
    }
}
