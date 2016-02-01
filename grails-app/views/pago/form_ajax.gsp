<%@ page import="ec.com.tw.parking.enums.Mes; ec.com.tw.parking.Pago" %>

<g:if test="${pagoInstance}">
    <script type="text/javascript" src="${resource(dir: 'js', file: 'ui.js')}"></script>

    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frmPago" id="${pagoInstance?.id}"
                role="form" action="save_ajax" method="POST">

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: pagoInstance, field: 'usuario', 'error')} required">
                    <label for="usuario" class="col-md-4 control-label">
                        <g:message code="pago.usuario.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <g:select id="usuario" name="usuario.id" from="${ec.com.tw.parking.Usuario.list()}" optionKey="id" required="" value="${pagoInstance?.usuario?.id}" class="many-to-one form-control"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: pagoInstance, field: 'fechaPago', 'error')} required">
                    <label for="fechaPago" class="col-md-4 control-label">
                        <g:message code="pago.fechaPago.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-8">
                        <elm:datepicker name="fechaPago" class="datepicker form-control  required"
                                        value="${pagoInstance?.fechaPago ?: new Date()}"/>

                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: pagoInstance, field: 'mes', 'error')} required">
                    <label for="mes" class="col-md-4 control-label">
                        <g:message code="pago.mes.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-4">
                        <g:select name="mes" class="form-control"
                                  from="${Mes?.values()}"
                                  keys="${Mes.values()*.name()}"
                                  required=""
                                  value="${pagoInstance?.mes?.name() ?:
                                          Mes.obtenerPorNumero(new Date().format('MM'))}"
                                  valueMessagePrefix="mes"/>
                    </div>

                    <div class="col-md-4">
                        <g:field type="number" name="anio" class="form-control required"
                                 value="${fieldValue(bean: pagoInstance, field: 'anio') ?:
                                         new Date().format('YYYY')}"
                                 required=""/>
                    </div>
                </div>
            </div>

            <div class="row">
                <div class="col-md-12 form-group ${hasErrors(bean: pagoInstance, field: 'monto', 'error')} required">
                    <label for="monto" class="col-md-4 control-label">
                        <g:message code="pago.monto.label"/>
                        <span class="required-indicator">*</span>
                    </label>

                    <div class="col-md-4">
                        <div class="input-group">
                            <g:textField name="monto" class="form-control required"
                                         value="${fieldValue(bean: pagoInstance, field: 'monto') ?:
                                                 g.formatNumber(number: cuota, maxFractionDigits: 2, minFractionDigits: 2)}"
                                         required=""/>
                            <span class="input-group-addon">
                                <i class="fa fa-usd"></i>
                            </span>
                        </div>

                    </div>
                </div>
            </div>

        </g:form>
    </div>

    <script type="text/javascript">
        var validator = $("#frmPago").validate({
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
                guardarPago();
                return false;
            }
            return true;
        });
    </script>
</g:if>
<g:else>
    <msgBuilder:noEncontradoHtml entidad="pago"/>
</g:else>