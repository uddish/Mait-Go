package com.example.uddishverma22.mait_go.Models;

import io.realm.RealmObject;

/**
 * Created by uddishverma22 on 05/04/17.
 */

public class Notice extends RealmObject{

    public String url;
    public String notice;

//    public Notice() {
//    }
//
//    public Notice(String notice, String url) {
//        this.notice = notice;
//        this.url = url;
//    }
//
    public String getNotice() {
        return notice;
    }

    public void setNotice(String notice) {
        this.notice = notice;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
