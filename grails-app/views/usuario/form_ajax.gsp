<%@ page import="ec.com.tw.parking.Usuario" %>

<g:if test="${usuarioInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>

    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmUsuario" id="${usuarioInstance?.id}"
                role="form" action="save_ajax" method="POST">

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'cedula', 'error')} required">
                    <label for="cedula" class="col-md-4 control-label">
                        <g:message code="usuario.cedula.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <g:textField name="cedula" maxlength="10" minlength="10" required="" class="form-control  required" value="${usuarioInstance?.cedula}"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'nombre', 'error')} required">
                    <label for="nombre" class="col-md-4 control-label">
                        <g:message code="usuario.nombre.label"/>
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
                        <g:message code="usuario.email.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <div class="input-group"><span class="input-group-addon"><i class="fa fa-envelope"></i>
                        </span><g:field type="email" name="email" maxlength="100" required="" class="form-control  required unique noEspacios" value="${usuarioInstance?.email}"/>
                        </div>

                    </div>
                </div>
            </div>

            <g:if test="${!usuarioInstance.password}">
                <div class="row">
                    <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'password', 'error')} required">
                        <label for="password" class="col-md-4 control-label">
                            <g:message code="usuario.password.label"/>
                            <span class="required-indicator">*</span>
                        </label>

                        <div class="col-md-8">
                            <div class="input-group"><span class="input-group-addon"><i class="fa fa-lock"></i>
                            </span><g:field type="password" name="password" maxlength="512" required=""
                                            class="form-control  required"/>
                            </div>

                        </div>
                    </div>
                </div>

                <div class="row">
                    <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'password', 'error')} required">
                        <label for="password" class="col-md-4 control-label">
                            <g:message code="usuario.password.again.label"/>
                            <span class="required-indicator">*</span>
                        </label>

                        <div class="col-md-8">
                            <div class="input-group">
                                <span class="input-group-addon"><i class="fa fa-lock"></i></span>
                                <g:field type="password" name="password2" maxlength="512" required=""
                                         class="form-control  required"
                                         equalTo="#password"/>
                            </div>

                        </div>
                    </div>
                </div>
            </g:if>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'esAdmin', 'error')} ">
                    <label for="esAdmin" class="col-md-4 control-label">
                        <g:message code="usuario.esAdmin.label"/>

                    </label>

                    <div class="col-md-8">
                        <g:checkBox name="esAdmin" value="${usuarioInstance?.esAdmin}"/>

                    </div>
                </div>
            </div>

        </g:form>
    </div>

    <script type="text/javascript">
        var validator = $("#frmUsuario").validate({
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
                guardarUsuario();
                return false;
            }
            return true;
        });
    </script>
</g:if>
<g:else>
    <msgBuilder:noEncontradoHtml entidad="usuario"/>
</g:else>