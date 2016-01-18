<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 1/16/16
  Time: 22:47
--%>

<%@ page import="ec.com.tw.parking.AsignacionPuesto;" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>
            <g:message code="historial.title"/>
        </title>
    </head>

    <body>
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-warning">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <g:message code="puesto.historico"/>
                        </h3>
                    </div>

                    <table class="table table-bordered table-condensed table-striped">
                        <tbody>
                            <g:each in="${AsignacionPuesto.listOrderByAuto()}" status="i" var="historial">
                                <tr>
                                    <td>${i + 1}</td>
                                    <td>
                                        ${historial}
                                    </td>
                                    <td>
                                        <g:formatDate date="${historial.fechaAsignacion}"/>
                                    </td>
                                    <td>
                                        <g:formatDate date="${historial.fechaLiberacion}"/>
                                    </td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>