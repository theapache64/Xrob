<%@ page import="com.theah64.xrob.api.utils.DarKnight" %>
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

            <%
                try {
                    final boolean isUserNameChangeFormSubmitted = request.getParameter("isUserNameChangeFormSubmitted") != null;
                    if (isUserNameChangeFormSubmitted) {
                        final String newUsername = request.getParameter(Clients.COLUMN_USERNAME);
                        if (newUsername != null && newUsername.matches(Clients.REGEX_USERNAME)) {
                            if (!newUsername.equals(client.getUsername())) {
                                final Clients clients = Clients.getInstance();
                                final boolean isExist = clients.isExist(Clients.COLUMN_USERNAME, newUsername);
                                if (!isExist) {
                                    final boolean isChanged = clients.update(Clients.COLUMN_ID, clientId.toString(), Clients.COLUMN_USERNAME, newUsername);
                                    if (isChanged) {
                                        client.setUsername(newUsername);
            %>
            <p class="text text-success change-settings-result">Username changed!
            </p>
            <%
                                } else {
                                    throw new Exception("Failed to change the username!");
                                }
                            } else {
                                throw new Exception(newUsername + " already exist!");
                            }
                        } else {
                            throw new Exception("Same as old!");
                        }
                    } else {
                        throw new Exception("Invalid username");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            %>
            <p class="text text-danger change-settings-result"><%=e.getMessage()%>
            </p>
            <%
                }
            %>

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

                <input name="isUserNameChangeFormSubmitted" style="margin:0 0 10px 0" value="Change username"
                       type="submit" class="btn btn-danger pull-right"/>
            </form>


        </div>
        <div class="col-md-4 change-form-wrapper change-form-wrapper-plus table-bordered">

            <%
                try {
                    final boolean isEmailChangeFormSubmitted = request.getParameter("isEmailChangeFormSubmitted") != null;
                    if (isEmailChangeFormSubmitted) {
                        final String newEmail = request.getParameter(Clients.COLUMN_EMAIL);
                        if (newEmail != null && newEmail.matches(Clients.REGEX_EMAIL)) {
                            if (!newEmail.equals(client.getEmail())) {
                                final Clients clients = Clients.getInstance();
                                final boolean isExist = clients.isExist(Clients.COLUMN_EMAIL, newEmail);
                                if (!isExist) {
                                    final boolean isChanged = clients.update(Clients.COLUMN_ID, clientId.toString(), Clients.COLUMN_EMAIL, newEmail);
                                    if (isChanged) {
                                        client.setEmail(newEmail);
            %>
            <p class="text text-success change-settings-result">Email changed!
            </p>
            <%
                                } else {
                                    throw new Exception("Failed to change the email!");
                                }
                            } else {
                                throw new Exception(newEmail + " already exist!");
                            }
                        } else {
                            throw new Exception("Same as old!");
                        }
                    } else {
                        throw new Exception("Invalid email");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            %>
            <p class="text text-danger change-settings-result"><%=e.getMessage()%>
            </p>
            <%
                }
            %>

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

                <input name="isEmailChangeFormSubmitted" style="margin: 0  0 10px 0" value="Change email"
                       type="submit" class="btn btn-danger pull-right"/>
            </form>
        </div>

        <div class="col-md-4 change-form-wrapper change-form-wrapper-plus table-bordered">

            <%
                try {
                    final boolean isClientCodeChangeFormSubmitted = request.getParameter("isClientCodeChangeFormSubmitted") != null;
                    if (isClientCodeChangeFormSubmitted) {
                        final String newClientCode = request.getParameter(Clients.COLUMN_CLIENT_CODE);
                        if (newClientCode != null && newClientCode.matches(Clients.REGEX_CLIENT_CODE)) {
                            if (!newClientCode.equals(client.getClientCode())) {
                                final Clients clients = Clients.getInstance();
                                final boolean isExist = clients.isExist(Clients.COLUMN_CLIENT_CODE, newClientCode);
                                if (!isExist) {
                                    final boolean isChanged = clients.update(Clients.COLUMN_ID, clientId.toString(), Clients.COLUMN_CLIENT_CODE, newClientCode);
                                    if (isChanged) {
                                        client.setClientCode(newClientCode);
            %>
            <p class="text text-success change-settings-result">Client code changed!
            </p>
            <%
                                } else {
                                    throw new Exception("Failed to change the client code!");
                                }
                            } else {
                                throw new Exception(newClientCode + " already exist!");
                            }
                        } else {
                            throw new Exception("Same as old!");
                        }
                    } else {
                        throw new Exception("Invalid client code");
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            %>
            <p class="text text-danger change-settings-result"><%=e.getMessage()%>
            </p>
            <%
                }
            %>

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
                    <input name="old_password" type="password" data-minlength="4" id="iPassword"
                           class="form-control"
                           placeholder="Current password" required/>

                    <div class="help-block with-errors">Current password</div>
                </div>

                <div class="form-group">
                    <label for="iNewPassword">New password : </label>
                    <input name="password" type="password" data-minlength="4" id="iNewPassword"
                           class="form-control"
                           placeholder="Password" required/>

                    <div class="help-block with-errors">Minimum 4 characters</div>
                </div>

                <div class="form-group">
                    <label for="iNewCPassword">Confirm : </label>
                    <input name="cpassword" type="password" id="iNewCPassword" class="form-control"
                           placeholder="Confirm password" data-match="#iNewPassword"
                           data-match-error="Password doesn't match" required/>

                    <div class="help-block with-errors">&nbsp;</div>
                </div>

                <input name="isPasswordChangeFormSubmitted" style="margin: 0  0px 10px 0" value="Change password"
                       type="submit" class="btn btn-danger pull-right"/>

            </form>


                <%
                    try {
                        final boolean isPasswordChangeFormSubmitted = request.getParameter("isPasswordChangeFormSubmitted") != null;
                        if (isPasswordChangeFormSubmitted) {

                            final String oldPassword = request.getParameter("old_password");
                            final String newPassword = request.getParameter(Clients.KEY_PASSWORD);
                            final String confirmNewPassword = request.getParameter(Clients.KEY_C_PASSWORD);

                            if (oldPassword != null && newPassword != null && confirmNewPassword != null) {

                                if (client.getPassHash().equals(DarKnight.getEncrypted(oldPassword))) {

                                    final String newPassHash = DarKnight.getEncrypted(newPassword);

                                    if (newPassHash != null) {

                                        if (!newPassHash.equals(client.getPassHash())) {
                                            final Clients clients = Clients.getInstance();

                                            final boolean isChanged = clients.update(Clients.COLUMN_ID, clientId.toString(), Clients.COLUMN_PASS_HASH, newPassHash);
                                            if (isChanged) {
                                                client.setClientCode(newPassword);
                %>
                <p class="text text-success change-settings-result">Client code changed!
                </p>
                <%
                                        } else {
                                            throw new Exception("Failed to set the password!");
                                        }

                                    } else {
                                        throw new Exception("Same as old!");
                                    }

                                } else {
                                    throw new Exception("Failed to change the password!");
                                }


                            } else {
                                throw new Exception("Old password is wrong!");
                            }

                        } else {
                            throw new Exception("Wrong request!");
                        }
                    }

                } catch (Exception e) {
                    e.printStackTrace();
                %>
                <span class="text text-danger change-settings-result-plus"><%=e.getMessage()%>
                </span>
                <%
                    }
                %>


        </div>
    </div>
    <br><br><br><br><br><br>
</div>
</body>
</html>
