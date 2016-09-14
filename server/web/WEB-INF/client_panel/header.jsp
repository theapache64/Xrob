<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 13/9/16
  Time: 2:32 PM
  To change this template use File | Settings | File Templates.
--%>
<div style="margin-bottom: 50px;" class="row">
    <div class="col-md-8">
        <a href="/client/panel">
            <h2>Xrob
                <small>v2</small>
            </h2>
        </a>
    </div>


    <div class="col-md-4" style="margin-top: 16px">

        <div class="well well-sm pull-left">CLIENT CODE:
            <b><%=((Client) request.getAttribute(Client.KEY)).getClientCode()%>
            </b></div>

        <div class="row">
            <ul id="main_menu" class="pager">
                <li><a href="/client/settings">Settings</a></li>
                <li><a href="/client/signout">Sign out</a></li>
            </ul>
        </div>
    </div>
</div>