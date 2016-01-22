package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class AutoController extends Shield {

    def crudHelperService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def form_ajax() {
        Auto autoInstance = crudHelperService.obtenerObjeto(Auto, params.id)
        if (params.usuario != 'null') {
            autoInstance.usuario = Usuario.get(params.usuario)
        }
        return [autoInstance: autoInstance]
    }

    def save_ajax() {
        def autoInstance = crudHelperService.obtenerObjeto(Auto, params.id)
        if (!autoInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'auto')
            return
        }
        render crudHelperService.guardarObjeto(autoInstance, params)
    }

    def delete_ajax() {
        def autoInstance = crudHelperService.obtenerObjeto(Auto, params.id)
        if (!autoInstance || !autoInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'auto')
            return
        }
        render crudHelperService.eliminarObjeto(autoInstance)
    }
}