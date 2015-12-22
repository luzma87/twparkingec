<%@ page import="ec.com.tw.parking.Usuario" %>

<script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>

<div class="modal-contenido">
    <g:form class="form-horizontal" name="frmUsuario" id="${usuarioInstance?.id}"
            role="form" action="save_ajax" method="POST">
        
        <div class="row">
            <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'nombre', 'error')} required">
                <label for="nombre" class="col-md-4 control-label">
                    <g:message code="usuario.nombre.label" default="Nombre" />
                    <span class="required-indicator">*</span>
                </label>
                <div class="col-md-8">
                    <g:textField name="nombre" maxlength="50" required="" class="form-control  required" value="${usuarioInstance?.nombre}"/>

                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'email', 'error')} required">
                <label for="email" class="col-md-4 control-label">
                    <g:message code="usuario.email.label" default="Email" />
                    <span class="required-indicator">*</span>
                </label>
                <div class="col-md-8">
                    <div class="input-group"><span class="input-group-addon"><i class="fa fa-envelope"></i></span><g:field type="email" name="email" maxlength="100" required="" class="form-control  required unique noEspacios" value="${usuarioInstance?.email}"/></div>

                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'password', 'error')} required">
                <label for="password" class="col-md-4 control-label">
                    <g:message code="usuario.password.label" default="Password" />
                    <span class="required-indicator">*</span>
                </label>
                <div class="col-md-8">
                    <div class="input-group"><span class="input-group-addon"><i class="fa fa-lock"></i></span><g:field type="password" name="password" maxlength="512" required="" class="form-control  required" value="${usuarioInstance?.password}"/></div>

                </div>
            </div>
        </div>
        
        <div class="row">
            <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'esAdmin', 'error')} ">
                <label for="esAdmin" class="col-md-4 control-label">
                    <g:message code="usuario.esAdmin.label" default="Es Admin" />
                    
                </label>
                <div class="col-md-8">
                    <g:checkBox name="esAdmin" value="${usuarioInstance?.esAdmin}" />

                </div>
            </div>
        </div>
        
    </g:form>
</div>

<script type="text/javascript">
    var validator = $("#frmUsuario").validate({
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
            guardarUsuario();
            return false;
        }
        return true;
    });
</script>
