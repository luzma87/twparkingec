package ec.com.tw.parking

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

    void "Debe retornar las asignaciones de puesto según tipo de preferencia y edificio"() {
        setup:
        def edificio
        def asignacionesEsperadas = []

        def tipoPreferencia = TipoPreferenciaBuilder.nuevo().guardar()
        def distanciaEdificio = DistanciaEdificioBuilder.nuevo().guardar()
        edificio = EdificioBuilder.nuevo().con { ed -> ed.distancia = distanciaEdificio }.guardar()
        def puestos = []
        10.times { puestos += PuestoBuilder.nuevo().con { pb -> pb.edificio = edificio }.guardar() }
        10.times { puestos += PuestoBuilder.nuevo().guardar() }
        def autos = []
        5.times { autos += AutoBuilder.nuevo().con { ab -> ab.usuario.preferencia = tipoPreferencia }.guardar() }
        15.times { autos += AutoBuilder.nuevo().guardar() }
        def guardoAsignaciones = true
        puestos.eachWithIndex { puesto, index ->
            def asignacion = new AsignacionPuesto()
            asignacion.puesto = puesto
            asignacion.auto = autos[index]
            asignacion.fechaAsignacion = new Date() - getRandomInt(1, 15)
            if (asignacion.save() && index < 5) {
                asignacionesEsperadas += asignacion
            } else {
                guardoAsignaciones = false
            }
        }

        when:
        List<AsignacionPuesto> respuesta = AsignacionPuesto.obtenerPorPreferenciaYedificio(tipoPreferencia, edificio)

        then:
        respuesta == asignacionesEsperadas
    }
}
