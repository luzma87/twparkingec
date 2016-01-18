<%--
  Created by IntelliJ IDEA.
  User: lmunda
  Date: 1/18/16
  Time: 16:25
--%>

<%@ page contentType="text/html;charset=UTF-8" %>
<html>
    <head>
        <meta name="layout" content="login"/>
        <title></title>

        <style type="text/css">
        body {
            background: url("${resource(dir:'images/background', file: 'f849936a.png')}");
        }
        </style>

    </head>

    <body>
        <div class="row">
            <div class="col-md-4 col-md-offset-3">
                <div class="panel panel-primary">
                    <div class="panel-heading">
                        <h3 class="panel-title">
                            <g:message code="login.label"/>
                        </h3>
                    </div>

                    <div class="panel-body">
                        <elm:flashMessage tipo="${flash.tipo}" clase="${flash.clase}">${flash.message}</elm:flashMessage>
                        <g:form name="frmLogin" action="iniciarSesion">
                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-user"></i>
                                    </span>
                                    <g:textField class="form-control required"
                                                 placeholder="${g.message(code: 'usuario.label')}"
                                                 name="usuario" required=""/>
                                    <span class="input-group-addon">
                                        @thoughtworks.com
                                    </span>
                                </div>
                            </div>

                            <div class="form-group">
                                <div class="input-group">
                                    <span class="input-group-addon">
                                        <i class="fa fa-lock"></i>
                                    </span>
                                    <g:passwordField class="form-control required"
                                                     placeholder="${g.message(code: 'usuario.password.label')}"
                                                     name="password" required=""/>
                                </div>
                            </div>

                            <button type="submit" id="btnLogin" class="btn btn-block btn-success">
                                <i class="fa fa-sign-in"></i> <g:message code="login.enter.label"/>
                            </button>
                        </g:form>
                    </div>
                </div>
            </div>
        </div>

        <script type="text/javascript">
            $(function () {
                $("#frmLogin").validate({
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