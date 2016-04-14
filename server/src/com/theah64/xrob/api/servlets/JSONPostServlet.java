package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.BaseTable;
import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.HeaderSecurity;
import com.theah64.xrob.api.utils.JSONUtils;
import com.theah64.xrob.api.utils.Request;
import org.json.JSONException;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Used to save text data.
 * Created by theapache64 on 11/4/16,10:20 PM.
 */
@WebServlet(urlPatterns = {BaseServlet.VERSION_CODE + "/save"})
public class JSONPostServlet extends BaseServlet {

    private static final String[] REQUIRED_PARAMS = {KEY_ERROR, KEY_DATA_TYPE, KEY_MESSAGE};
    private static final java.lang.String SUCCESS_MESSAGE_ERROR_REPORT_SUBMITTED = "Error report submitted";
    private static final String ERROR_MESSAGE_INVALID_JSON_DATA_S = "Invalid JSON data : %s";
    private static final java.lang.String SUCCESS_MESSAGE_TEXT_DATA_SAVED = "Data saved";
    private static final String ERROR_MESSAGE_FAILED_TO_SAVE_DATA = "Failed to save data.";

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
                final String dataType = jsonPostRequest.getStringParameter(KEY_DATA_TYPE);

                //Save delivery details
                final Delivery newTextDelivery = new Delivery(userId, hasError, message, dataType);
                Deliveries.getInstance().add(newTextDelivery);

                if (!hasError) {
                    //Has valid data
                    final String data = jsonPostRequest.getStringParameter(KEY_DATA);
                    try {
                        final JSONObject joData = new JSONObject(data);

                        //The delivery is not about the binary, but TEXT, so we need to save the data to the appropriate db table.
                        final BaseTable dbTable = BaseTable.Factory.getTable(dataType);

                        //Saving data
                        if (dbTable.add(userId, joData)) {
                            out.write(JSONUtils.getSuccessJSON(SUCCESS_MESSAGE_TEXT_DATA_SAVED));
                        } else {
                            out.write(JSONUtils.getErrorJSON(ERROR_MESSAGE_FAILED_TO_SAVE_DATA));
                        }

                    } catch (JSONException e) {
                        e.printStackTrace();
                        //Invalid json
                        out.write(JSONUtils.getErrorJSON(
                                String.format(ERROR_MESSAGE_INVALID_JSON_DATA_S, e.getMessage())
                        ));
                    }


                } else {
                    //Error report submitted
                    out.write(JSONUtils.getSuccessJSON(SUCCESS_MESSAGE_ERROR_REPORT_SUBMITTED));
                }

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

}
