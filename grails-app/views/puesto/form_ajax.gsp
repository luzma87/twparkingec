<%@ page import="ec.com.tw.parking.enums.Tamanio; ec.com.tw.parking.Puesto" %>

<g:if test="${puestoInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>

    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmPuesto" id="${puestoInstance?.id}"
                role="form" action="save_ajax" method="POST">
            <g:hiddenField name="edificio.id" value="${puestoInstance?.edificioId}"/>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: puestoInstance, field: 'edificio', 'error')} required">
                    <label class="col-md-4 control-label">
                        <g:message code="puesto.edificio.label"/>
                    </label>

                    <div class="col-md-8">
                        <p class="form-control-static">${puestoInstance?.edificio}</p>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: puestoInstance, field: 'tamanio', 'error')} required">
                    <label for="tamanio" class="col-md-4 control-label">
                        <g:message code="puesto.tamanio.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <g:select name="tamanio" class="form-control"
                                  from="${Tamanio?.values()}"
                                  keys="${Tamanio.values()*.name()}"
                                  required="" value="${puestoInstance?.tamanio?.name()}"
                                  valueMessagePrefix="tamanio"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: puestoInstance, field: 'numero', 'error')} required">
                    <label for="numero" class="col-md-4 control-label">
                        <g:message code="puesto.numero.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-3">
                        <g:textField name="numero" maxlength="6" required="" class="form-control  required" value="${puestoInstance?.numero}"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: puestoInstance, field: 'precio', 'error')} required">
                    <label for="precio" class="col-md-4 control-label">
                        <g:message code="puesto.precio.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-3">

                        <div class="input-group">
                            <g:field type="number" name="precio" class="form-control required"
                                     value="${fieldValue(bean: puestoInstance, field: 'precio')}" required=""/>
                            <span class="input-group-addon">
                                <i class="fa fa-usd"></i>
                            </span>
                        </div>

                    </div>
                </div>
            </div>

        </g:form>
    </div>

    <script type="text/javascript">
        var validator = $("#frmPuesto").validate({
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
                guardarPuesto();
                return false;
            }
            return true;
        });
    </script>
</g:if>
<g:else>
    <msgBuilder:noEncontradoHtml entidad="puesto"/>
</g:else>