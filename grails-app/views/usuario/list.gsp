<%@ page import="ec.com.tw.parking.AsignacionPuesto; ec.com.tw.parking.Usuario" %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="default.list.label" args="[message(code: 'usuario.label')]"/>
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
                        <g:link controller="usuario" action="list" class="btn btn-default btn-search">
                            <i class="fa fa-search"></i>&nbsp;
                        </g:link>
                    </span>
                </div><!-- /input-group -->
            </div>
        </div>

        <table class="table table-condensed table-bordered table-striped table-hover margin-top">
            <thead>
                <tr>
                    <th></th>
                    <g:sortableColumn property="nombre" title="${message(code: 'usuario.nombre.label')}"/>

                    <g:sortableColumn property="email" title="${message(code: 'usuario.email.label')}"/>

                    <g:sortableColumn property="esAdmin" title="${message(code: 'usuario.esAdmin.label')}"/>

                    <g:sortableColumn property="cedula" title="${message(code: 'usuario.cedula.label')}"/>

                    <th><g:message code="usuario.autos.label"/></th>

                    <th style="width: 105px"><g:message code="default.button.actions.label"/></th>
                </tr>
            </thead>
            <tbody>
                <g:if test="${usuarioInstanceCount > 0}">
                    <g:each in="${usuarioInstanceList}" status="i" var="usuarioInstance">
                        <tr data-id="${usuarioInstance.id}">
                            <td>${i + 1}</td>
                            <td>${usuarioInstance.nombre}</td>

                            <td><g:fieldValue bean="${usuarioInstance}" field="email"/></td>

                            <td><g:formatBoolean boolean="${usuarioInstance.esAdmin}" false="${message(code: 'default.boolean.no')}" true="${message(code: 'default.boolean.yes')}"/></td>

                            <td><g:fieldValue bean="${usuarioInstance}" field="cedula"/></td>

                            <td>
                                <ul class="fa-ul">
                                    <g:each in="${usuarioInstance.autos}" var="auto">
                                        <li data-id="${auto.id}">
                                            <i class="fa fa-car"></i>
                                            <a href="#" class="btnEditarAuto"
                                               title="${message(code: 'default.button.edit.label')}">
                                                ${auto.toStringCorto()}
                                            </a>
                                            <g:if test="${AsignacionPuesto.countByAuto(auto) == 0}">
                                                <a href="#" class="btn btn-link btnEliminarAuto"
                                                   title="${message(code: 'default.button.delete.label')}">
                                                    <i class="fa fa-trash"></i>
                                                </a>
                                            </g:if>
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
                                    <a href="#" class="btnAgregarAuto btn btn-primary"
                                       title="${message(code: 'default.button.add.car.label')}">
                                        <i class="fa fa-car"></i>
                                    </a>
                                    <a href="#" class="btnPassword btn btn-warning"
                                       title="${message(code: 'default.button.password.label')}">
                                        <i class="fa fa-unlock"></i>
                                    </a>
                                    <g:if test="${usuarioInstance.estaActivo}">
                                        <a href="#" class="btnDesactivar btn btn-success"
                                           title="${message(code: 'default.button.disable.label')}">
                                            <i class="fa fa-toggle-on"></i>
                                        </a>
                                    </g:if>
                                    <g:else>
                                        <a href="#" class="btnActivar btn btn-danger"
                                           title="${message(code: 'default.button.enable.label')}">
                                            <i class="fa fa-toggle-off"></i>
                                        </a>
                                    </g:else>
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
            function guardarUsuario() {
                var $form = $("#frmUsuario");
                if ($form.valid()) {
                    openLoader("${message(code: 'default.saving', args:[message(code: 'usuario.label')])}");
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
            function crearEditarUsuario(id) {
                var title = id ?
                            "${message(code: 'default.edit.label', args:[message(code: 'usuario.label')])}" :
                            "${message(code: 'default.create.label', args:[message(code: 'usuario.label')])}";
                var data = id ? {id : id} : {};
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(controller:'usuario', action:'form_ajax')}",
                    data    : data,
                    success : function (msg) {
                        var b = bootbox.dialog({
                            id      : "dlgCreateEditUsuario",
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
                                        return guardarUsuario();
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
            function editarPassword(id) {
                var title = "${message(code: 'default.edit.password.label')}";
                var data = id ? {id : id} : {};
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(controller:'usuario', action:'password_ajax')}",
                    data    : data,
                    success : function (msg) {
                        var b = bootbox.dialog({
                            id      : "dlgEditPassUsuario",
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
                                        return guardarUsuario();
                                    } //callback
                                } //guardar
                            } //buttons
                        }); //dialog
                        setTimeout(function () {
                            b.find(".form-control").first().focus()
                        }, 500);
                    } //success
                }); //ajax
            }
            function cambiarEstadoUsuario(id, activar) {
                var title = "${message(code: 'default.alert.disable.user.title')}";
                var icon = "fa-toggle-off";
                var clase = "danger";
                var message = "${message(code:'default.disable.confirm.message')}";
                var boton = "${message(code:'default.button.disable.label')}";
                var loader = "${message(code: 'default.disabling', args:[message(code: 'usuario.label')])}";
                if (activar) {
                    title = "${message(code:'default.alert.enable.user.title')}";
                    icon = "fa-toggle-on";
                    clase = "success";
                    message = "${message(code:'default.enable.confirm.message')}";
                    boton = "${message(code:'default.button.enable.label')}";
                    loader = "${message(code: 'default.enabling', args:[message(code: 'usuario.label')])}";
                }
                bootbox.dialog({
                    title   : title,
                    message : "<i class='fa " + icon + " fa-3x pull-left text-" + clase + " text-shadow'></i><p>" + message + "</p>",
                    buttons : {
                        cancelar : {
                            label     : "${message(code: 'default.button.cancel.label')}",
                            className : "btn-primary",
                            callback  : function () {
                            }
                        },
                        accion   : {
                            label     : "<i class='fa " + icon + "'></i> " + boton,
                            className : "btn-" + clase,
                            callback  : function () {
                                openLoader(loader);
                                $.ajax({
                                    type    : "POST",
                                    url     : '${createLink(controller:'usuario', action:'cambiarEstado')}',
                                    data    : {
                                        id      : id,
                                        activar : activar
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

            function guardarAuto() {
                var $form = $("#frmAuto");
                if ($form.valid()) {
                    openLoader("${message(code: 'default.saving', args:[message(code: 'auto.label')])}");
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
            function crearEditarAuto(autoId, usuarioId) {
                var title = autoId ?
                            "${message(code: 'default.edit.label', args:[message(code: 'auto.label')])}" :
                            "${message(code: 'default.add.label', args:[message(code: 'auto.label')])}";
                var data = autoId ? {id : autoId} : {};
                data.usuario = usuarioId ? usuarioId : null;
                $.ajax({
                    type    : "POST",
                    url     : "${createLink(controller:'auto', action:'form_ajax')}",
                    data    : data,
                    success : function (msg) {
                        var b = bootbox.dialog({
                            id      : "dlgCreateEditAuto",
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
                                        return guardarAuto();
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
            function eliminarAuto(itemId) {
                bootbox.dialog({
                    title   : "${message(code: 'default.alert.title')}",
                    message : "<i class='fa fa-trash-o fa-3x pull-left text-danger text-shadow'></i><p>" +
                              "${message(code: 'default.delete.confirm.message', args:[message(code: 'auto.label')])}</p>",
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
                                openLoader("${message(code: 'default.deleting', args:[message(code: 'auto.label')])}");
                                $.ajax({
                                    type    : "POST",
                                    url     : '${createLink(controller:'auto', action:'delete_ajax')}',
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

            $(function () {
                $(".btnCrear").click(function () {
                    crearEditarUsuario();
                    return false;
                });
                $(".btnEditar").click(function () {
                    crearEditarUsuario($(this).parents("tr").data("id"));
                    return false;
                });
                $(".btnPassword").click(function () {
                    editarPassword($(this).parents("tr").data("id"));
                    return false;
                });
                $(".btnActivar").click(function () {
                    cambiarEstadoUsuario($(this).parents("tr").data("id"), true);
                    return false;
                });
                $(".btnDesactivar").click(function () {
                    cambiarEstadoUsuario($(this).parents("tr").data("id"), false);
                    return false;
                });
                $(".btnEditarAuto").click(function () {
                    crearEditarAuto($(this).parents("li").data("id"));
                    return false;
                });
                $(".btnAgregarAuto").click(function () {
                    crearEditarAuto(null, $(this).parents("tr").data("id"));
                    return false;
                });
                $(".btnEliminarAuto").click(function () {
                    eliminarAuto($(this).parents("li").data("id"));
                    return false;
                });
            });
        </script>

    </body>
</html>