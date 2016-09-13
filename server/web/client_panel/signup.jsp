<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 12/9/16
  Time: 4:08 PM
  To change this template use File | Settings | File Templates.
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
            <form data-toggle="validator" action="signup.jsp" method="POST" role="form">

                <div class="form-group">
                    <label for="iUsername">Username : </label>
                    <input name="username" type="text" pattern="^[a-z0-9]{6,20}$" id="iUsername" class="form-control"
                           placeholder="Username" required/>

                    <div class="help-block">Minimum 6 characters, maximum 20 characters, letters and numbers
                        only.
                    </div>
                </div>


                <div class="form-group">
                    <label for="iPassword">Password : </label>
                    <input name="password" type="password" data-minlength="6" id="iPassword" class="form-control"
                           placeholder="Password" required/>

                    <div class="help-block with-errors">Minimum 6 characters</div>
                </div>

                <div class="form-group">
                    <label for="iCPassword">Confirm password : </label>
                    <input name="cpassword" type="password" id="iCPassword" class="form-control"
                           placeholder="Confirm password" data-match="#iPassword"
                           data-match-error="Password doesn't match" required/>

                    <div class="help-block with-errors"></div>
                </div>

                <div class="form-group">
                    <label for="iEmail">Email : </label>
                    <input name="email" type="email" id="iEmail" class="form-control" placeholder="Your email address"
                           required/>
                </div>


                <div class="row">

                    <div class="col-md-8">
                        <%--Errors are show here--%>
                    </div>


                    <div class="col-md-4">
                        <button type="submit" class="btn btn-primary pull-right">Sign up</button>
                    </div>


                </div>


            </form>
        </div>
    </div>
</div>
</body>
</html>
