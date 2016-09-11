package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.database.tables.Victims;
import com.theah64.xrob.api.models.Victim;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by theapache64 on 12/9/16.
 */
@WebServlet(urlPatterns = {BaseServlet.VERSION_CODE + "/update/fcm"}, name = "FCMUpdateServlet")
public class FCMUpdateServlet extends BaseServlet {

    private final String[] REQUIRED_PARAMS = {Victims.COLUMN_FCM_ID};

    protected void doPost(HttpServletRequest request, HttpServletResponse response) throws ServletException, IOException {
        response.setContentType(CONTENT_TYPE_JSON);


    }

}
