<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 1/18/16
  Time: 17:38
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="main">
        <title>
            <g:message code="usuario.personal.label"/>
        </title>
    </head>

    <body>
        <div class="row">
            <div class="col-md-6 col-md-offset-3">
                <div class="panel panel-info">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <g:message code="usuario.password.change.label"/>
                        </h3>
                    </div>

                    <div class="panel-body">
                        <elm:flashMessage tipo="${flash.tipo}" clase="${flash.clase}">${flash.message}</elm:flashMessage>
                        <g:form class="form-horizontal" name="frmUsuario" id="${usuarioInstance?.id}"
                                role="form" action="save" method="POST">

                            <div class="row">
                                <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'password', 'error')} required">
                                    <label for="password" class="col-md-4 control-label">
                                        <g:message code="usuario.password.current.label"/>
                                        <span class="required-indicator">*</span>
                                    </label>

                                    <div class="col-md-8">
                                        <div class="input-group"><span class="input-group-addon"><i class="fa fa-unlock-alt"></i>
                                        </span><g:field type="password" name="old_password" maxlength="512" required=""
                                                        class="form-control  required"/>
                                        </div>

                                    </div>
                                </div>
                            </div>

                            <div class="row">
                                <div class="col-md-12 form-group ${hasErrors(bean: usuarioInstance, field: 'password', 'error')} required">
                                    <label for="password" class="col-md-4 control-label">
                                        <g:message code="usuario.password.new.label"/>
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

                            <div class="text-center">
                                <button type="submit" id="btnLogin" class="btn btn-success">
                                    <i class="fa fa-save"></i> Guardar
                                </button>
                            </div>
                        </g:form>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/javascript">
            $(function() {
                $("#frmUsuario").validate({
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
            });
        </script>
    </body>
</html>