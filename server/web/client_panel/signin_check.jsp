<%@ page import="com.theah64.xrob.api.models.Client" %>
<%@ page import="com.theah64.xrob.api.database.tables.Clients" %>
<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 30/8/16
  Time: 10:32 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title></title>
</head>
<body>
<%
    if (session.getAttribute(Clients.COLUMN_CLIENT_CODE) == null) {
        response.sendRedirect("signin.jsp");
        return;
    }
%>
</body>
</html>
