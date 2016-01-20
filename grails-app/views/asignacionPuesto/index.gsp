<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 1/16/16
  Time: 17:40
--%>

<%@ page import="ec.com.tw.parking.AsignacionPuesto; ec.com.tw.parking.Usuario" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="asignar.title"/>
        </title>
    </head>

    <body>

        <elm:flashMessage tipo="${flash.tipo}" clase="${flash.clase}">${flash.message}</elm:flashMessage>

        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <g:message code="puesto.actual"/>
                            <g:link controller="asignacionPuesto" action="enviarMails"
                                    class="pull-right btn btn-xs btn-warning btnLoader">
                                <i class="fa fa-paper-plane-o"></i>
                                Enviar mails
                            </g:link>
                            <g:link controller="asignacionPuesto" action="reasignar" style="margin-right: 10px"
                                    class="pull-right btn btn-xs btn-info btnLoader">
                                <i class="fa fa-random"></i>
                                Reasignar
                            </g:link>
                        </h3>
                    </div>

                    <table class="table table-bordered table-condensed table-striped table-hover">
                        <tbody>
                            <g:each in="${AsignacionPuesto.findAllByFechaLiberacionIsNull([sort: "auto"])}" status="i" var="asignacion">
                                <tr>
                                    <td>${i + 1}</td>
                                    <td>
                                        ${asignacion}
                                        <g:if test="${asignacion?.puesto?.edificio?.observaciones}">
                                            [${asignacion?.puesto?.edificio?.observaciones}]
                                        </g:if>
                                    </td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

        <script type="text/javascript">
            $(function () {
                $(".btnLoader").click(function () {
                    openLoader("Por favor espere");
                });
            });
        </script>

    </body>
</html>