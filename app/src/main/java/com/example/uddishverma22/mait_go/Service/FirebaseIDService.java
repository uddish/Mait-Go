package com.example.uddishverma22.mait_go.Service;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
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
        Globals.fcmRefreshToken = FirebaseInstanceId.getInstance().getToken();
        Preferences.setPrefs("fcmToken", FirebaseInstanceId.getInstance().getToken(), getApplicationContext());

        Log.d(TAG, "Refreshed token: " + Globals.fcmRefreshToken);

        // If you want to send messages to this application instance or
        // manage this apps subscriptions on the server side, send the
        // Instance ID token to your app server.
        sendRegistrationToServer(Globals.fcmRefreshToken);
    }

    private void sendRegistrationToServer(String refreshedToken) {
    }

}
