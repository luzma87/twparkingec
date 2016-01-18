<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 12/21/15
  Time: 12:43
--%>

<%@ page import="ec.com.tw.parking.AsignacionPuesto; ec.com.tw.parking.Usuario" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main"/>
    </head>

    <body>
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <g:message code="default.registered.people"/>
                        </h3>
                    </div>

                    <table class="table table-bordered table-condensed table-striped">
                        <thead>
                            <tr>
                                <th></th>
                                <th>${message(code: 'usuario.nombre.label')}</th>
                                <th>${message(code: 'auto.label')}</th>
                                <th>${message(code: 'puesto.label')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${AsignacionPuesto.list()}" status="i" var="asignacion">
                                <tr>
                                    <td>${i + 1}</td>
                                    <td>
                                        ${asignacion.auto.usuario}
                                    </td>
                                    <td>
                                        ${asignacion.auto} (${asignacion.auto.placa})
                                    </td>
                                    <td>
                                        ${asignacion?.puesto}
                                        <g:if test="${asignacion?.puesto?.edificio?.observaciones}">
                                            <br/>[${asignacion?.puesto?.edificio?.observaciones}]
                                        </g:if>
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