package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.BaseTable;
import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.HeaderSecurity;
import com.theah64.xrob.api.utils.JSONUtils;
import com.theah64.xrob.api.utils.Request;
import org.json.JSONArray;
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

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);

        //out
        final PrintWriter out = resp.getWriter();


        try {

            final HeaderSecurity headerSecurity = new HeaderSecurity(req.getHeader(HeaderSecurity.KEY_AUTHORIZATION));

            //Checking if the request has every required parameter.
            final Request jsonPostRequest = new Request(req, REQUIRED_PARAMS);
            final String dataType = jsonPostRequest.getStringParameter(KEY_DATA_TYPE);

            //user id
            final String userId = headerSecurity.getUserId();
            final boolean hasError = jsonPostRequest.getBooleanParameter(KEY_ERROR);
            final String message = jsonPostRequest.getStringParameter(KEY_MESSAGE);

            final Delivery delivery = new Delivery(userId, hasError, message, dataType);
            final Deliveries deliveries = Deliveries.getInstance();

            try {

                if (!hasError) {

                    //Has valid data
                    final String data = jsonPostRequest.getStringParameter(KEY_DATA);

                    if (data != null) {

                        try {
                            final JSONArray jaData = new JSONArray(data);

                            //The delivery is not about the binary, but TEXT, so we need to save the data to the appropriate db table.
                            final BaseTable dbTable = BaseTable.Factory.getTable(dataType);


                            dbTable.addv2(userId, jaData);
                            //Save delivery details
                            deliveries.addv2(delivery);

                            out.write(JSONUtils.getSuccessJSON("Data saved"));


                        } catch (JSONException e) {
                            e.printStackTrace();

                            //Invalid json
                            throw new Delivery.DamagedPackageException(String.format("Invalid JSON data : %s", e.getMessage()));
                        }

                    } else {
                        //Data is null!
                        throw new Delivery.DamagedPackageException("Data can't be null");
                    }


                } else {
                    //Save delivery details
                    deliveries.addv2(delivery);
                    //Error report submitted
                    out.write(JSONUtils.getSuccessJSON("Error report submitted"));
                }

            } catch (Delivery.DamagedPackageException e) {
                e.printStackTrace();
                delivery.setServerError(true);
                delivery.setServerErrorMessage(e.getMessage());
                deliveries.addv2(delivery);
                throw new Exception(e.getMessage());
            }

        } catch (Exception e) {
            out.write(JSONUtils.getErrorJSON(e.getMessage()));
        }


        out.flush();
        out.close();
    }


}
