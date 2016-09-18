package com.theah64.xrob.api;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 10/13/2015.
 */
public class TestMain {




    public static void main(String[] args) throws JSONException {
        final File root = new File("lab/testdir");
        final List<FileNode> fileNodes = scan(root);
        //Collections.reverse(fileNodes);
        JSONArray jaFiles = read(fileNodes);

    }

    public static class FileNode {
        private final String id, name;
        private final List<FileNode> files;
        private final long sizeInKb;

        public FileNode(String id, String name, List<FileNode> files, long sizeInKb) {
            this.id = id;
            this.name = name;
            this.files = files;
            this.sizeInKb = sizeInKb;
        }

        public String getId() {
            return id;
        }

        public String getName() {
            return name;
        }

        public List<FileNode> getFiles() {
            return files;
        }

        public long getSizeInKb() {
            return sizeInKb;
        }
    }

    private static JSONArray read(List<FileNode> fileNodes) throws JSONException {
        if (fileNodes != null) {
            final JSONArray jaFiles = new JSONArray();
            for (final FileNode fn : fileNodes) {
                final JSONObject joFn = new JSONObject();
                joFn.put("id", fn.getId());
                if (fn.getFiles() != null) {
                    joFn.put("files", read(fn.getFiles()));
                }
                joFn.put("size", fn.getSizeInKb());
                joFn.put("name", fn.getName());
                jaFiles.put(joFn);
            }
            return jaFiles;
        }
        return null;
    }

    private static int id = 1;

    private static List<FileNode> scan(File root) {
        if (root.isDirectory()) {
            final List<FileNode> rootFileNodes = new ArrayList<>();
            for (final File file : root.listFiles()) {
                rootFileNodes.add(new FileNode(id++ + "", file.getName(), scan(file), (file.length() / 1024)));
            }
            return rootFileNodes;
        }
        return null;
    }


}

