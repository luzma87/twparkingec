<%@ page import="ec.com.tw.parking.Puesto" %>

<g:if test="${puestoInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>

    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmPuesto" id="${puestoInstance?.id}"
                role="form" action="save_ajax" method="POST">

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: puestoInstance, field: 'tamanio', 'error')} required">
                    <label for="tamanio" class="col-md-4 control-label">
                        <g:message code="puesto.tamanio.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <g:select name="tamanio" class="form-control"
                                  from="${ec.com.tw.parking.Tamanio?.values()}"
                                  keys="${ec.com.tw.parking.Tamanio.values()*.name()}"
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

                    <div class="col-md-8">
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

                    <div class="col-md-8">
                        <g:field type="number" name="precio" value="${fieldValue(bean: puestoInstance, field: 'precio')}" required=""/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: puestoInstance, field: 'edificio', 'error')} required">
                    <label for="edificio" class="col-md-4 control-label">
                        <g:message code="puesto.edificio.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <g:select id="edificio" name="edificio.id" from="${ec.com.tw.parking.Edificio.list()}" optionKey="id" required="" value="${puestoInstance?.edificio?.id}" class="many-to-one form-control"/>

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