<%@ page import="com.theah64.xrob.api.database.tables.Clients" %>
<%@ page import="com.theah64.xrob.api.models.Client" %>
<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 12/9/16
  Time: 4:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Client Sign in</title>
    <%@include file="../common_headers.jsp" %>
</head>
<body>
<div class="container">

    <div class="row">
        <h1 class="text-center">Sign in</h1>
    </div>

    <div class="row">
        <div class="col-md-4  content-centered">

            <%--Form--%>
            <form role="form" action="signin.jsp" method="POST">

                <div class="form-group">
                    <label for="iUsername">Username : </label>
                    <input type="text" id="iUsername" class="form-control" placeholder="Username"/>
                </div>


                <div class="form-group">
                    <label for="iPassword">Password : </label>
                    <input type="password" id="iPassword" class="form-control" placeholder="Password"/>
                </div>

                <button type="submit" class="btn btn-primary pull-right">Sign in</button>

            </form>


            <%
                if (session.getAttribute("authorization") != null) {
                    response.sendRedirect("/index.jsp");
                    return;
                }

                final String username = request.getParameter("username");
                final String password = request.getParameter("password");

                if (username != null && password != null) {

                    final Client theClient = Clients.getInstance().get(Clients.COLUMN_USERNAME, username, Clients.COLUMN_PASS_HASH, password);

                    if (theClient != null) {
                        session.setAttribute(Clients.COLUMN_ID, theClient.getId());
                        response.sendRedirect("index.jsp");
                    } else {
            %>
            <div class="text-danger">Invalid credentials!</div>
            <%
                    }
                }
            %>


        </div>
    </div>
</div>
</body>
</html>
