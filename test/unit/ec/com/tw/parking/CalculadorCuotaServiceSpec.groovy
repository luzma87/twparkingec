package ec.com.tw.parking

import ec.com.tw.parking.builders.PuestoBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomInt

@TestFor(CalculadorCuotaService)
class CalculadorCuotaServiceSpec extends Specification {

    def setup() {
    }

    void "Debe calcular la cuota por usuario del servicio de parqueadero"() {
        setup:
        def cantidad = getRandomInt(10)
        def puestos = []
        cantidad.times {
            puestos += new PuestoBuilder().crear()
        }
        GroovyMock(Usuario, global: true)
        Usuario.countByEstaActivo(true) >> cantidad

        GroovyMock(Puesto, global: true)
        Puesto.list() >> puestos

        def precioTotal = puestos.sum { it.precio }
        def cuota = precioTotal / cantidad

        expect:
        service.calcularCuota() == cuota
    }
}
