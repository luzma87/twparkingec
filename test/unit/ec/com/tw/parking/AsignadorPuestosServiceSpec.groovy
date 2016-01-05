package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.DistanciaEdificioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomInt

@TestFor(AsignadorPuestosService)
@Mock([AsignacionPuesto])
class AsignadorPuestosServiceSpec extends Specification {

    def "Debe devolver una lista con todas las asignaciones de puesto"() {
        setup:
        def listaAsignaciones = []
        10.times {
            listaAsignaciones.add(new AsignacionPuestoBuilder().crear())
        }

        when:
        GroovyMock(AsignacionPuesto, global: true)
        AsignacionPuesto.list() >> listaAsignaciones
        def respuesta = service.obtenerTodasAsignaciones()

        then:
        respuesta == listaAsignaciones
    }

    def "Debe retornar un mapa con claves de distancia edificio"() {
        setup:
        def distancia1 = new DistanciaEdificioBuilder()
        def distancia2 = new DistanciaEdificioBuilder()
        def distancia3 = new DistanciaEdificioBuilder()
        def mapa = [:]
        def lista1 = crearListaPorDistanciaEdificio(getRandomInt(1, 15), distancia1.crear())
        def lista2 = crearListaPorDistanciaEdificio(getRandomInt(1, 15), distancia2.crear())
        def lista3 = crearListaPorDistanciaEdificio(getRandomInt(1, 15), distancia3.crear())
        mapa.put(distancia1.params.codigo, lista1)
        mapa.put(distancia2.params.codigo, lista2)
        mapa.put(distancia3.params.codigo, lista3)

        expect:
        service.obtenerMapaAsignacionPorDistanciaEdificio(lista1 + lista2 + lista3) == mapa
    }

    private List<AsignacionPuesto> crearListaPorDistanciaEdificio(cantidad, distancia) {
        def lista = []
        cantidad.times {
            def asignacionPuestoBuilder = new AsignacionPuestoBuilder()
            asignacionPuestoBuilder.puesto.edificio.distancia = distancia
            def asignacion = asignacionPuestoBuilder.crear()
            lista += asignacion
        }
        return lista
    }

}