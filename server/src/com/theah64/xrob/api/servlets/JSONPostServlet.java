package com.theah64.xrob.api.servlets;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by theapache64 on 11/4/16,10:20 PM.
 */
public class JSONPostServlet extends BaseServlet {

    private static final String[] REQUIRED_PARAMS = {KEY_ERROR, KEY_MESSAGE};

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(CONTENT_TYPE_JSON);

        


    }


    /**
     * //The delivery is not about the binary, but TEXT, so we need to save the data to the appropriate db table.
     final BaseTable dbTable = BaseTable.Factory.getTable(dataType);
     final String fileContents = getFileContents(dataFilePart.getInputStream());

     try {
     if (dbTable.add(headerSecurity.getUserId(), new JSONObject(fileContents))) {
     out.write(JSONUtils.getSuccessJSON(SUCCESS_MESSAGE_TEXT_DATA_SAVED));
     } else {
     out.write(JSONUtils.getErrorJSON(ERROR_MESSAGE_FAILED_TO_SAVE_DATA));
     }

     } catch (JSONException e) {
     e.printStackTrace();
     out.write(JSONUtils.getErrorJSON("Error in JSON Package : " + e.getMessage()));
     }

     */
}
