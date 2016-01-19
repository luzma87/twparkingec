package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class AsignacionPuestoController extends Shield {

    def crudHelperService
    def generadorNotificacionesService
    def enviarMailService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
    }

    def reasignar() {
        def datosNotificacion = generadorNotificacionesService.generarNotificacion()
        enviarMailService.enviarMail(
            datosNotificacion.destinatarios.email,
            datosNotificacion.asunto,
            datosNotificacion.mensaje
        )
        flash.message = "Puestos reasignados exitosamente y mail de notificacion enviado"
        flash.tipo = "success"
        redirect(controller: "asignacionPuesto", action: "index")
    }

    def historial() {

    }

    def list() {
        return [asignacionPuestoInstanceList: AsignacionPuesto.list(), asignacionPuestoInstanceCount: AsignacionPuesto.count()]
    }

    def form_ajax() {
        def asignacionPuestoInstance = crudHelperService.obtenerObjeto(AsignacionPuesto, params.id)
        return [asignacionPuestoInstance: asignacionPuestoInstance]
    }

    def save_ajax() {
        def asignacionPuestoInstance = crudHelperService.obtenerObjeto(AsignacionPuesto, params.id)
        if (!asignacionPuestoInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'asignacionPuesto')
            return
        }
        render crudHelperService.guardarObjeto(asignacionPuestoInstance, params)
    }

    def delete_ajax() {
        def asignacionPuestoInstance = crudHelperService.obtenerObjeto(AsignacionPuesto, params.id)
        if (!asignacionPuestoInstance || !asignacionPuestoInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'asignacionPuesto')
            return
        }
        render crudHelperService.eliminarObjeto(asignacionPuestoInstance)
    }
}