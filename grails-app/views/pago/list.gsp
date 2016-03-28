<%@ page import="ec.com.tw.parking.enums.Mes; ec.com.tw.parking.Pago" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="default.list.label" args="[message(code: 'pago.label')]"/>
        </title>
        <style type="text/css">
        th {
            vertical-align: middle !important;
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
                        <g:link controller="pago" action="list" class="btn btn-default btn-search">
                            <i class="fa fa-search"></i>&nbsp;
                        </g:link>
                    </span>
                </div><!-- /input-group -->
            </div>
        </div>

        <h2><g:message code="pagos.title"/> <g:select name="anio" from="${anios}" value="${anio}"/></h2>

        <table class="table table-bordered table-condensed table-striped table-hover">
            <thead>
                <tr>
                    <th></th>
                    <th class="text-center">TWer</th>
                    <g:each in="${Mes.values()}" var="mes">
                        <th class="text-center">
                            <g:message code="mes.${mes}"/><br/>
                            <g:formatNumber number="${Pago.findAllByMesAndAnio(mes, anio).sum { it.monto }}"
                                            type="currency" currencySymbol="\$"/>
                        </th>
                    </g:each>
                </tr>
            </thead>
            <tbody>
                <g:each in="${pagos}" status="i" var="datosPagos">
                    <tr>
                        <td>${i + 1}</td>
                        <td>${datosPagos.value.usuario}</td>
                        <g:each in="${Mes.values()}" var="mes">
                            <g:set var="pago" value="${datosPagos.value.pagos.find { it.mes == mes }}"/>
                            <td class="text-center col-md-2">
                                <g:if test="${pago}">
                                    <g:formatDate date="${pago.fechaPago}"
                                                  format="${g.message(code: 'default.date.format.no.time')}"/>
                                    <br/>
                                    <g:formatNumber number="${pago.monto}" type="currency" currencySymbol="\$"/>
                                    <g:if test="${mes.numero == mesActual + 1}">
                                        <a href="#" class="btn btn-xs btn-danger btnEliminar"
                                           data-id="${pago.id}"
                                           title="${message(code: 'default.button.delete.label')}">
                                            <i class="fa fa-trash"></i>
                                        </a>
                                    </g:if>
                                </g:if>
                                <g:else>
                                    <g:if test="${mes.numero == mesActual + 1}">
                                        <div class="input-group input-group-sm">
                                            <span class="input-group-addon">
                                                <i class="fa fa-usd"></i>
                                            </span>
                                            <input type="text" class="form-control input" value="${cuota}">
                                            <span class="input-group-btn">
                                                <a href="#" class="btn btn-success btnRegistrarPago"
                                                   data-usuario="${datosPagos.value.usuario.id}"
                                                   data-mes="${mes}"
                                                   title="${g.message(code: 'pago.registrar')}">
                                                    <i class="fa fa-lg fa-money"></i>
                                                </a>
                                            </span>
                                        </div>
                                    </g:if>
                                </g:else>
                            </td>
                        </g:each>
                    </tr>
                </g:each>
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
            function registrarPago($btn) {
                openLoader("${message(code: 'default.saving', args:[message(code: 'pago.label')])}");
                var usuarioId = $btn.data("usuario");
                var numeroMes = $btn.data("mes");
                var monto = $btn.parent().prev().val();
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(controller: 'pago', action: 'save_ajax')}",
                    data    : {
                        usuario : usuarioId,
                        mes     : numeroMes,
                        anio    : "${params.anio}",
                        monto   : monto
                    },
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
            }
            $(function () {
                $("#anio").change(function () {
                    var anio = $(this).val();
                    if ("${params.anio}" != anio) {
                        location.href = "${createLink(controller: 'pago', action:'list')}?anio=" + anio;
                    }
                });
                $(".btnCrear").click(function () {
                    crearEditarPago();
                    return false;
                });
                $(".btnEliminar").click(function () {
                    eliminarPago($(this).data("id"));
                    return false;
                });
                $(".btnRegistrarPago").click(function () {
                    registrarPago($(this));
                    return false;
                });
                $(".input").keypress(function (ev) {
                    if (ev.keyCode == 13) {
                        $(this).next().children().first().click();
                    }
                });
            });
        </script>

    </body>
</html>