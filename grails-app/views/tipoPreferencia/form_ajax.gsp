<%@ page import="ec.com.tw.parking.TipoPreferencia" %>

<g:if test="${tipoPreferenciaInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>
    
    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmTipoPreferencia" id="${tipoPreferenciaInstance?.id}"
                role="form" action="save_ajax" method="POST">
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: tipoPreferenciaInstance, field: 'codigo', 'error')} required">
                    <label for="codigo" class="col-md-4 control-label">
                        <g:message code="tipoPreferencia.codigo.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:textField name="codigo" maxlength="1" required="" class="form-control  required unique noEspacios" value="${tipoPreferenciaInstance?.codigo}"/>

                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: tipoPreferenciaInstance, field: 'descripcion', 'error')} required">
                    <label for="descripcion" class="col-md-4 control-label">
                        <g:message code="tipoPreferencia.descripcion.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:textField name="descripcion" maxlength="10" required="" class="form-control  required" value="${tipoPreferenciaInstance?.descripcion}"/>

                    </div>
                </div>
            </div>
            
        </g:form>
    </div>

    <script type="text/javascript">
        var validator = $("#frmTipoPreferencia").validate({
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
                guardarTipoPreferencia();
                return false;
            }
            return true;
        });
    </script>
</g:if>
<g:else>
    <msgBuilder:noEncontradoHtml entidad="tipoPreferencia"/>
</g:else>