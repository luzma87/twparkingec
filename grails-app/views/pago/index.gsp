<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 2/1/16
  Time: 15:51
--%>

<%@ page import="ec.com.tw.parking.enums.Mes; ec.com.tw.parking.Pago" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="pagos.title"/>
        </title>>
    </head>

    <body>
        <div class="row">
            <div class="col-md-12">
                <div class="panel panel-success">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <g:message code="pago.historico"/> ${anio}
                        </h3>
                    </div>

                    <table class="table table-bordered table-condensed table-striped table-hover">
                        <thead>
                            <tr>
                                <th>TWer</th>
                                <g:each in="${Mes.values()}" var="mes">
                                    <th>${mes}</th>
                                </g:each>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${pagos}" status="i" var="datosPagos">
                                <tr>
                                    <td>${datosPagos.value.usuario}</td>
                                    <g:each in="${Mes.values()}" var="mes">
                                        <g:set var="pago" value="${datosPagos.value.pagos.find { it.mes == mes }}"/>
                                        <td class="text-center">
                                            <g:formatDate date="${pago?.fechaPago}"
                                                          format="${g.message(code: 'default.date.format.no.time')}"/>
                                            <br/>
                                            <g:formatNumber number="${pago?.monto}" type="currency" currencySymbol="\$"/>
                                        </td>
                                    </g:each>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>
    </body>
</html>