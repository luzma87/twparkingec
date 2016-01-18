package ec.com.tw.parking

import ec.com.tw.parking.builders.PuestoBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification

import static RandomUtilsHelpers.getRandomDouble
import static RandomUtilsHelpers.getRandomInt
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomInt

@TestFor(CalculadorCuotaService)
class CalculadorCuotaServiceSpec extends Specification {

    def cantidad
    def puestos
    def precioTotal

    def setup() {
        cantidad = getRandomInt(5, 15)
        puestos = []
        cantidad.times {
            puestos += PuestoBuilder.nuevo().crear()
        }

        GroovyMock(Puesto, global: true)
        Puesto.list() >> puestos
        precioTotal = puestos.sum { it.precio }
    }

    void "Debe calcular la cuota por usuario del servicio de parqueadero"() {
        setup:
        def cuota = (precioTotal + 0.5) / cantidad
        GroovyMock(Usuario, global: true)
        Usuario.countByEstaActivo(true) >> cantidad

        expect:
        service.calcularCuota() == cuota
    }

    void "Debe calcular la cuota tomando en cuenta los puestos faltantes y el precio"() {
        setup:
        def puestosFaltantes = getRandomInt(1, 10)
        def precio = getRandomDouble(100)
        GroovyMock(Usuario, global: true)
        Usuario.countByEstaActivo(true) >> cantidad + puestosFaltantes

        def cuotaNueva = (precioTotal + (puestosFaltantes * precio) + 0.5) / (cantidad + puestosFaltantes)

        expect:
        service.calcularCuota(puestosFaltantes, precio) == cuotaNueva

    }
}
