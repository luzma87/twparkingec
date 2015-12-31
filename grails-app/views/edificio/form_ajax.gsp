<%@ page import="ec.com.tw.parking.Edificio" %>

<g:if test="${edificioInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>
    
    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmEdificio" id="${edificioInstance?.id}"
                role="form" action="save_ajax" method="POST">
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: edificioInstance, field: 'nombre', 'error')} required">
                    <label for="nombre" class="col-md-4 control-label">
                        <g:message code="edificio.nombre.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:textField name="nombre" maxlength="50" required="" class="form-control  required" value="${edificioInstance?.nombre}"/>

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