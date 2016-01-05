<% import grails.persistence.Event %>
<%=packageName%>
<% classNameLower = domainClass.propertyName %>
<!DOCTYPE html>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="default.list.label" args="[message(code: '${domainClass.propertyName}.label')]"/>
        </title>
    </head>
    <body>

        <elm:flashMessage tipo="\${flash.tipo}" clase="\${flash.clase}">\${flash.message}</elm:flashMessage>

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
                    <input type="text" class="form-control input-search" placeholder="Buscar" value="\${params.search}">
                    <span class="input-group-btn">
                        <g:link controller="${classNameLower}" action="list" class="btn btn-default btn-search">
                            <i class="fa fa-search"></i>&nbsp;
                        </g:link>
                    </span>
                </div><!-- /input-group -->
            </div>
        </div>

        <table class="table table-condensed table-bordered table-striped table-hover margin-top">
            <thead>
                <tr>
                    <% excludedProps = Event.allEvents.toList() << 'id' << 'version' << 'password'
                    allowedNames = domainClass.persistentProperties*.name << 'dateCreated' << 'lastUpdated'
                    props = domainClass.properties.findAll {
                        allowedNames.contains(it.name) && !excludedProps.contains(it.name) && it.type != null && !Collection.isAssignableFrom(it.type) && (domainClass.constrainedProperties[it.name] ? domainClass.constrainedProperties[it.name].display : true)
                    }
                    colCount = 0
                    Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                    props.eachWithIndex { p, i ->
                        if (i < 6) {
                            colCount ++
                            if (p.isAssociation()) { %>
                    <th><g:message code="${domainClass.propertyName}.${p.name}.label" /></th>
                    <% } else { %>
                    <g:sortableColumn property="${p.name}" title="\${message(code: '${domainClass.propertyName}.${
                            p.name}.label')}"/>
                    <% }
                    }
                    } %>
                    <th style="width: 76px"><g:message code="default.button.actions.label"/> </th>
                </tr>
            </thead>
            <tbody>
                <g:if test="\${${propertyName}Count > 0}">
                    <g:each in="\${${propertyName}List}" status="i" var="${propertyName}">
                        <tr data-id="\${${propertyName}.id}">
                            <%  props.eachWithIndex { p, i ->
                                boolean bool = p.type == Boolean || p.type == boolean
                                boolean number = Number.isAssignableFrom(p.type) || (p.type?.isPrimitive() && p. type != boolean)
                                boolean date = p.type == Date || p.type == java.sql.Date || p.type == java.sql.Time || p.type == Calendar
                                if (i == 0) { %>
                            <td>\${${propertyName}.${p.name}}</td>
                            <%      } else if (i < 6) {
                                if (bool) { %>
                            <td><g:formatBoolean boolean="\${${propertyName}.${p.name}}" false="\${message(code: 'default.boolean.no')}" true="\${message(code: 'default.boolean.yes')}" /></td>
                            <%          } else if (date) { %>
                            <td><g:formatDate date="\${${propertyName}.${p.name}}" format="\${message(code: 'default.date.format')}" /></td>
                            <%          } else if (number) { %>
                            <td><g:fieldValue bean="\${${propertyName}}" field="${p.name}"/></td>
                            <%          } else { %>
                            <td><g:fieldValue bean="\${${propertyName}}" field="${p.name}"/></td>
                                <%  }   }   } %>
                            <td>
                                <div class="btn-group btn-group-sm">
                                    <a href="#" class="btnEditar btn btn-info"
                                       title="\${message(code:'default.button.edit.label')}">
                                        <i class="fa fa-pencil"></i>
                                    </a>
                                    <a href="#" class="btnEliminar btn btn-danger"
                                       title="\${message(code:'default.button.delete.label')}">
                                        <i class="fa fa-trash"></i>
                                    </a>
                                </div>
                            </td>
                        </tr>
                    </g:each>
                </g:if>
                <g:else>
                    <tr class="info">
                        <td class="text-center text-shadow" colspan="${colCount + 1}">
                            <i class="fa fa-2x icon-ghost"></i>
                            <g:message code="default.no.records.found"/>
                        </td>
                    </tr>
                </g:else>
            </tbody>
        </table>

        <script type="text/javascript">
            var id = null;
            function guardar${domainClass.propertyName.capitalize()}() {
                var \$form = \$("#frm${domainClass.propertyName.capitalize()}");
                if (\$form.valid()) {
                    openLoader("\${message(code: 'default.saving', args:[message(code: '${domainClass.propertyName}.label')])}");
                    \$.ajax({
                        type    : "POST",
                        url     : \$form.attr("action"),
                            data    : \$form.serialize(),
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
                        log("\${message(code: 'default.internal.error')}", "Error");
                        closeLoader();
                    }
                });
                } else {
                    return false;
                } //else
            }
            function eliminar${domainClass.propertyName.capitalize()}(itemId) {
                bootbox.dialog({
                    title   : "\${message(code: 'default.alert.title')}",
                    message : "<i class='fa fa-trash-o fa-3x pull-left text-danger text-shadow'></i><p>" +
                              "\${message(code: 'default.delete.confirm.message', args:[message(code: '${domainClass.propertyName}.label')])}</p>",
                    buttons : {
                        cancelar : {
                            label     : "\${message(code: 'default.button.cancel.label')}",
                            className : "btn-primary",
                            callback  : function () {
                            }
                        },
                        eliminar : {
                            label     : "<i class='fa fa-trash-o'></i> \${message(code: 'default.button.delete.label')}",
                            className : "btn-danger",
                            callback  : function () {
                                openLoader("\${message(code: 'default.deleting', args:[message(code: '${domainClass.propertyName}.label')])}");
                                \$.ajax({
                                    type    : "POST",
                                    url     : '\${createLink(controller:'${classNameLower}', action:'delete_ajax')}',
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
                                        log("\${message(code: 'default.internal.error')}", "Error");
                                        closeLoader();
                                    }
                                });
                            }
                        }
                    }
                });
            }
            function crearEditar${domainClass.propertyName.capitalize()}(id) {
                var title = id ?
                            "\${message(code: 'default.edit.label', args:[message(code: '${domainClass.propertyName}.label')])}" :
                            "\${message(code: 'default.create.label', args:[message(code: '${domainClass.propertyName}.label')])}";
                var data = id ? { id: id } : {};
                \$.ajax({
                    type    : "POST",
                    url     : "\${createLink(controller:'${classNameLower}', action:'form_ajax')}",
                    data    : data,
                    success : function (msg) {
                        var b = bootbox.dialog({
                            id      : "dlgCreateEdit${domainClass.propertyName.capitalize()}",
                            title   : title,
                            message : msg,
                            buttons : {
                                cancelar : {
                                    label     : "\${message(code: 'default.button.cancel.label')}",
                                    className : "btn-primary",
                                    callback  : function () {
                                    }
                                },
                                guardar  : {
                                    id        : "btnSave",
                                    label     : "<i class='fa fa-save'></i> \${message(code: 'default.button.save.label')}",
                                    className : "btn-success",
                                    callback  : function () {
                                        return guardar${domainClass.propertyName.capitalize()}();
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
            \$(function () {
                \$(".btnCrear").click(function() {
                    crearEditar${domainClass.propertyName.capitalize()}();
                    return false;
                });
                \$(".btnEditar").click(function() {
                    crearEditar${domainClass.propertyName.capitalize()}(\$(this).parents("tr").data("id"));
                    return false;
                });
                \$(".btnEliminar").click(function() {
                    eliminar${domainClass.propertyName.capitalize()}(\$(this).parents("tr").data("id"));
                    return false;
                });
            });
        </script>

    </body>
</html>