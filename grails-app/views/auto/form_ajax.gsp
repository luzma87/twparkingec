<%@ page import="ec.com.tw.parking.Auto" %>

<g:if test="${autoInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>
    
    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmAuto" id="${autoInstance?.id}"
                role="form" action="save_ajax" method="POST">
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: autoInstance, field: 'marca', 'error')} required">
                    <label for="marca" class="col-md-4 control-label">
                        <g:message code="auto.marca.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:textField name="marca" maxlength="20" required="" class="form-control  required" value="${autoInstance?.marca}"/>

                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: autoInstance, field: 'modelo', 'error')} required">
                    <label for="modelo" class="col-md-4 control-label">
                        <g:message code="auto.modelo.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:textField name="modelo" maxlength="25" required="" class="form-control  required" value="${autoInstance?.modelo}"/>

                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: autoInstance, field: 'placa', 'error')} required">
                    <label for="placa" class="col-md-4 control-label">
                        <g:message code="auto.placa.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:textField name="placa" maxlength="8" required="" class="form-control  required" value="${autoInstance?.placa}"/>

                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: autoInstance, field: 'tamanio', 'error')} required">
                    <label for="tamanio" class="col-md-4 control-label">
                        <g:message code="auto.tamanio.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:select name="tamanio" from="${autoInstance.constraints.tamanio.inList}" required="" class="form-control  required" value="${autoInstance?.tamanio}" valueMessagePrefix="auto.tamanio"/>

                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: autoInstance, field: 'usuario', 'error')} required">
                    <label for="usuario" class="col-md-4 control-label">
                        <g:message code="auto.usuario.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:select id="usuario" name="usuario.id" from="${ec.com.tw.parking.Usuario.list()}" optionKey="id" required="" value="${autoInstance?.usuario?.id}" class="many-to-one"/>

                    </div>
                </div>
            </div>
            
        </g:form>
    </div>

    <script type="text/javascript">
        var validator = $("#frmAuto").validate({
            errorClass     : "help-block",
            errorPlacement : function (error, element) {
                if (element.parent().hasClass("input-group")) {
                    error.insertAfter(element.parent());
                } else {
                    error.insertAfter(element);
                }
                element.parents(".grupo").addClass('has-error');
            },
            success        : function (label) {
                label.parents(".grupo").removeClass('has-error');
                label.remove();
            }
        });
        $(".form-control").keydown(function (ev) {
            if (ev.keyCode == 13) {
                guardarAuto();
                return false;
            }
            return true;
        });
    </script>
</g:if>
<g:else>
    <msgBuilder:noEncontradoHtml entidad="auto"/>
</g:else>