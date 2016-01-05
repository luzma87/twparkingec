package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.DistanciaEdificioBuilder
import ec.com.tw.parking.builders.TipoTransicionBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomInt

@TestFor(AsignadorPuestosService)
@Mock([AsignacionPuesto])
class AsignadorPuestosServiceSpec extends Specification {

    List<AsignacionPuesto> listaAsignaciones = []

    def setup() {
        10.times {
            listaAsignaciones.add(new AsignacionPuestoBuilder().crear())
        }
    }

    def "Debe devolver una lista con todas las asignaciones de puesto"() {
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

    def "Debe retornar la asignacion con fecha minima de una lista de asignaciones"() {
        setup:
        def asignacionConFechaMinima = listaAsignaciones.min { it.fechaAsignacion }

        expect:
        service.obtenerAsignacionConFechaMinima(listaAsignaciones) == asignacionConFechaMinima
    }

    def "Debe devolver una lista de tipo transicion"() {
        setup:
        def listaTipoTransiciones = []
        3.times {
            listaTipoTransiciones.add(new TipoTransicionBuilder().crear())
        }

        when:
        GroovyMock(TipoTransicion, global: true)
        TipoTransicion.list() >> listaTipoTransiciones
        def respuesta = service.obtenerTodosTiposTransicion()

        then:
        respuesta == listaTipoTransiciones
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