package com.theah64.xrob.api.servlets;

import javax.servlet.ServletException;
import javax.servlet.annotation.WebServlet;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.IOException;

/**
 * Created by theapache64 on 10/13/2015.
 */
@WebServlet(urlPatterns = {"/lab"}, name = "TestServlet")
public class TestServlet extends HttpServlet {

    @Override
    protected void doGet(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType("application/json");
        final File root = new File(System.getProperty("user.dir") + File.separator + "testdir");
        resp.getWriter().write(scan(root));
    }

    private String scan(File root) {
        if (root.isDirectory()) {
            for (final File f : root.listFiles()) {
                return scan(f);
            }

        } else {
            return root.getAbsolutePath();
        }
    }


}
