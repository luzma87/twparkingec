<%@ page import="ec.com.tw.parking.AsignacionPuesto; ec.com.tw.parking.Edificio" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="default.list.label" args="[message(code: 'edificio.label')]"/>
        </title>


        <style type="text/css">
        .noMargin {
            margin: 0;
        }
        </style>
    </head>

    <body>

        <elm:flashMessage tipo="${flash.tipo}" clase="${flash.clase}">${flash.message}</elm:flashMessage>

        <!-- botones -->
        <div class="btn-toolbar toolbar">
            <div class="btn-group btn-group-sm">
                <a href="#" class="btn btn-success btnCrear">
                    <i class="fa fa-file-o"></i>
                    <g:message code="default.button.create.label"/>
                </a>
            </div>

            <div class="btn-group pull-right col-md-3">
                <div class="input-group input-group-sm">
                    <input type="text" class="form-control input-search" placeholder="Buscar" value="${params.search}">
                    <span class="input-group-btn">
                        <g:link controller="edificio" action="list" class="btn btn-default btn-search">
                            <i class="fa fa-search"></i>&nbsp;
                        </g:link>
                    </span>
                </div><!-- /input-group -->
            </div>
        </div>

        <table class="table table-condensed table-bordered table-striped table-hover margin-top">
            <thead>
                <tr>

                    <g:sortableColumn property="nombre" title="${message(code: 'edificio.nombre.label')}"/>

                    <th><g:message code="edificio.distancia.label"/></th>

                    <g:sortableColumn property="esAmpliable" title="${message(code: 'edificio.esAmpliable.label')}"/>

                    <g:sortableColumn property="datosPago" title="${message(code: 'edificio.datosPago.label')}"/>

                    <g:sortableColumn property="observaciones" title="${message(code: 'edificio.observaciones.label')}"/>

                    <th>
                        <g:message code="edificio.puestos.label"/>
                    </th>

                    <th style="width: 76px"><g:message code="default.button.actions.label"/></th>
                </tr>
            </thead>
            <tbody>
                <g:if test="${edificioInstanceCount > 0}">
                    <g:each in="${edificioInstanceList}" status="i" var="edificioInstance">
                        <tr data-id="${edificioInstance.id}">

                            <td>${edificioInstance.nombre}</td>

                            <td><g:fieldValue bean="${edificioInstance}" field="distancia"/></td>

                            <td><g:formatBoolean boolean="${edificioInstance.esAmpliable}" false="${message(code: 'default.boolean.no')}" true="${message(code: 'default.boolean.yes')}"/></td>

                            <td>
                                ${raw(edificioInstance.datosPago)}
                            </td>

                            <td>
                                <g:fieldValue bean="${edificioInstance}" field="observaciones"/>
                            </td>

                            <td>
                                <ul class="fa-ul noMargin">
                                    <g:each in="${edificioInstance.puestos}" var="puesto">
                                        <li data-id="${puesto.id}">
                                            <i class="fa fa-toggle-${puesto.estaActivo ? 'on text-success' : 'off text-danger'}"></i>
                                            <a href="#" class="btnEditarPuesto"
                                               title="${message(code: 'default.button.edit.label')}">
                                                ${puesto.numero}
                                                (<g:message code="tamanio.${puesto.tamanio}"/>)
                                                <g:if test="${puesto.precio > 0}">
                                                    [<g:formatNumber number="${puesto.precio}" type="currency" currencySymbol="\$"/>]
                                                </g:if>
                                            </a>

                                            <div class="btn-group btn-group-xs marginLeft" role="group">
                                                <g:if test="${AsignacionPuesto.countByPuesto(puesto) == 0}">
                                                    <a href="#" class="btn btn-danger btnEliminarPuesto"
                                                       title="${message(code: 'default.button.delete.label')}">
                                                        <i class="fa fa-trash"></i>
                                                    </a>
                                                </g:if>
                                            </div>
                                        </li>
                                    </g:each>
                                </ul>
                            </td>

                            <td>
                                <div class="btn-group btn-group-xs">
                                    <a href="#" class="btnEditar btn btn-info"
                                       title="${message(code: 'default.button.edit.label')}">
                                        <i class="fa fa-pencil"></i>
                                    </a>
                                    <a href="#" class="btnAgregarPuesto btn btn-primary"
                                       title="${message(code: 'default.button.add.spot.label')}">
                                        <i class="fa fa-cube"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </g:each>
                </g:if>
                <g:else>
                    <tr class="info">
                        <td class="text-center text-shadow" colspan="6">
                            <i class="fa fa-2x icon-ghost"></i>
                            <g:message code="default.no.records.found"/>
                        </td>
                    </tr>
                </g:else>
            </tbody>
        </table>

        <script type="text/javascript">
            var id = null;
            function guardarEdificio() {
                var $form = $("#frmEdificio");
                if ($form.valid()) {
                    openLoader("${message(code: 'default.saving', args:[message(code: 'edificio.label')])}");
                    $.ajax({
                        type    : "POST",
                        url     : $form.attr("action"),
                        data    : $form.serialize(),
                        success : function (msg) {
                            var parts = msg.split("*");
                            log(parts[1], parts[0]); // log(msg, type, title, hide)
                            setTimeout(function () {
                                if (parts[0] == "SUCCESS") {
                                    location.reload(true);
                                } else {
                                    closeLoader();
                                    return false;
                                }
                            }, 1000);
                        },
                        error   : function () {
                            log("${message(code: 'default.internal.error')}", "Error");
                            closeLoader();
                        }
                    });
                } else {
                    return false;
                } //else
            }
            function crearEditarEdificio(id) {
                var title = id ?
                            "${message(code: 'default.edit.label', args:[message(code: 'edificio.label')])}" :
                            "${message(code: 'default.create.label', args:[message(code: 'edificio.label')])}";
                var data = id ? {id : id} : {};
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(controller:'edificio', action:'form_ajax')}",
                    data    : data,
                    success : function (msg) {
                        var b = bootbox.dialog({
                            id      : "dlgCreateEditEdificio",
                            title   : title,
                            message : msg,
                            buttons : {
                                cancelar : {
                                    label     : "${message(code: 'default.button.cancel.label')}",
                                    className : "btn-primary",
                                    callback  : function () {
                                    }
                                },
                                guardar  : {
                                    id        : "btnSave",
                                    label     : "<i class='fa fa-save'></i> ${message(code: 'default.button.save.label')}",
                                    className : "btn-success",
                                    callback  : function () {
                                        return guardarEdificio();
                                    } //callback
                                } //guardar
                            } //buttons
                        }); //dialog
                        setTimeout(function () {
                            b.find(".form-control").first().focus()
                        }, 500);
                    } //success
                }); //ajax
            } //createEdit
            function guardarPuesto() {
                var $form = $("#frmPuesto");
                if ($form.valid()) {
                    openLoader("${message(code: 'default.saving', args:[message(code: 'puesto.label')])}");
                    $.ajax({
                        type    : "POST",
                        url     : $form.attr("action"),
                        data    : $form.serialize(),
                        success : function (msg) {
                            var parts = msg.split("*");
                            log(parts[1], parts[0]); // log(msg, type, title, hide)
                            setTimeout(function () {
                                if (parts[0] == "SUCCESS") {
                                    location.reload(true);
                                } else {
                                    closeLoader();
                                    return false;
                                }
                            }, 1000);
                        },
                        error   : function () {
                            log("${message(code: 'default.internal.error')}", "Error");
                            closeLoader();
                        }
                    });
                } else {
                    return false;
                } //else
            }
            function eliminarPuesto(itemId) {
                bootbox.dialog({
                    title   : "${message(code: 'default.alert.title')}",
                    message : "<i class='fa fa-trash-o fa-3x pull-left text-danger text-shadow'></i><p>" +
                              "${message(code: 'default.delete.confirm.message', args:[message(code: 'puesto.label')])}</p>",
                    buttons : {
                        cancelar : {
                            label     : "${message(code: 'default.button.cancel.label')}",
                            className : "btn-primary",
                            callback  : function () {
                            }
                        },
                        eliminar : {
                            label     : "<i class='fa fa-trash-o'></i> ${message(code: 'default.button.delete.label')}",
                            className : "btn-danger",
                            callback  : function () {
                                openLoader("${message(code: 'default.deleting', args:[message(code: 'puesto.label')])}");
                                $.ajax({
                                    type    : "POST",
                                    url     : '${createLink(controller:'puesto', action:'delete_ajax')}',
                                    data    : {
                                        id : itemId
                                    },
                                    success : function (msg) {
                                        var parts = msg.split("*");
                                        log(parts[1], parts[0]); // log(msg, type, title, hide)
                                        if (parts[0] == "SUCCESS") {
                                            setTimeout(function () {
                                                location.reload(true);
                                            }, 1000);
                                        } else {
                                            closeLoader();
                                        }
                                    },
                                    error   : function () {
                                        log("${message(code: 'default.internal.error')}", "Error");
                                        closeLoader();
                                    }
                                });
                            }
                        }
                    }
                });
            }
            function crearEditarPuesto(puestoId, edificioId) {
                var title = puestoId ?
                            "${message(code: 'default.edit.label', args:[message(code: 'puesto.label')])}" :
                            "${message(code: 'default.add.label', args:[message(code: 'puesto.label')])}";
                var data = puestoId ? {id : puestoId} : {};
                data.edificio = edificioId ? edificioId : null;
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(controller:'puesto', action:'form_ajax')}",
                    data    : data,
                    success : function (msg) {
                        var b = bootbox.dialog({
                            id      : "dlgCreateEditPuesto",
                            title   : title,
                            message : msg,
                            buttons : {
                                cancelar : {
                                    label     : "${message(code: 'default.button.cancel.label')}",
                                    className : "btn-primary",
                                    callback  : function () {
                                    }
                                },
                                guardar  : {
                                    id        : "btnSave",
                                    label     : "<i class='fa fa-save'></i> ${message(code: 'default.button.save.label')}",
                                    className : "btn-success",
                                    callback  : function () {
                                        return guardarPuesto();
                                    } //callback
                                } //guardar
                            } //buttons
                        }); //dialog
                        setTimeout(function () {
                            b.find(".form-control").first().focus()
                        }, 500);
                    } //success
                }); //ajax
            } //createEdit

            $(function () {
                $(".btnCrear").click(function () {
                    crearEditarEdificio();
                    return false;
                });
                $(".btnEditar").click(function () {
                    crearEditarEdificio($(this).parents("tr").data("id"));
                    return false;
                });
                $(".btnEditarPuesto").click(function () {
                    crearEditarPuesto($(this).parents("li").data("id"));
                    return false;
                });
                $(".btnEliminarPuesto").click(function () {
                    eliminarPuesto($(this).parents("li").data("id"));
                    return false;
                });
                $(".btnAgregarPuesto").click(function () {
                    crearEditarPuesto(null, $(this).parents("tr").data("id"));
                    return false;
                });

            });
        </script>

    </body>
</html>