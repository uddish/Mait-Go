package com.example.uddishverma22.mait_go.Models;

/**
 * Created by uddishverma22 on 04/04/17.
 * Contains the daily schedule of the student
 */

public class DailySchedule {

    String time;
    String subject;
    String room;

    public DailySchedule(String time, String subject, String room) {
        this.time = time;
        this.subject = subject;
        this.room = room;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getRoom() {
        return room;
    }

    public void setRoom(String room) {
        this.room = room;
    }
}
