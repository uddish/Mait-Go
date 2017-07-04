package com.example.uddishverma22.mait_go.Service;

import android.content.Intent;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;

import com.example.uddishverma22.mait_go.Utils.Globals;
import com.example.uddishverma22.mait_go.Utils.Preferences;
import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;

public class FirebaseIDService extends FirebaseInstanceIdService {
    
    public static final String TAG = "MyFirebaseInstance";

    @Override
    public void onTokenRefresh() {
        // Get updated InstanceID token.
        String token = FirebaseInstanceId.getInstance().getToken();

        Preferences.setPrefs("fcmToken", FirebaseInstanceId.getInstance().getToken(), getApplicationContext());

        sendRegistrationToServer(Globals.fcmRefreshToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {

    }

}
