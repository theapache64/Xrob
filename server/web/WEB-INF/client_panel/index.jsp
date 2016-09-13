<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.database.tables.Victims" %>
<%@ page import="com.theah64.xrob.api.utils.CommonUtils" %>
<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 12/9/16
  Time: 4:08 PM
  To change this template use File | Settings | File Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<%@include file="signin_check.jsp" %>

<html>
<head>
    <title>Client panel</title>
    <%@include file="../common_headers.jsp" %>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>

    <%
        final List<Victim> victims = Clients.getInstance().getVictims(session.getAttribute(Clients.COLUMN_ID).toString());
        if (victims != null) {

    %>

    <%--Generate table here--%>

    <h4>You've <%=CommonUtils.getProperSentense(victims.size(), "one victim", victims.size() + " victims")%>
    </h4>

    <div class="table-responsive">
        <table class="table table-bordered table-striped">
            <tr>
                <th>Name</th>
                <th >Email</th>
                <th>Phone</th>
                <th>IMEI</th>
                <th>Device</th>
                <th ></th>
                <%-- <th>Device info</th>--%>
            </tr>

            <%
                for (final Victim victim : victims) {
            %>
            <tr>
                <td><%=victim.getName()%>
                </td>
                <td><%=victim.getEmail()%>
                </td>
                <td><%=victim.getPhone()%>
                </td>
                <td><%=victim.getIMEI()%>
                </td>
                <td><%=victim.getDeviceName()%>
                    <a data-toggle="collapse"
                       data-target="#<%=victim.getId()%>">more...
                    </a>

                    <div id="<%=victim.getId()%>" class="collapse">
                        <%=Victim.getDeviceInfoReadable(victim.getOtherDeviceInfo())%>
                    </div>
                </td>
                <td>

                    <a href="/client/victim/profile/<%=victim.getVictimCode()%>" type="button" class="btn btn-default">
                        <span class="glyphicon glyphicon-eye-open"></span> View
                    </a>
                </td>
                <%--<td><%=Victim.getDeviceInfoReadable(victim.getOtherDeviceInfo())%>
                </td>--%>
            </tr>
            <%
                }
            %>

        </table>
    </div>


    <%


    } else {
    %>

    <div class="row">
        <h1 class="text-danger text-center">No victim connected!</h1>
    </div>


    <%
        }
    %>

</div>
</body>
</html>
