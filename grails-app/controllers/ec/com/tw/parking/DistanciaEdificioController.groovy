package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class DistanciaEdificioController extends Shield {

    def crudHelperService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [distanciaEdificioInstanceList: DistanciaEdificio.list(), distanciaEdificioInstanceCount: DistanciaEdificio.count()]
    }

    def form_ajax() {
        def distanciaEdificioInstance = crudHelperService.obtenerObjeto(DistanciaEdificio, params.id)
        return [distanciaEdificioInstance: distanciaEdificioInstance]
    }

    def save_ajax() {
        def distanciaEdificioInstance = crudHelperService.obtenerObjeto(DistanciaEdificio, params.id)
        if(!distanciaEdificioInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'distanciaEdificio')
            return
        }
        render crudHelperService.guardarObjeto(distanciaEdificioInstance, params)
    }

    def delete_ajax() {
        def distanciaEdificioInstance = crudHelperService.obtenerObjeto(DistanciaEdificio, params.id)
        if(!distanciaEdificioInstance || !distanciaEdificioInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'distanciaEdificio')
            return
        }
        render crudHelperService.eliminarObjeto(distanciaEdificioInstance)
    }
}