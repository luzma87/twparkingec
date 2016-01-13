<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 1/13/16
  Time: 14:35
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main"/>
        <title>Parámetros</title>
    </head>

    <body>
        <div class="panel panel-info">
            <div class="panel-heading">
                <h3 class="panel-title">Parámetros del sistema</h3>
            </div>

            <div class="panel-body">
                <ul class="fa-ul">
                    <li>
                        <i class="fa-li fa fa-circle-thin"></i>
                        <g:link controller="distanciaEdificio" action="index">
                            Distancia Edificio
                        </g:link>
                    </li>
                    <li>
                        <i class="fa-li fa fa-circle-thin"></i>
                        <g:link controller="tipoPreferencia" action="index">
                            Tipo de preferencia
                        </g:link>
                    </li>
                    <li>
                        <i class="fa-li fa fa-circle-thin"></i>
                        <g:link controller="tipoTransicion" action="index">
                            Tipo de transición
                        </g:link>
                    </li>
                </ul>
            </div>
        </div>
    </body>
</html>