<%@ page import="com.theah64.xrob.api.utils.Request" %>
<%@ page import="com.theah64.xrob.api.database.tables.Clients" %>
<%@ page import="com.theah64.xrob.api.models.Client" %>
<%@ page import="com.theah64.xrob.api.utils.DarKnight" %>
<%@ page import="com.theah64.xrob.api.utils.RandomString" %>
<%@ page import="com.theah64.xrob.api.utils.CommonUtils" %>
<%
    if (session.getAttribute(Clients.COLUMN_ID) != null) {
        response.sendRedirect("/client/panel");
        return;
    }

%>
<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 12/9/16
  Time: 4:08 PM
  To change this template use FileNode | Settings | FileNode Templates.
--%>
<%@ page contentType="text/html;charset=UTF-8" language="java" %>
<html>
<head>
    <title>Signup client</title>
    <%@include file="../common_headers.jsp" %>
</head>
<body>
<div class="container">
    <div class="row">
        <h1 class="text-center">Sign up</h1>
    </div>

    <div class="row">

        <div class="col-md-4 content-centered">
            <%--Form - username,password,confirm_password,email,--%>
            <form data-toggle="validator" action="/client/signup" method="POST" role="form">

                <div class="form-group">
                    <label for="iUsername">Username : </label>
                    <input value="testuser" name="username" type="text" pattern="<%=Clients.REGEX_USERNAME%>"
                           id="iUsername"
                           class="form-control"
                           placeholder="Username" required/>

                    <div class="help-block">Minimum 6 characters, maximum 20 characters, letters and numbers
                        only.
                    </div>
                </div>


                <div class="form-group">
                    <label for="iPassword">Password : </label>
                    <input value="testpass" name="password" type="password" data-minlength="6" id="iPassword"
                           class="form-control"
                           placeholder="Password" required/>

                    <div class="help-block with-errors">Minimum 6 characters</div>
                </div>

                <div class="form-group">
                    <label for="iCPassword">Confirm password : </label>
                    <input value="testpass" name="cpassword" type="password" id="iCPassword" class="form-control"
                           placeholder="Confirm password" data-match="#iPassword"
                           data-match-error="Password doesn't match" required/>

                    <div class="help-block with-errors"></div>
                </div>

                <div class="form-group">
                    <label for="iEmail">Email : </label>
                    <input value="theapache64@gmail.com" name="email" type="email" id="iEmail" class="form-control"
                           placeholder="Your email address"
                           data-error="Whooops, that email address is invalid"
                           required/>
                    <div class="help-block with-errors"></div>
                </div>


                <div class="row">

                    <div class="col-md-8">
                        <%--Errors are show here--%>

                        <%
                            final boolean isFormSubmitted = request.getParameter("isFormSubmitted") != null;

                            if (isFormSubmitted) {

                                int errorCounter = 0;
                                try {
                                    final Request clientSignUpRequest = new Request(request,
                                            new String[]{
                                                    Clients.COLUMN_USERNAME,
                                                    Clients.KEY_PASSWORD,
                                                    Clients.KEY_C_PASSWORD,
                                                    Clients.COLUMN_EMAIL});


                                    final String username = clientSignUpRequest.getStringParameter(Clients.COLUMN_USERNAME);
                                    final String password = clientSignUpRequest.getStringParameter(Clients.KEY_PASSWORD);
                                    final String cPassword = clientSignUpRequest.getStringParameter(Clients.KEY_C_PASSWORD);
                                    final String email = clientSignUpRequest.getStringParameter(Clients.COLUMN_EMAIL);


                                    final StringBuilder errorBuilder = new StringBuilder();

                                    if (!username.matches(Clients.REGEX_USERNAME)) {
                                        errorBuilder.append(++errorCounter).append("Invalid username!</br>");
                                    }

                                    if (password.length() < 6) {
                                        errorBuilder.append(++errorCounter).append("Password length must be at least 6 characters</br>");
                                    }

                                    if (password.length() > 6 && !password.equals(cPassword)) {
                                        errorBuilder.append(++errorCounter).append("Password doesn't match</br>");
                                    }

                                    if (!email.matches(Clients.REGEX_EMAIL)) {
                                        errorBuilder.append(++errorCounter).append("Invalid email: ").append(email).append("</br>");
                                    }


                                    final Clients clientsTable = Clients.getInstance();

                                    //Existence check
                                    if (clientsTable.isExist(Clients.COLUMN_USERNAME, username)) {
                                        errorBuilder.append(++errorCounter).append("# Username already exists</br>");
                                    }

                                    if (clientsTable.isExist(Clients.COLUMN_EMAIL, email)) {
                                        errorBuilder.append(++errorCounter).append("# Email already exists</br>");
                                    }

                                    final String error = errorBuilder.toString();

                                    if (errorCounter == 0 && error.isEmpty()) {
                                        //Valid data here
                                        final String clientCode = clientsTable.getNewClientCode();

                                        final Client client = new Client(
                                                null,
                                                username,
                                                DarKnight.getEncrypted(password),
                                                RandomString.getNewApiKey(Clients.API_KEY_LENGTH),
                                                email,
                                                clientCode
                                        );

                                        final String clientId = clientsTable.addv3(client);
                                        if (clientId != null) {
                                            session.setAttribute(Clients.COLUMN_ID, clientId);
                                            response.sendRedirect("/client/panel");
                                        } else {
                                            throw new Exception("Sign up failed");
                                        }

                                    } else {
                                        throw new Exception("<b>" + errorCounter + CommonUtils.getProperSentense(errorCounter, " ERROR", " ERRORS") + " : </b></br>" + error);
                                    }

                                } catch (Exception e) {
                                    e.printStackTrace();
                        %>
                        <div class="text-danger pull-left">
                            <%=e.getMessage()%>
                        </div>
                        <%
                                }
                            }
                        %>

                    </div>


                    <div class="col-md-4">
                        <input name="isFormSubmitted" value="Sign up" type="submit" class="btn btn-primary pull-right"/>
                    </div>


                </div>
            </form>
        </div>
    </div>

    <div class="row">
        <div class="col-md-12 text-center">
            Already have an account, <a href="/client/signin">Sign In</a>
        </div>
    </div>
</div>
</body>
</html>
