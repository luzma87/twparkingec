package ec.com.tw.parking

import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification

import static RandomUtilsHelpers.getRandomDouble
import static RandomUtilsHelpers.getRandomInt
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomDouble

@TestFor(MensajeFactoryService)
class MensajeFactoryServiceSpec extends Specification {

    def totalUsuariosActivos
    def puestosFaltantes

    def setup() {
        totalUsuariosActivos = getRandomInt(1, 15)
        puestosFaltantes = getRandomInt(1, 15)
    }

    void "Debe devolver un mensaje de advertencia si la cantidad de edificios ampliables es 0"() {
        setup:
        GroovyMock(Edificio, global: true)
        Edificio.findAllByEsAmpliable(true) >> []
        def mensajeEsperado = "No se encontraron edificios ampliables, no se pudo recalcular la cuota"

        expect:
        service.construirMensajePuestosFaltantes(puestosFaltantes) == mensajeEsperado
    }

    void "Debe devolver un mensaje de advertencia si con la cantidad de edificios ampliables si esta es mayor a 1"() {
        setup:
        def cantEdificios = getRandomInt(2, 15)
        def edificios = []
        cantEdificios.times {
            edificios += EdificioBuilder.nuevo().crear()
        }
        GroovyMock(Edificio, global: true)
        Edificio.findAllByEsAmpliable(true) >> edificios
        def mensajeEsperado = "Se encontraron " + cantEdificios + " edificios ampliables, no se pudo recalcular la cuota"

        expect:
        service.construirMensajePuestosFaltantes(puestosFaltantes) == mensajeEsperado
    }

    void "Debe devolver un mensaje con la cuota recalculada si la cantidad de edificios ampliables es 1"() {
        setup:
        def edificios = obtenerEdificios()
        def precio = obtenerPrecioPuesto(edificios.first())
        def nuevaCuota = getRandomDouble(100)
        def mensajeEsperado = formarMensajeAmpliable(edificios.first().nombre, precio, nuevaCuota)
        def calculadorCuotaServiceMock = mockCalculadorCuotaService()

        when:
        def respuesta = service.construirMensajePuestosFaltantes(puestosFaltantes)

        then:
        1 * calculadorCuotaServiceMock.calcularCuota(_, _) >> nuevaCuota
        respuesta == mensajeEsperado
    }

    private obtenerEdificios() {
        def edificios = [EdificioBuilder.nuevo().crear()]
        GroovyMock(Edificio, global: true)
        Edificio.findAllByEsAmpliable(true) >> edificios
        return edificios
    }

    private obtenerPrecioPuesto(edificio) {
        def precio = getRandomDouble(1, 100)
        def puesto = PuestoBuilder.nuevo().con { pb -> pb.precio = precio }.crear()
        GroovyMock(Puesto, global: true)
        Puesto.findByEdificioAndPrecioGreaterThan(edificio, 0) >> puesto
        return precio
    }

    private formarMensajeAmpliable(nombreEdificio, precio, nuevaCuota) {
        def mensajeEsperado = "Si se asume que los puestos faltantes se ubican en " + nombreEdificio
        mensajeEsperado += " (\$" + precio + "), la nueva cuota sería \$" + nuevaCuota
        return mensajeEsperado
    }

    private mockCalculadorCuotaService() {
        CalculadorCuotaService calculadorCuotaServiceMock = Mock(CalculadorCuotaService)
        service.calculadorCuotaService = calculadorCuotaServiceMock
        return calculadorCuotaServiceMock
    }
}
