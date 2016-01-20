<%@ page import="ec.com.tw.parking.AsignacionPuesto" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="default.list.label" args="[message(code: 'asignacionPuesto.label')]"/>
        </title>
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
                        <g:link controller="asignacionPuesto" action="list" class="btn btn-default btn-search">
                            <i class="fa fa-search"></i>&nbsp;
                        </g:link>
                    </span>
                </div><!-- /input-group -->
            </div>
        </div>

        <div class="row">
            <div class="col-md-6">
                <h4>Puestos sin asignación</h4>
                <ul class="fa-ul">
                    <g:each in="${puestosSinAsignacion}" var="puesto">
                        <li>${puesto} (${puesto.edificio.distancia})</li>
                    </g:each>
                </ul>
            </div>

            <div class="col-md-6">
                <h4>Autos sin asignacion</h4>
                <ul class="fa-ul">
                    <g:each in="${autosSinAsignacion}" var="auto">
                        <li>${auto}</li>
                    </g:each>
                </ul>
            </div>
        </div>

        <table class="table table-condensed table-bordered table-striped table-hover margin-top">
            <thead>
                <tr>

                    <th><g:message code="asignacionPuesto.auto.label"/></th>

                    <th><g:message code="asignacionPuesto.puesto.label"/></th>

                    <g:sortableColumn property="fechaAsignacion" title="${message(code: 'asignacionPuesto.fechaAsignacion.label')}"/>

                    <g:sortableColumn property="fechaLiberacion" title="${message(code: 'asignacionPuesto.fechaLiberacion.label')}"/>

                </tr>
            </thead>
            <tbody>
                <g:if test="${asignacionPuestoInstanceCount > 0}">
                    <g:each in="${asignacionPuestoInstanceList}" status="i" var="asignacionPuestoInstance">
                        <tr data-id="${asignacionPuestoInstance.id}">

                            <td>${asignacionPuestoInstance.auto}</td>

                            <td><g:fieldValue bean="${asignacionPuestoInstance}" field="puesto"/></td>

                            <td><g:formatDate date="${asignacionPuestoInstance.fechaAsignacion}" format="${message(code: 'default.date.format')}"/></td>

                            <td><g:formatDate date="${asignacionPuestoInstance.fechaLiberacion}" format="${message(code: 'default.date.format')}"/></td>

                        </tr>
                    </g:each>
                </g:if>
                <g:else>
                    <tr class="info">
                        <td class="text-center text-shadow" colspan="5">
                            <i class="fa fa-2x icon-ghost"></i>
                            <g:message code="default.no.records.found"/>
                        </td>
                    </tr>
                </g:else>
            </tbody>
        </table>

        <script type="text/javascript">
            var id = null;
            function guardarAsignacionPuesto() {
                var $form = $("#frmAsignacionPuesto");
                if ($form.valid()) {
                    openLoader("${message(code: 'default.saving', args:[message(code: 'asignacionPuesto.label')])}");
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
            function eliminarAsignacionPuesto(itemId) {
                bootbox.dialog({
                    title   : "${message(code: 'default.alert.title')}",
                    message : "<i class='fa fa-trash-o fa-3x pull-left text-danger text-shadow'></i><p>" +
                              "${message(code: 'default.delete.confirm.message', args:[message(code: 'asignacionPuesto.label')])}</p>",
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
                                openLoader("${message(code: 'default.deleting', args:[message(code: 'asignacionPuesto.label')])}");
                                $.ajax({
                                    type    : "POST",
                                    url     : '${createLink(controller:'asignacionPuesto', action:'delete_ajax')}',
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
            function crearEditarAsignacionPuesto(id) {
                var title = id ?
                            "${message(code: 'default.edit.label', args:[message(code: 'asignacionPuesto.label')])}" :
                            "${message(code: 'default.create.label', args:[message(code: 'asignacionPuesto.label')])}";
                var data = id ? {id : id} : {};
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(controller:'asignacionPuesto', action:'form_ajax')}",
                    data    : data,
                    success : function (msg) {
                        var b = bootbox.dialog({
                            id      : "dlgCreateEditAsignacionPuesto",
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
                                        return guardarAsignacionPuesto();
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
                    crearEditarAsignacionPuesto();
                    return false;
                });
                $(".btnEditar").click(function () {
                    crearEditarAsignacionPuesto($(this).parents("tr").data("id"));
                    return false;
                });
                $(".btnEliminar").click(function () {
                    eliminarAsignacionPuesto($(this).parents("tr").data("id"));
                    return false;
                });
            });
        </script>

    </body>
</html>