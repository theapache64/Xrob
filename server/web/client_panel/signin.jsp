<%@ page import="com.theah64.xrob.api.database.tables.Clients" %>
<%@ page import="com.theah64.xrob.api.models.Client" %>
<%@ page import="com.theah64.xrob.api.utils.DarKnight" %>
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
            <form action="signin.jsp" method="POST" role="form">

                <div class="form-group">
                    <label for="iUsername">Username : </label>
                    <input name="username" type="text" id="iUsername" class="form-control" placeholder="Username"/>
                </div>


                <div class="form-group">
                    <label for="iPassword">Password : </label>
                    <input name="password" type="password" id="iPassword" class="form-control" placeholder="Password"/>
                </div>

                <div class="row">

                    <div class="col-md-8">
                        <%

                            if (session.getAttribute(Clients.COLUMN_ID) != null) {
                                response.sendRedirect("/index.jsp");
                                return;
                            }

                            final String username = request.getParameter(Clients.COLUMN_USERNAME);
                            final String password = request.getParameter("password");


                            if (username != null && password != null) {

                                final Client theClient = Clients.getInstance().get(Clients.COLUMN_USERNAME, username, Clients.COLUMN_PASS_HASH, DarKnight.getEncrypted(password));

                                if (theClient != null) {
                                    System.out.println(theClient.toString());
                                    session.setAttribute(Clients.COLUMN_ID, theClient.getId());
                                    response.sendRedirect("index.jsp");
                                } else {
                        %>
                        <div class="text-danger pull-left">Invalid credentials!!</div>

                        <%
                                }
                            }
                        %>
                    </div>


                    <div class="col-md-4">
                        <button type="submit" class="btn btn-primary pull-right">Sign in</button>
                    </div>


                </div>


            </form>


        </div>
    </div>
</div>
</body>
</html>
