package com.example.uddishverma22.mait_go.Models;

import org.json.JSONArray;

/**
 * Created by uddishverma22 on 07/05/17.
 */

public class AssignmentModel{

    public String imageUrl;
    public String subject;
    public String teacher;
    public String lastdate;
    public String marks;
    public JSONArray images;

    public String getMarks() {
        return marks;
    }

    public void setMarks(String marks) {
        this.marks = marks;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
    }

    public String getLastdate() {
        return lastdate;
    }

    public void setLastdate(String lastdate) {
        this.lastdate = lastdate;
    }
}
