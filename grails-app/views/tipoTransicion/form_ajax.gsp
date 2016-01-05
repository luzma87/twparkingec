<%@ page import="ec.com.tw.parking.TipoTransicion" %>

<g:if test="${tipoTransicionInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>
    
    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmTipoTransicion" id="${tipoTransicionInstance?.id}"
                role="form" action="save_ajax" method="POST">
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: tipoTransicionInstance, field: 'nombre', 'error')} required">
                    <label for="nombre" class="col-md-4 control-label">
                        <g:message code="tipoTransicion.nombre.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:textField name="nombre" maxlength="30" required="" class="form-control  required" value="${tipoTransicionInstance?.nombre}"/>

                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: tipoTransicionInstance, field: 'distanciaOrigen', 'error')} required">
                    <label for="distanciaOrigen" class="col-md-4 control-label">
                        <g:message code="tipoTransicion.distanciaOrigen.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:select id="distanciaOrigen" name="distanciaOrigen.id" from="${ec.com.tw.parking.DistanciaEdificio.list()}" optionKey="id" required="" value="${tipoTransicionInstance?.distanciaOrigen?.id}" class="many-to-one form-control"/>

                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: tipoTransicionInstance, field: 'distanciaDestino', 'error')} required">
                    <label for="distanciaDestino" class="col-md-4 control-label">
                        <g:message code="tipoTransicion.distanciaDestino.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:select id="distanciaDestino" name="distanciaDestino.id" from="${ec.com.tw.parking.DistanciaEdificio.list()}" optionKey="id" required="" value="${tipoTransicionInstance?.distanciaDestino?.id}" class="many-to-one form-control"/>

                    </div>
                </div>
            </div>
            
            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: tipoTransicionInstance, field: 'prioridad', 'error')} required">
                    <label for="prioridad" class="col-md-4 control-label">
                        <g:message code="tipoTransicion.prioridad.label" />
                        <span class="required-indicator">*</span>
                    </label>
                    <div class="col-md-8">
                        <g:field name="prioridad" type="number" min="1" value="${tipoTransicionInstance.prioridad}" required=""/>

                    </div>
                </div>
            </div>
            
        </g:form>
    </div>

    <script type="text/javascript">
        var validator = $("#frmTipoTransicion").validate({
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
                guardarTipoTransicion();
                return false;
            }
            return true;
        });
    </script>
</g:if>
<g:else>
    <msgBuilder:noEncontradoHtml entidad="tipoTransicion"/>
</g:else>