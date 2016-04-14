package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.HeaderSecurity;
import com.theah64.xrob.api.utils.JSONUtils;
import com.theah64.xrob.api.utils.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Used to save text data.
 * Created by theapache64 on 11/4/16,10:20 PM.
 */
public class JSONPostServlet extends BaseServlet {

    private static final String[] REQUIRED_PARAMS = {KEY_ERROR, KEY_MESSAGE};

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);

        //out
        final PrintWriter out = resp.getWriter();

        //To check API key
        final HeaderSecurity headerSecurity = new HeaderSecurity(req.getHeader(HeaderSecurity.KEY_AUTHORIZATION));

        if (headerSecurity.isAuthorized()) {

            //Checking if the request has every required parameter.
            final Request jsonPostRequest = new Request(req, REQUIRED_PARAMS);

            if (jsonPostRequest.hasAllParams()) {

                //user id
                final String userId = headerSecurity.getUserId();
                final boolean hasError = jsonPostRequest.getBooleanParameter(KEY_ERROR);
                final String message = jsonPostRequest.getStringParameter(KEY_MESSAGE);

                final Delivery newTextDelivery = new Delivery(userId, hasError, message);
                Deliveries.getInstance().add(newTextDelivery);

            } else {
                //Missing param
                out.write(JSONUtils.getErrorJSON(jsonPostRequest.getErrorReport()));
            }


        } else {
            //Unauthorized request
            out.write(JSONUtils.getUnAuthorizedRequestError());
        }

        out.flush();
        out.close();
    }


    /**
     * //The delivery is not about the binary, but TEXT, so we need to save the data to the appropriate db table.
     final BaseTable dbTable = BaseTable.Factory.getTable(dataType);
     final String fileContents = getFileContents(dataFilePart.getInputStream());

     try {
     if (dbTable.add(headerSecurity.getUserId(), new JSONObject(fileContents))) {
     out.write(JSONUtils.getSuccessJSON(SUCCESS_MESSAGE_TEXT_DATA_SAVED));
     } else {
     out.write(JSONUtils.getErrorJSON(ERROR_MESSAGE_FAILED_TO_SAVE_DATA));
     }

     } catch (JSONException e) {
     e.printStackTrace();
     out.write(JSONUtils.getErrorJSON("Error in JSON Package : " + e.getMessage()));
     }

     */
}
