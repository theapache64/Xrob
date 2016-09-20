package com.theah64.xrob.api;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

/**
 * Created by theapache64 on 20/9/16,11:44 PM.
 */
public class FileWalker {

    private static final String KEY_FILES = "files";
    private static final String KEY_SIZE = "size";
    private static final String KEY_NAME = "name";

    private final File root;

    public FileWalker(String root) {
        this.root = new File(root);
    }

    public JSONArray walk() throws JSONException {
        return read(scan(this.root));
    }

    public static class FileNode {

        private final String name;
        private final List<FileNode> files;
        private final long sizeInKb;

        public FileNode(String name, List<FileNode> files, long sizeInKb) {
            this.name = name;
            this.files = files;
            this.sizeInKb = sizeInKb;
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
