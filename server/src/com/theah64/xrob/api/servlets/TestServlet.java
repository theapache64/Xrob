package com.theah64.xrob.api.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * Created by theapache64 on 10/13/2015.
 */
@WebServlet(urlPatterns = {"/lab"}, name = "TestServlet")
public class TestServlet extends AdvancedBaseServlet {

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        super.doPost(req, resp);
    }

    @Override
    protected boolean isSecureServlet() {
        return false;
    }

    @Override
    public String[] getRequiredParameters() {
        return new String[]{"key1", "key2"};
    }

    @Override
    protected void doAdvancedPost() {

    }

}
