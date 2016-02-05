package ec.com.tw.parking

import ec.com.tw.parking.builders.PuestoBuilder
import spock.lang.Specification

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomDouble
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomInt

class CalculadorCuotaTest extends Specification {
    private cantidadUsuariosActivos
    private costoTransferencia = CalculadorCuota.COSTO_TRANSFERENCIA
    private precioTotal

    def setup() {
        cantidadUsuariosActivos = getRandomDouble(1, 10)
        def cantidad = getRandomInt(5, 15)
        def puestos = []
        cantidad.times {
            puestos += PuestoBuilder.nuevo().con { p -> p.estaActivo = true }.crear()
        }
        precioTotal = (double) (puestos.sum { it.precio })

        GroovyMock(Usuario, global: true)
        Usuario.countByEstaActivo(true) >> cantidadUsuariosActivos
        GroovyMock(Puesto, global: true)
        Puesto.findAllByEstaActivo(true) >> puestos
    }

    def "Debe retornar la suma de los precios de los puestos activos mas valor de transferencia dividido para los usuarios activos"() {
        setup:
        def valorEsperado = (cantidadUsuariosActivos + costoTransferencia) / precioTotal

        expect:
        CalculadorCuota.calcularCuota(0, 0) == valorEsperado
    }

    def "Debe retornar la cuota tomando en cuenta los puestos faltantes y su precio"() {
        setup:
        def puestosFaltantes = getRandomInt(1, 10)
        def precioPuestosFaltantes = getRandomDouble(1, 100)

        def cantidadTotalUsuarios = cantidadUsuariosActivos + puestosFaltantes
        precioTotal += (puestosFaltantes * precioPuestosFaltantes)
        def valorEsperado = (cantidadTotalUsuarios + costoTransferencia) / precioTotal

        expect:
        CalculadorCuota.calcularCuota(puestosFaltantes, precioPuestosFaltantes) == valorEsperado
    }
}
