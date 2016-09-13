<%--
  Created by IntelliJ IDEA.
  User: theapache64
  Date: 13/9/16
  Time: 2:32 PM
  To change this template use File | Settings | File Templates.
--%>
<div class="row">
    <div class="col-md-8">
        <h2>Xrob
            <small>v2</small>
        </h2>
    </div>
    <div class="col-md-4" style="margin-top: 16px">
        <div class="well well-sm pull-right">CLIENT CODE:
            <b><%=((Client) request.getAttribute(Client.KEY)).getClientCode()%>
            </b></div>
    </div>
</div>