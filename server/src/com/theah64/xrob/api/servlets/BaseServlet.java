package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.utils.JSONUtils;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServlet;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by Shifar Shifz on 11/22/2015.
 */
public class BaseServlet extends HttpServlet {

    //Basic request paramaters
    protected static final String KEY_ERROR = "error";
    protected static final String KEY_MESSAGE = "message";
    protected static final String KEY_DATA_TYPE = "data_type";
    protected static final String KEY_DATA = "data"; //file Part


    protected static final String CONTENT_TYPE_JSON = "application/json";
    private static final String ERROR_GET_NOT_SUPPORTED = "GET method not supported";
    private static final String ERROR_POST_NOT_SUPPORTED = "POST method not supported";


    protected static void setGETMethodNotSupported(HttpServletResponse response) throws IOException {
        notSupported(ERROR_GET_NOT_SUPPORTED, response);
    }

    protected static void POSTMethodNotSupported(HttpServletResponse response) throws IOException {
        notSupported(ERROR_POST_NOT_SUPPORTED, response);
    }

    private static void notSupported(String methodErrorMessage, HttpServletResponse response) throws IOException {
        response.setContentType(CONTENT_TYPE_JSON);
        final PrintWriter out = response.getWriter();

        //GET Method not supported
        out.write(JSONUtils.getErrorJSON(methodErrorMessage));

        out.flush();
        out.close();
    }
}
