<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 12/21/15
  Time: 12:43
--%>

<%@ page import="ec.com.tw.parking.Usuario" contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main"/>
    </head>

    <body>
        <div class="row">
            <div class="col-md-3">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <g:message code="default.registered.people"/>
                            <span class="badge pull-right">${Usuario.count()}</span>
                        </h3>
                    </div>

                    <table class="table table-bordered table-condensed table-striped">
                        <thead>
                            <tr>
                                <th>${message(code: 'usuario.nombre.label', default: 'Nombre')}</th>
                                <th>${message(code: 'usuario.email.label', default: 'Email')}</th>
                            </tr>
                        </thead>
                        <tbody>
                            <g:each in="${Usuario.list()}" status="i" var="usuarioInstance">
                                <tr class="${(i % 2) == 0 ? 'even' : 'odd'}">
                                    <td><g:link action="show" id="${usuarioInstance.id}">${fieldValue(bean: usuarioInstance, field: "nombre")}</g:link></td>
                                    <td>${fieldValue(bean: usuarioInstance, field: "email")}</td>
                                </tr>
                            </g:each>
                        </tbody>
                    </table>
                </div>
            </div>
        </div>

    </body>
</html>