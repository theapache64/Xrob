package com.theah64.xrob.api.utils;

import com.theah64.xrob.api.models.Server;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.http.Part;
import java.io.*;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;

/**
 * Created by theapache64 on 27/10/16.
 */
public class FileUploader {

    private static final String crlf = "\r\n";
    private static final String twoHyphens = "--";
    private static final String boundary = "*****";


    public static JSONObject upload(FilePart filePart, final Server server) throws IOException, JSONException {

        HttpURLConnection httpUrlConnection = null;
        final URL url = new URL(server.getUploadUrl());

        httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setDoOutput(true);

        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setRequestProperty("Authorization", server.getAuthorization());
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
        httpUrlConnection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);


        DataOutputStream request = new DataOutputStream(
                httpUrlConnection.getOutputStream());

        final Part dataFilePart = filePart.getDataFilePart();
        System.out.println("FileNme: " + dataFilePart.getName());

        final String randomFileName = filePart.getRandomFileName();

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                "file\";filename=\"" +
                randomFileName + "\"" + crlf);
        request.writeBytes(crlf);

        final InputStream is = dataFilePart.getInputStream();
        byte[] buffer = new byte[1024];
        int len;
        while ((len = is.read(buffer)) != -1) {
            request.write(buffer, 0, len);
        }
        is.close();

        request.writeBytes(crlf);
        request.writeBytes(twoHyphens + boundary +
                twoHyphens + crlf);

        request.flush();
        request.close();

        //TODO: Handle server response here
        final BufferedReader bis = new BufferedReader(new InputStreamReader(httpUrlConnection.getInputStream()));
        final StringBuilder sb = new StringBuilder();
        String line;
        while ((line = bis.readLine()) != null) {
            sb.append(line).append("\n");
        }
        bis.close();

        return new JSONObject(sb.toString());
    }
}
