package com.theah64.xrob.api.servlets;

import com.theah64.xrob.api.utils.APIResponse;
import com.theah64.xrob.api.utils.HeaderSecurity;
import com.theah64.xrob.api.utils.Request;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;

/**
 * Created by shifar on 16/9/16.
 */
public abstract class AdvancedBaseServlet extends BaseServlet {

    private Request request;
    private HeaderSecurity hs;

    @Override
    protected void doPost(HttpServletRequest req, HttpServletResponse resp) throws ServletException, IOException {
        resp.setContentType(getContentType());
        final PrintWriter out = resp.getWriter();
        try {
            if (isSecureServlet()) {
                hs = new HeaderSecurity(req.getHeader(HeaderSecurity.KEY_AUTHORIZATION));
            }

            if (getRequiredParameters() != null) {
                request = new Request(req, getRequiredParameters());
            }

            doAdvancedPost();

        } catch (Exception e) {
            e.printStackTrace();
            out.write(new APIResponse(e.getMessage()).toString());
        }

        out.flush();
        out.close();
    }


    protected String getContentType() {
        return CONTENT_TYPE_JSON;
    }

    protected abstract boolean isSecureServlet();

    protected abstract String[] getRequiredParameters();

    protected abstract void doAdvancedPost();

    protected Request getRequest() {
        return request;
    }

    public HeaderSecurity getHeaderSecurity() {
        if (!isSecureServlet()) {
            throw new IllegalArgumentException("It's not a secure servlet");
        }
        return hs;
    }
}
