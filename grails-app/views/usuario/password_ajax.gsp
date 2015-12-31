<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 12/30/15
  Time: 16:34
--%>

<%@ page import="ec.com.tw.parking.Usuario" %>

<g:if test="${usuarioInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>

    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmUsuario" id="${usuarioInstance?.id}"
                role="form" action="save_ajax" method="POST">

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