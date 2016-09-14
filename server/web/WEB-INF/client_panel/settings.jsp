<%@ page import="com.theah64.xrob.api.database.tables.Deliveries" %>
<%@ page import="com.theah64.xrob.api.database.tables.Victims" %>
<%@ page import="com.theah64.xrob.api.models.Delivery" %>
<%@ page import="com.theah64.xrob.api.models.Victim" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.HtmlTemplates" %>
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
    <%

    %>
    <title>Settings</title>
    <%@include file="../common_headers.jsp" %>
    <%
        final HtmlTemplates.SearchTemplate searchTemplate = new HtmlTemplates.SearchTemplate("tDeliveries", "delivery_row");
    %>
    <script>
        $(document).ready(function () {


        });


    </script>
</head>
<body>
<div class="container">

    <%@include file="header.jsp" %>

    <div class="row text-center">
        <ul id="nav_menu" class="breadcrumb">
            <li><a href="/client/panel">Victims</a></li>
            </a></li>
            <li class="active">Settings</li>
        </ul>
    </div>


    <div class="row">
        <div class="table-bordered change-form-wrapper col-md-4">
            <%--Change username--%>
            <form data-toggle="validator" action="/client/settings" method="POST" role="form">
                </br>
                <h4>Change username</h4>
                </br>
                <div class="form-group">
                    <label for="iUsername">Username : </label>
                    <input value="<%=client.getUsername()%>" name="username" type="text"
                           pattern="<%=Clients.REGEX_USERNAME%>"
                           id="iUsername"
                           class="form-control"
                           placeholder="Username" required/>

                    <div class="help-block">Minimum 6 characters, maximum 20 characters, letters and numbers
                        only.
                    </div>
                </div>

                <input name="isUserNameChangeFormSubmitted" style="margin: 0  0px 10px 0" value="Change username"
                       type="submit" class="btn btn-danger pull-right"/>
            </form>

        </div>
        <div class="col-md-4 change-form-wrapper table-bordered">
            <%--Change email--%>
            <form data-toggle="validator" action="/client/settings" method="POST" role="form">
                </br>
                <h4>Change email</h4>
                </br>

                <div class="form-group">
                    <label for="iEmail">Email : </label>
                    <input value="<%=client.getEmail()%>" name="email" type="email" id="iEmail" class="form-control"
                           placeholder="Your email address"
                           data-error="Whooops, that email address is invalid"
                           required/>

                    <div class="help-block with-errors">Enter your new email address</div>
                </div>

                <input name="isEmailChangeFormSubmitted" style="margin: 0  0px 10px 0" value="Change email"
                       type="submit" class="btn btn-danger pull-right"/>
            </form>
        </div>

        <div class="col-md-4 change-form-wrapper table-bordered">
            <%--Change client code--%>
            <form data-toggle="validator" action="/client/settings" method="POST" role="form">
                </br>
                <h4>Change client code</h4>
                </br>

                <div class="form-group">
                    <label for="iEmail">Client code : </label>
                    <input value="<%=client.getClientCode()%>" name="client_code" type="text" id="iClientCode"
                           class="form-control"
                           data-minlength="10"
                           maxlength="10"
                           pattern="<%=Clients.REGEX_CLIENT_CODE%>"
                           placeholder="10 Digit code"
                           data-error="Whooops, that can't be a client code"
                           required/>

                    <div class="help-block with-errors">Enter your new client code</div>
                </div>

                <input name="isClientCodeChangeFormSubmitted" style="margin: 0  0px 10px 0" value="Change client code"
                       type="submit" class="btn btn-danger pull-right"/>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12 change-form-wrapper table-bordered">
            <%--Change pasword--%>
            <form data-toggle="validator" class="form-inline" action="/client/settings" method="POST" role="form">
                </br>
                <h4>Change password</h4>
                </br>

                <div class="form-group">
                    <label for="iPassword">Old password : </label>
                    <input name="npassword" type="password" data-minlength="6" id="iPassword"
                           class="form-control"
                           placeholder="Current password" required/>

                    <div class="help-block with-errors">Current password</div>
                </div>

                <div class="form-group">
                    <label for="iNewPassword">New password : </label>
                    <input name="npassword" type="password" data-minlength="6" id="iNewPassword"
                           class="form-control"
                           placeholder="Password" required/>

                    <div class="help-block with-errors">Minimum 6 characters</div>
                </div>

                <div class="form-group">
                    <label for="iNewCPassword">Confirm : </label>
                    <input name="cnpassword" type="password" id="iNewCPassword" class="form-control"
                           placeholder="Confirm password" data-match="#iNPassword"
                           data-match-error="Password doesn't match" required/>

                    <div class="help-block with-errors">&nbsp;</div>
                </div>

                <input name="isPasswordChangeFormSubmitted" style="margin: 0  0px 10px 0" value="Change password"
                       type="submit" class="btn btn-danger pull-right"/>
            </form>
        </div>
    </div>
    <br><br><br><br><br><br>
</div>
</body>
</html>
