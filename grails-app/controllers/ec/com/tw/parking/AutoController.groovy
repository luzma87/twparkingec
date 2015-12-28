package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class AutoController extends Shield {

    def crudHelperService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [autoInstanceList: Auto.list(), autoInstanceCount: Auto.count()]
    }

    def form_ajax() {
        def autoInstance = crudHelperService.obtenerObjeto(Auto, params.id)
        return [autoInstance: autoInstance]
    }

    def save_ajax() {
        def autoInstance = crudHelperService.obtenerObjeto(Auto, params.id)
        if (!autoInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'auto')
            return
        }
        render crudHelperService.guardarObjeto('auto', autoInstance, params)
    }

    def delete_ajax() {
        def autoInstance = crudHelperService.obtenerObjeto(Auto, params.id)
        if (!autoInstance || !autoInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'auto')
            return
        }
        render crudHelperService.eliminarObjeto('auto', autoInstance)
    }
}