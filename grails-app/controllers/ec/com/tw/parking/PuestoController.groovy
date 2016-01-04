package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class PuestoController extends Shield {

    def crudHelperService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [puestoInstanceList: Puesto.list(), puestoInstanceCount: Puesto.count()]
    }

    def form_ajax() {
        def puestoInstance = crudHelperService.obtenerObjeto(Puesto, params.id)
        return [puestoInstance: puestoInstance]
    }

    def save_ajax() {
        def puestoInstance = crudHelperService.obtenerObjeto(Puesto, params.id)
        if (!puestoInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'puesto')
            return
        }
        render crudHelperService.guardarObjeto(puestoInstance, params)
    }

    def delete_ajax() {
        def puestoInstance = crudHelperService.obtenerObjeto(Puesto, params.id)
        if (!puestoInstance || !puestoInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'puesto')
            return
        }
        render crudHelperService.eliminarObjeto(puestoInstance)
    }
}