package com.theah64.xrob.api.utils;

import javax.servlet.http.Part;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
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


    public static boolean upload(Part dataFilePart, String uploadUrl) throws IOException {

        HttpURLConnection httpUrlConnection = null;
        URL url = new URL("http://example.com/server.cgi");
        httpUrlConnection = (HttpURLConnection) url.openConnection();
        httpUrlConnection.setUseCaches(false);
        httpUrlConnection.setDoOutput(true);

        httpUrlConnection.setRequestMethod("POST");
        httpUrlConnection.setRequestProperty("Connection", "Keep-Alive");
        httpUrlConnection.setRequestProperty("Cache-Control", "no-cache");
        httpUrlConnection.setRequestProperty(
                "Content-Type", "multipart/form-data;boundary=" + boundary);


        DataOutputStream request = new DataOutputStream(
                httpUrlConnection.getOutputStream());

        request.writeBytes(twoHyphens + boundary + crlf);
        request.writeBytes("Content-Disposition: form-data; name=\"" +
                dataFilePart.getName() + "\";filename=\"" +
                dataFilePart.getName() + "\"" + crlf);
        request.writeBytes(crlf);

        final InputStream is = dataFilePart.getInputStream();
        byte[] buffer = new byte[1024];
        int len = 0;
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

        return false;

    }
}
