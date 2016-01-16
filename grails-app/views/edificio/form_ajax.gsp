<%@ page import="ec.com.tw.parking.Edificio" %>

<g:if test="${edificioInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>

    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmEdificio" id="${edificioInstance?.id}"
                role="form" action="save_ajax" method="POST">

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: edificioInstance, field: 'nombre', 'error')} required">
                    <label for="nombre" class="col-md-4 control-label">
                        <g:message code="edificio.nombre.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <g:textField name="nombre" maxlength="50" required="" class="form-control  required" value="${edificioInstance?.nombre}"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: edificioInstance, field: 'distancia', 'error')} required">
                    <label for="distancia" class="col-md-4 control-label">
                        <g:message code="edificio.distancia.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <g:select id="distancia" name="distancia.id" from="${ec.com.tw.parking.DistanciaEdificio.list()}" optionKey="id" required="" value="${edificioInstance?.distancia?.id}" class="many-to-one form-control"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: edificioInstance, field: 'esAmpliable', 'error')} ">
                    <label for="esAmpliable" class="col-md-4 control-label">
                        <g:message code="edificio.esAmpliable.label"/>

                    </label>

                    <div class="col-md-8">
                        <g:checkBox name="esAmpliable" value="${edificioInstance?.esAmpliable}"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: edificioInstance, field: 'datosPago', 'error')} ">
                    <label for="datosPago" class="col-md-4 control-label">
                        <g:message code="edificio.datosPago.label"/>

                    </label>

                    <div class="col-md-8">
                        <g:textField name="datosPago" class="form-control " value="${edificioInstance?.datosPago}"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: edificioInstance, field: 'observaciones', 'error')} ">
                    <label for="observaciones" class="col-md-4 control-label">
                        <g:message code="edificio.observaciones.label"/>

                    </label>

                    <div class="col-md-8">
                        <g:textField name="observaciones" maxlength="150" class="form-control " value="${edificioInstance?.observaciones}"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: edificioInstance, field: 'puestos', 'error')} ">
                    <label class="col-md-4 control-label">
                        <g:message code="edificio.puestos.label"/>

                    </label>

                    <div class="col-md-8">

                        <ul class="one-to-many fa-ul">
                            <g:each in="${edificioInstance?.puestos ?}" var="p">
                                <li>
                                    <g:link controller="puesto" action="show" id="${p.id}">
                                        <i class="fa-li fa fa-cube"></i>
                                        ${p?.encodeAsHTML()}
                                    </g:link>
                                </li>
                            </g:each>
                            <li class="add">
                                <g:link controller="puesto" action="create" params="['edificio.id': edificioInstance?.id]">
                                    <i class="fa-li fa fa-plus"></i>
                                    ${message(code: 'default.add.label', args: [message(code: 'puesto.label', default: 'Puesto')])}
                                </g:link>
                            </li>
                        </ul>

                    </div>
                </div>
            </div>

        </g:form>
    </div>

    <script type="text/javascript">
        var validator = $("#frmEdificio").validate({
            errorElement   : "span",
            errorClass     : "text-danger",
            errorPlacement : function (error, element) {
                if (element.parent().hasClass("input-group")) {
                    error.insertAfter(element.parent());
                } else {
                    error.insertAfter(element);
                }
                element.parents(".form-group").addClass('has-error');
            },
            success        : function (label) {
                label.parents(".form-group").removeClass('has-error');
                label.remove();
            }
        });
        $(".form-control").keydown(function (ev) {
            if (ev.keyCode == 13) {
                guardarEdificio();
                return false;
            }
            return true;
        });
    </script>
</g:if>
<g:else>
    <msgBuilder:noEncontradoHtml entidad="edificio"/>
</g:else>