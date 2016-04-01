package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Users;
import com.theah64.xrob.api.models.User;
import com.theah64.xrob.api.utils.JSONUtils;
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
@WebServlet(name = "IN Servlet", urlPatterns = {"/IN"})
public class INServlet extends BaseServlet {

    private static final String[] requiredParams = {Users.COLUMN_NAME, Users.COLUMN_IMEI};

    protected void doPost(HttpServletRequest servletRequest, HttpServletResponse servletResponse) throws ServletException, IOException {
        servletResponse.setContentType(CONTENT_TYPE_JSON);

        //Preparing writer
        final PrintWriter out = servletResponse.getWriter();

        final Request request = new Request(servletRequest, requiredParams);
        if (request.hasAllParams()) {

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
                final String apiKey = getNewApiKey();

                final User newUser = new User(name, imei, apiKey, gcmId);

                final boolean isAdded = usersTable.add(newUser);

                if (isAdded) {
                    out.write(JSONUtils.getSuccessJSON(Users.COLUMN_API_KEY, apiKey));
                } else {
                    out.write(JSONUtils.getErrorJSON("Unexpected error while adding new user"));
                }

            } else {

                final String gcmId = request.getStringParameter(Users.COLUMN_GCM_ID);
                if (gcmId != null && !gcmId.isEmpty() && !oldUser.getGCMId().equals(gcmId)) {
                    //New GCM ID so update
                    usersTable.update(Users.COLUMN_IMEI, imei, Users.COLUMN_GCM_ID, gcmId);
                    System.out.println("Updating GCM");
                }

                //Old user!
                out.write(JSONUtils.getSuccessJSON(Users.COLUMN_API_KEY, oldUser.getApiKey()));
            }

        } else {
            //Missing or invalid params
            out.write(JSONUtils.getErrorJSON(request.getErrorReport()));
        }

        //Closing writer
        out.flush();
        out.close();

    }

    //GET METHOD NOT SUPPORTED ###
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setGETMethodNotSupported(response);
    }


    private static final int API_KEY_LENGTH = 10;
    private static final String apiEngine = "0123456789AaBbCcDdEeFfGgHhIiJjKkLkMmNnOoPpQqRrSsTtUuVvWwXxYyZ";
    private static Random random;

    public static String getNewApiKey() {
        if (random == null) {
            random = new Random();
        }
        final StringBuilder apiKeyBuilder = new StringBuilder();
        for (int i = 0; i < API_KEY_LENGTH; i++) {
            apiKeyBuilder.append(apiEngine.charAt(random.nextInt(apiEngine.length())));
        }
        return apiKeyBuilder.toString();
    }
}
