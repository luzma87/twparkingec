package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class PagoController extends Shield {

    def crudHelperService
    def calculadorCuotaService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [pagoInstanceList: Pago.list(), pagoInstanceCount: Pago.count()]
    }

    def form_ajax() {
        def cuota = calculadorCuotaService.calcularCuota()
        def pagoInstance = crudHelperService.obtenerObjeto(Pago, params.id)
        return [pagoInstance: pagoInstance, cuota: cuota]
    }

    def save_ajax() {
        def pagoInstance = crudHelperService.obtenerObjeto(Pago, params.id)
        if (!pagoInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'pago')
            return
        }
        render crudHelperService.guardarObjeto(pagoInstance, params)
    }

    def delete_ajax() {
        def pagoInstance = crudHelperService.obtenerObjeto(Pago, params.id)
        if (!pagoInstance || !pagoInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'pago')
            return
        }
        render crudHelperService.eliminarObjeto(pagoInstance)
    }
}