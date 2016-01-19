package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class AsignacionPuestoController extends Shield {

    def generadorNotificacionesService
    def enviarMailService

    def list() {}

    def reasignar() {
        def datosNotificacion = generadorNotificacionesService.generarNotificacion()
        enviarMailService.enviarMail(
            datosNotificacion.destinatarios.email,
            datosNotificacion.asunto,
            datosNotificacion.mensaje
        )
        flash.message = "Puestos reasignados exitosamente y mail de notificacion enviado"
        flash.tipo = "success"
        redirect(controller: "asignacionPuesto", action: "list")
    }

    def historial() {

    }
}
