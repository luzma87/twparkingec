package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.DistanciaEdificioBuilder
import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import ec.com.tw.parking.builders.TipoPreferenciaBuilder
import grails.test.spock.IntegrationSpec

import static RandomUtilsHelpers.getRandomInt

class AsignacionPuestoIntegrationSpec extends IntegrationSpec {

    def setup() {
    }

    def cleanup() {
    }

    void "Debe retornar las asignaciones de puesto segun tipo de preferencia y edificio"() {
        setup:
        def edificio
        def asignacionesEsperadas = []

        def tipoPreferencia = TipoPreferenciaBuilder.nuevo().guardar()
        def distanciaEdificio = DistanciaEdificioBuilder.nuevo().guardar()
        edificio = EdificioBuilder.nuevo().con { ed -> ed.distancia = distanciaEdificio }.guardar()
        def puestos = [], autos = []
        10.times { puestos += PuestoBuilder.nuevo().con { pb -> pb.edificio = edificio }.guardar() }
        10.times { puestos += PuestoBuilder.nuevo().guardar() }
        5.times { autos += AutoBuilder.nuevo().con { ab -> ab.usuario.preferencia = tipoPreferencia }.guardar() }
        15.times { autos += AutoBuilder.nuevo().guardar() }
        puestos.eachWithIndex { puesto, index ->
            def asignacion = AsignacionPuestoBuilder.nuevo()
                .con { a -> a.puesto = puesto }
                .con { a -> a.auto = autos[index] }
                .con { a -> a.fechaAsignacion = new Date() - getRandomInt(1, 15)
            }.crear()
            if (asignacion.save() && index < 5) {
                asignacionesEsperadas += asignacion
            }
        }

        when:
        List<AsignacionPuesto> respuesta = AsignacionPuesto.obtenerOcupadosPorPreferenciaYedificio(tipoPreferencia, edificio)

        then:
        respuesta == asignacionesEsperadas
    }

    def "Debe retornar las asignaciones segun la distancia"() {
        setup:
        def distancia = DistanciaEdificioBuilder.nuevo().guardar()
        def edificio = EdificioBuilder.nuevo().con { ed -> ed.distancia = distancia }.guardar()
        def puestosDistanciaCorrecta = [], otrosPuestos = [], autos = []
        5.times { puestosDistanciaCorrecta += PuestoBuilder.nuevo().con { pb -> pb.edificio = edificio }.guardar() }
        5.times { otrosPuestos += PuestoBuilder.nuevo().guardar() }
        10.times { autos += AutoBuilder.nuevo().guardar() }
        List<AsignacionPuesto> asignacioneEsperadas = []
        5.times {
            asignacioneEsperadas += AsignacionPuestoBuilder.nuevo()
                .con { a -> a.auto = autos[it] }
                .con { a -> a.puesto = puestosDistanciaCorrecta[it] }.guardar()
            AsignacionPuestoBuilder.nuevo()
                .con { a -> a.auto = autos[it + 5] }
                .con { a -> a.puesto = otrosPuestos[it] }.guardar()
        }

        when:
        List<AsignacionPuesto> asignacionesObtenidas = AsignacionPuesto.obtenerPorDistancia(distancia)

        then:
        asignacionesObtenidas == asignacioneEsperadas
    }
}
