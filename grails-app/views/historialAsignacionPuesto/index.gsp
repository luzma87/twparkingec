<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 1/16/16
  Time: 22:47
--%>

<%@ page import="ec.com.tw.parking.HistoricoAsignacionPuesto" contentType="text/html;charset=UTF-8" %>
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
                            <g:if test="${historial.size() > 0}">
                                <g:each in="${HistoricoAsignacionPuesto.list()}" status="i" var="historial">
                                    <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                        <td>
                                            ${historial}
                                        </td>
                                    </tr>
                                </g:each>
                            </g:if>
                            <g:else>
                                <tr class="info text-shadow text-center">
                                    <td>
                                        <i class="fa icon-ghost fa-2x"></i>
                                        No se encontró el historial de asignaciones.
                                        El historial se guarda una vez que se termina la asignación
                                    </td>
                                </tr>
                            </g:else>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>