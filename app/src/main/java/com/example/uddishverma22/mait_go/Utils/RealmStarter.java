package com.example.uddishverma22.mait_go.Utils;

import android.app.Application;

import io.realm.Realm;
import io.realm.RealmConfiguration;

/**
 * Created by uddishverma22 on 19/04/17.
 * This class initialises when the app is opened
 * and creates a Realm Reference
 */

public class RealmStarter extends Application{

    @Override
    public void onCreate() {
        super.onCreate();

        Realm.init(this);

        RealmConfiguration config = new RealmConfiguration
                .Builder()
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();

        Realm.setDefaultConfiguration(config);
    }
}
