
<%@ page import="ec.com.tw.parking.TipoTransicion" %>

<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="default.list.label" args="[message(code: 'tipoTransicion.label')]"/>
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
                        <g:link controller="tipoTransicion" action="list" class="btn btn-default btn-search">
                            <i class="fa fa-search"></i>&nbsp;
                        </g:link>
                    </span>
                </div><!-- /input-group -->
            </div>
        </div>

        <table class="table table-condensed table-bordered table-striped table-hover margin-top">
            <thead>
                <tr>
                    
                    <g:sortableColumn property="nombre" title="${message(code: 'tipoTransicion.nombre.label')}"/>
                    
                    <th><g:message code="tipoTransicion.distanciaOrigen.label" /></th>
                    
                    <th><g:message code="tipoTransicion.distanciaDestino.label" /></th>
                    
                    <g:sortableColumn property="prioridad" title="${message(code: 'tipoTransicion.prioridad.label')}"/>
                    
                    <th style="width: 76px"><g:message code="default.button.actions.label"/> </th>
                </tr>
            </thead>
            <tbody>
                <g:if test="${tipoTransicionInstanceCount > 0}">
                    <g:each in="${tipoTransicionInstanceList}" status="i" var="tipoTransicionInstance">
                        <tr data-id="${tipoTransicionInstance.id}">
                            
                            <td>${tipoTransicionInstance.nombre}</td>
                            
                            <td><g:fieldValue bean="${tipoTransicionInstance}" field="distanciaOrigen"/></td>
                                
                            <td><g:fieldValue bean="${tipoTransicionInstance}" field="distanciaDestino"/></td>
                                
                            <td><g:fieldValue bean="${tipoTransicionInstance}" field="prioridad"/></td>
                            
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <a href="#" class="btnEditar btn btn-info"
                                       title="${message(code:'default.button.edit.label')}">
                                        <i class="fa fa-pencil"></i>
                                    </a>
                                    <a href="#" class="btnEliminar btn btn-danger"
                                       title="${message(code:'default.button.delete.label')}">
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
            function guardarTipoTransicion() {
                var $form = $("#frmTipoTransicion");
                if ($form.valid()) {
                    openLoader("${message(code: 'default.saving', args:[message(code: 'tipoTransicion.label')])}");
                    $.ajax({
                        type    : "POST",
                        url     : $form.attr("action"),
                            data    : $form.serialize(),
                            success : function (msg) {
                        var parts = msg.split("*");
                        log(parts[1], parts[0]); // log(msg, type, title, hide)
                        setTimeout(function() {
                            if (parts[0] == "SUCCESS") {
                                location.reload(true);
                            } else {
                                closeLoader();
                                return false;
                            }
                        }, 1000);
                    },
                    error: function() {
                        log("${message(code: 'default.internal.error')}", "Error");
                        closeLoader();
                    }
                });
                } else {
                    return false;
                } //else
            }
            function eliminarTipoTransicion(itemId) {
                bootbox.dialog({
                    title   : "${message(code: 'default.alert.title')}",
                    message : "<i class='fa fa-trash-o fa-3x pull-left text-danger text-shadow'></i><p>" +
                              "${message(code: 'default.delete.confirm.message', args:[message(code: 'tipoTransicion.label')])}</p>",
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
                                openLoader("${message(code: 'default.deleting', args:[message(code: 'tipoTransicion.label')])}");
                                $.ajax({
                                    type    : "POST",
                                    url     : '${createLink(controller:'tipoTransicion', action:'delete_ajax')}',
                                    data    : {
                                        id : itemId
                                    },
                                    success : function (msg) {
                                        var parts = msg.split("*");
                                        log(parts[1], parts[0]); // log(msg, type, title, hide)
                                        if (parts[0] == "SUCCESS") {
                                            setTimeout(function() {
                                                location.reload(true);
                                            }, 1000);
                                        } else {
                                            closeLoader();
                                        }
                                    },
                                    error: function() {
                                        log("${message(code: 'default.internal.error')}", "Error");
                                        closeLoader();
                                    }
                                });
                            }
                        }
                    }
                });
            }
            function crearEditarTipoTransicion(id) {
                var title = id ?
                            "${message(code: 'default.edit.label', args:[message(code: 'tipoTransicion.label')])}" :
                            "${message(code: 'default.create.label', args:[message(code: 'tipoTransicion.label')])}";
                var data = id ? { id: id } : {};
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(controller:'tipoTransicion', action:'form_ajax')}",
                    data    : data,
                    success : function (msg) {
                        var b = bootbox.dialog({
                            id      : "dlgCreateEditTipoTransicion",
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
                                        return guardarTipoTransicion();
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
                $(".btnCrear").click(function() {
                    crearEditarTipoTransicion();
                    return false;
                });
                $(".btnEditar").click(function() {
                    crearEditarTipoTransicion($(this).parents("tr").data("id"));
                    return false;
                });
                $(".btnEliminar").click(function() {
                    eliminarTipoTransicion($(this).parents("tr").data("id"));
                    return false;
                });
            });
        </script>

    </body>
</html>