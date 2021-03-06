package com.app.uddishverma22.mait_go.Models;

/**
 * Created by uddishverma22 on 04/04/17.
 * Contains the daily schedule of the student
 */

public class DailySchedule{

    String time;
    String subject;
    String room;
    String teacher;

    public DailySchedule()  {

    }

    public DailySchedule(String time, String subject, String room, String teacher) {
        this.time = time;
        this.subject = subject;
        this.room = room;
        this.teacher = teacher;
    }

    public String getTeacher() {
        return teacher;
    }

    public void setTeacher(String teacher) {
        this.teacher = teacher;
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
