<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 1/19/16
  Time: 09:23
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="error.server.error.500.title"/>
        </title>
    </head>

    <body>

        <div class="alert alert-warning text-shadow">
            <i class="fa fa-ban fa-4x pull-left"></i>

            <h1 class="text-white">
                <g:message code="error.title"/>
            </h1>

            <g:message code="error.server.error.500.message"/>

        </div>

    </body>
</html>