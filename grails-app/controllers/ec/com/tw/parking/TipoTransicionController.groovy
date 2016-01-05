package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class TipoTransicionController extends Shield {

    def crudHelperService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [tipoTransicionInstanceList: TipoTransicion.list(), tipoTransicionInstanceCount: TipoTransicion.count()]
    }

    def form_ajax() {
        def tipoTransicionInstance = crudHelperService.obtenerObjeto(TipoTransicion, params.id)
        return [tipoTransicionInstance: tipoTransicionInstance]
    }

    def save_ajax() {
        def tipoTransicionInstance = crudHelperService.obtenerObjeto(TipoTransicion, params.id)
        if (!tipoTransicionInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'tipoTransicion')
            return
        }
        render crudHelperService.guardarObjeto(tipoTransicionInstance, params)
    }

    def delete_ajax() {
        def tipoTransicionInstance = crudHelperService.obtenerObjeto(TipoTransicion, params.id)
        if (!tipoTransicionInstance || !tipoTransicionInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'tipoTransicion')
            return
        }
        render crudHelperService.eliminarObjeto(tipoTransicionInstance)
    }
}