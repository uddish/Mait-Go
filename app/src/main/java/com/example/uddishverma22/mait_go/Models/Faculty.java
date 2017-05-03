package com.example.uddishverma22.mait_go.Models;

import io.realm.RealmObject;

/**
 * Created by uddishverma22 on 02/05/17.
 */

public class Faculty extends RealmObject {

    public String name;
    public String designation;
    public String qualification;
    public String experience;
    public String imageUrl;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDesignation() {
        return designation;
    }

    public void setDesignation(String designation) {
        this.designation = designation;
    }

    public String getQualification() {
        return qualification;
    }

    public void setQualification(String qualification) {
        this.qualification = qualification;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }
}
