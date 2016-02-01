<%@ page import="ec.com.tw.parking.Pago" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="default.list.label" args="[message(code: 'pago.label')]"/>
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
                        <g:link controller="pago" action="list" class="btn btn-default btn-search">
                            <i class="fa fa-search"></i>&nbsp;
                        </g:link>
                    </span>
                </div><!-- /input-group -->
            </div>
        </div>

        <table class="table table-condensed table-bordered table-striped table-hover margin-top">
            <thead>
                <tr>

                    <th><g:message code="pago.usuario.label"/></th>

                    <g:sortableColumn property="fechaPago" title="${message(code: 'pago.fechaPago.label')}"/>

                    <g:sortableColumn property="mes" title="${message(code: 'pago.mes.label')}"/>

                    <g:sortableColumn property="monto" title="${message(code: 'pago.monto.label')}"/>

                    <th style="width: 76px"><g:message code="default.button.actions.label"/></th>
                </tr>
            </thead>
            <tbody>
                <g:if test="${pagoInstanceCount > 0}">
                    <g:each in="${pagoInstanceList}" status="i" var="pagoInstance">
                        <tr data-id="${pagoInstance.id}">

                            <td>${pagoInstance.usuario}</td>

                            <td><g:formatDate date="${pagoInstance.fechaPago}"
                                              format="${message(code: 'default.date.format.no.time')}"/></td>

                            <td>
                                <g:message code="mes.${pagoInstance.mes}"/> ${pagoInstance.anio}
                            </td>

                            <td><g:formatNumber number="${pagoInstance.monto}" type="currency" currencySymbol="\$"/></td>

                            <td>
                                <div class="btn-group btn-group-sm">
                                    <a href="#" class="btnEditar btn btn-info"
                                       title="${message(code: 'default.button.edit.label')}">
                                        <i class="fa fa-pencil"></i>
                                    </a>
                                    <a href="#" class="btnEliminar btn btn-danger"
                                       title="${message(code: 'default.button.delete.label')}">
                                        <i class="fa fa-trash"></i>
                                    </a>
                                </div>
                            </td>
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
            function guardarPago() {
                var $form = $("#frmPago");
                if ($form.valid()) {
                    openLoader("${message(code: 'default.saving', args:[message(code: 'pago.label')])}");
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
            function eliminarPago(itemId) {
                bootbox.dialog({
                    title   : "${message(code: 'default.alert.title')}",
                    message : "<i class='fa fa-trash-o fa-3x pull-left text-danger text-shadow'></i><p>" +
                              "${message(code: 'default.delete.confirm.message', args:[message(code: 'pago.label')])}</p>",
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
                                openLoader("${message(code: 'default.deleting', args:[message(code: 'pago.label')])}");
                                $.ajax({
                                    type    : "POST",
                                    url     : '${createLink(controller:'pago', action:'delete_ajax')}',
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
            function crearEditarPago(id) {
                var title = id ?
                            "${message(code: 'default.edit.label', args:[message(code: 'pago.label')])}" :
                            "${message(code: 'default.create.label', args:[message(code: 'pago.label')])}";
                var data = id ? {id : id} : {};
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(controller:'pago', action:'form_ajax')}",
                    data    : data,
                    success : function (msg) {
                        var b = bootbox.dialog({
                            id      : "dlgCreateEditPago",
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
                                        return guardarPago();
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
                    crearEditarPago();
                    return false;
                });
                $(".btnEditar").click(function () {
                    crearEditarPago($(this).parents("tr").data("id"));
                    return false;
                });
                $(".btnEliminar").click(function () {
                    eliminarPago($(this).parents("tr").data("id"));
                    return false;
                });
            });
        </script>

    </body>
</html>