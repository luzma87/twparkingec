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
        flash.message = "Mail exitosamente"
        flash.tipo = "success"
        redirect(controller: "asignacionPuesto", action: "index")
    }

    def historial() {

    }

    def list() {
        return [puestosSinAsignacion         : getPuestosSinAsignacion(),
                autosSinAsignacion           : getAutosSinAsignacion(),
                asignacionPuestoInstanceList : AsignacionPuesto.list(),
                asignacionPuestoInstanceCount: AsignacionPuesto.count()]
    }

    def form_ajax() {
        def asignacionPuestoInstance = crudHelperService.obtenerObjeto(AsignacionPuesto, params.id)
        return [puestosSinAsignacion    : getPuestosSinAsignacion(),
                autosSinAsignacion      : getAutosSinAsignacion(),
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

    def getPuestosSinAsignacion() {
        def todosPuestos = Puesto.list()
        def puestosConAsignacion = AsignacionPuesto.withCriteria {
            projections {
                distinct("puesto")
            }
        }
        def puestosEnAmbasListas = todosPuestos.intersect(puestosConAsignacion)
        def puestosSinAsignacion = todosPuestos + puestosConAsignacion
        puestosSinAsignacion.removeAll(puestosEnAmbasListas)

        def puestosSinAsignacionActiva = []
        todosPuestos.each { puesto ->
            if (AsignacionPuesto.countByPuestoAndFechaLiberacionIsNotNull(puesto) > 0 &&
                AsignacionPuesto.countByPuestoAndFechaLiberacionIsNull(puesto) == 0) {
                puestosSinAsignacionActiva += puesto
            }
        }
        puestosSinAsignacion += puestosSinAsignacionActiva
        return puestosSinAsignacion
    }

    def getAutosSinAsignacion() {
        def todosAutos = Auto.withCriteria {
            usuario {
                eq("estaActivo", true)
            }
        }
        def autosConAsignacion = AsignacionPuesto.withCriteria {
            auto {
                usuario {
                    eq("estaActivo", true)
                }
            }
            projections {
                distinct("auto")
            }
        }
        def autosEnAmbasListas = todosAutos.intersect(autosConAsignacion)
        def autosSinAsignacion = todosAutos + autosConAsignacion
        autosSinAsignacion.removeAll(autosEnAmbasListas)
        return autosSinAsignacion
    }
}