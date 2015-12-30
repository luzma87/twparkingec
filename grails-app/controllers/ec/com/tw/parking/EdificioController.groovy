package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class EdificioController extends Shield {

    def crudHelperService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [edificioInstanceList: Edificio.list(), edificioInstanceCount: Edificio.count()]
    }

    def form_ajax() {
        def edificioInstance = crudHelperService.obtenerObjeto(Edificio, params.id)
        return [edificioInstance: edificioInstance]
    }

    def save_ajax() {
        def edificioInstance = crudHelperService.obtenerObjeto(Edificio, params.id)
        if (!edificioInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'edificio')
            return
        }
        render crudHelperService.guardarObjeto('edificio', edificioInstance, params)
    }

    def delete_ajax() {
        def edificioInstance = crudHelperService.obtenerObjeto(Edificio, params.id)
        if (!edificioInstance || !edificioInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'edificio')
            return
        }
        render crudHelperService.eliminarObjeto('edificio', edificioInstance)
    }
}