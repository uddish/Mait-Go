package com.example.uddishverma22.mait_go.Models;

/**
 * Created by uddishverma22 on 12/04/17.
 */

public class ResultModel {

    public String subName;
    public String intMarks;
    public String extMarks;
    public String totMarks;
    public String credits;
    public String percentage;

    public ResultModel(String subName, String intMarks, String extMarks, String totMarks, String credits, String percentage) {
        this.subName = subName;
        this.intMarks = intMarks;
        this.extMarks = extMarks;
        this.totMarks = totMarks;
        this.credits = credits;
        this.percentage = percentage;
    }

    public ResultModel() {
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
