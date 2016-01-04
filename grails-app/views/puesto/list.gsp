
<%@ page import="ec.com.tw.parking.Puesto" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="default.list.label" args="[message(code: 'puesto.label')]"/>
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
                        <g:link controller="puesto" action="list" class="btn btn-default btn-search">
                            <i class="fa fa-search"></i>&nbsp;
                        </g:link>
                    </span>
                </div><!-- /input-group -->
            </div>
        </div>

        <table class="table table-condensed table-bordered table-striped table-hover margin-top">
            <thead>
                <tr>
                    
                    <g:sortableColumn property="tamanio" title="${message(code: 'puesto.tamanio.label')}"/>
                    
                    <g:sortableColumn property="numero" title="${message(code: 'puesto.numero.label')}"/>
                    
                    <th><g:message code="puesto.edificio.label" /></th>
                    
                    <th style="width: 76px"><g:message code="default.button.actions.label"/> </th>
                </tr>
            </thead>
            <tbody>
                <g:if test="${puestoInstanceCount > 0}">
                    <g:each in="${puestoInstanceList}" status="i" var="puestoInstance">
                        <tr data-id="${puestoInstance.id}">

                            <td>
                                <g:message code="tamanio.${puestoInstance.tamanio}" />
                            </td>
                            
                            <td><g:fieldValue bean="${puestoInstance}" field="numero"/></td>
                                
                            <td><g:fieldValue bean="${puestoInstance}" field="edificio"/></td>
                                
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
                        <td class="text-center text-shadow" colspan="4">
                            <i class="fa fa-2x icon-ghost"></i>
                            <g:message code="default.no.records.found"/>
                        </td>
                    </tr>
                </g:else>
            </tbody>
        </table>

        <script type="text/javascript">
            var id = null;
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
            function crearEditarPuesto(id) {
                var title = id ?
                            "${message(code: 'default.edit.label', args:[message(code: 'puesto.label')])}" :
                            "${message(code: 'default.create.label', args:[message(code: 'puesto.label')])}";
                var data = id ? { id: id } : {};
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
                $(".btnCrear").click(function() {
                    crearEditarPuesto();
                    return false;
                });
                $(".btnEditar").click(function() {
                    crearEditarPuesto($(this).parents("tr").data("id"));
                    return false;
                });
                $(".btnEliminar").click(function() {
                    eliminarPuesto($(this).parents("tr").data("id"));
                    return false;
                });
            });
        </script>

    </body>
</html>