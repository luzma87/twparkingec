<%@ page import="ec.com.tw.parking.AsignacionPuesto" %>

<g:if test="${asignacionPuestoInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>

    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmAsignacionPuesto" id="${asignacionPuestoInstance?.id}"
                role="form" action="save_ajax" method="POST">

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: asignacionPuestoInstance, field: 'auto', 'error')} required">
                    <label for="auto" class="col-md-4 control-label">
                        <g:message code="asignacionPuesto.auto.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <g:select id="auto" name="auto.id" from="${ec.com.tw.parking.Auto.list()}" optionKey="id" required="" value="${asignacionPuestoInstance?.auto?.id}" class="many-to-one form-control"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: asignacionPuestoInstance, field: 'puesto', 'error')} required">
                    <label for="puesto" class="col-md-4 control-label">
                        <g:message code="asignacionPuesto.puesto.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <g:select id="puesto" name="puesto.id" from="${ec.com.tw.parking.Puesto.list()}" optionKey="id" required="" value="${asignacionPuestoInstance?.puesto?.id}" class="many-to-one form-control"/>

                    </div>
                </div>
            </div>
        </g:form>
    </div>

    <script type="text/javascript">
        var validator = $("#frmAsignacionPuesto").validate({
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
                guardarAsignacionPuesto();
                return false;
            }
            return true;
        });
    </script>
</g:if>
<g:else>
    <msgBuilder:noEncontradoHtml entidad="asignacionPuesto"/>
</g:else>