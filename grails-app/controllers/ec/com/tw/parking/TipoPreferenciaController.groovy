package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class TipoPreferenciaController extends Shield {

    def crudHelperService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [tipoPreferenciaInstanceList: TipoPreferencia.list(), tipoPreferenciaInstanceCount: TipoPreferencia.count()]
    }

    def form_ajax() {
        def tipoPreferenciaInstance = crudHelperService.obtenerObjeto(TipoPreferencia, params.id)
        return [tipoPreferenciaInstance: tipoPreferenciaInstance]
    }

    def save_ajax() {
        def tipoPreferenciaInstance = crudHelperService.obtenerObjeto(TipoPreferencia, params.id)
        if(!tipoPreferenciaInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'tipoPreferencia')
            return
        }
        render crudHelperService.guardarObjeto(tipoPreferenciaInstance, params)
    }

    def delete_ajax() {
        def tipoPreferenciaInstance = crudHelperService.obtenerObjeto(TipoPreferencia, params.id)
        if(!tipoPreferenciaInstance || !tipoPreferenciaInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'tipoPreferencia')
            return
        }
        render crudHelperService.eliminarObjeto(tipoPreferenciaInstance)
    }
}