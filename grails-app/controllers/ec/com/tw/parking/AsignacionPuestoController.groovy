package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class AsignacionPuestoController extends Shield {

    def crudHelperService
    def generadorNotificacionesService
    def enviarMailService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        def ultimaAsignacion = AsignacionPuesto.withCriteria {
            projections {
                max("fechaAsignacion")
            }
        }.first()
        return [ultimaAsignacion: ultimaAsignacion]
    }

    def reasignar() {
        generadorNotificacionesService.generarNotificacion()
        flash.message = "Puestos reasignados exitosamente"
        flash.tipo = "success"
        redirect(controller: "asignacionPuesto", action: "index")
    }

    def enviarMails() {
        def datosNotificacion = generadorNotificacionesService.generadorNotificacionExito()
        enviarMailService.enviarMail(
            datosNotificacion.destinatarios.email,
            datosNotificacion.asunto,
            datosNotificacion.mensaje
        )
        flash.message = "Mail enviado exitosamente"
        flash.tipo = "success"
        redirect(controller: "asignacionPuesto", action: "index")
    }

    def verMails() {
        def datosNotificacion = generadorNotificacionesService.generadorNotificacionExito()
        return [notificacion: datosNotificacion]
    }

    def historial() {

    }

    def list() {
        return [puestosSinAsignacion         : Puesto.obtenerSinAsignacion(),
                autosSinAsignacion           : Auto.obtenerSinAsignacion(),
                asignacionPuestoInstanceList : AsignacionPuesto.list(),
                asignacionPuestoInstanceCount: AsignacionPuesto.count()]
    }

    def form_ajax() {
        def asignacionPuestoInstance = crudHelperService.obtenerObjeto(AsignacionPuesto, params.id)
        return [puestosSinAsignacion    : Puesto.obtenerSinAsignacion(),
                autosSinAsignacion      : Auto.obtenerSinAsignacion(),
                asignacionPuestoInstance: asignacionPuestoInstance]
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

    def liberar_ajax() {
        def asignacion = AsignacionPuesto.get(params.id)
        asignacion.liberar()
        render "SUCCESS*Asignación liberada exitosamente"
    }
}