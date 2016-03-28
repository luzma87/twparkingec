package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield
import ec.com.tw.parking.enums.Mes

class PagoController extends Shield {

    def crudHelperService
    def calculadorCuotaService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        def anioActual = new Date().format("yyyy").toInteger()
        def pagos = [:]
        Usuario.list().each { usuario ->
            Mes.values().each { mes ->
                def pagosUsu = Pago.withCriteria {
                    eq("mes", mes)
                    eq("anio", anioActual)
                    eq("usuario", usuario)
                }
                if (pagosUsu.size() > 0) {
                    if (!pagos[usuario.email]) {
                        pagos[usuario.email] = [usuario: usuario, pagos: []]
                    }
                    pagos[usuario.email].pagos += pagosUsu
                }
            }
        }
        return [anio: anioActual, pagos: pagos]
    }

    def list() {
        def anioActual = new Date().format("yyyy").toInteger()
        if (!params.anio) {
            params.anio = anioActual
        }
        def pagos = [:]
        Usuario.findAllByEstaActivo(true).each { usuario ->
            Mes.values().each { mes ->
                def pagosUsu = Pago.withCriteria {
                    eq("mes", mes)
                    eq("anio", params.anio.toInteger())
                    eq("usuario", usuario)
                }
                if (pagosUsu.size() > 0) {
                    if (!pagos[usuario.email]) {
                        pagos[usuario.email] = [usuario: usuario, pagos: []]
                    }
                    pagos[usuario.email].pagos += pagosUsu
                }
            }
        }
        def anios = []
        for (int i = anioActual; i >= 2016; i--) {
            anios += i
        }
        def mesActual = new Date().format("MM").toInteger()
        def cuota = g.formatNumber(number: calculadorCuotaService.calcularCuota(), maxFractionDigits: 2, minFractionDigits: 2)
        return [anio : params.anio, pagos: pagos, anios: anios, mesActual: mesActual,
                cuota: cuota]
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