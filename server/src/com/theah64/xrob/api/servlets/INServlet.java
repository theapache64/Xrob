package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Users;
import com.theah64.xrob.api.models.User;
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
 * Used to create new user
 * Needed params
 * 1) Name
 * 2) Imei
 * 3) GCM ID - Optional
 */
@WebServlet(name = "IN Servlet", urlPatterns = {BaseServlet.VERSION_CODE + "/IN"})
public class INServlet extends BaseServlet {

    private static final String[] requiredParams = {Users.COLUMN_NAME, Users.COLUMN_IMEI};
    private static final int API_KEY_LENGTH = 10;

    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        servletResponse.setContentType(CONTENT_TYPE_JSON);

        //Preparing writer
        final PrintWriter out = servletResponse.getWriter();

        try {
            final Request request = new Request(servletRequest, requiredParams);

            final String imei = request.getStringParameter(Users.COLUMN_IMEI);
            final Users usersTable = Users.getInstance();
            final User oldUser = usersTable.get(Users.COLUMN_IMEI, imei);

            if (oldUser == null) {

                //User doesn't exists, so create new account
                final String name = request.getStringParameter(Users.COLUMN_NAME);

                //GCM ID may be null
                String gcmId = request.getStringParameter(Users.COLUMN_GCM_ID);
                if (gcmId != null && gcmId.isEmpty()) {
                    gcmId = null;
                }

                //Preparing new api key for new user
                final String apiKey = RandomString.getNewApiKey(API_KEY_LENGTH);

                final User newUser = new User(name, imei, apiKey, gcmId);

                usersTable.addv2(newUser);
                out.write(JSONUtils.getSuccessJSON(Users.COLUMN_API_KEY, apiKey));

            } else {

                final String gcmId = request.getStringParameter(Users.COLUMN_GCM_ID);
                if (gcmId != null && !gcmId.isEmpty() && !oldUser.getGCMId().equals(gcmId)) {
                    //New GCM ID so update
                    usersTable.update(Users.COLUMN_IMEI, imei, Users.COLUMN_GCM_ID, gcmId);
                }

                //Old user!
                out.write(JSONUtils.getSuccessJSON(Users.COLUMN_API_KEY, oldUser.getApiKey()));
            }

        } catch (Exception e) {
            e.printStackTrace();
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
