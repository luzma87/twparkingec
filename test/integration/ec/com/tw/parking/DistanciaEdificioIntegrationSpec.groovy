package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.DistanciaEdificioBuilder
import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import grails.test.spock.IntegrationSpec

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomInt

class DistanciaEdificioIntegrationSpec extends IntegrationSpec {

    def "Debe obtener puestos libres dada una distancia"() {
        setup:
        def distanciaEdificio = DistanciaEdificioBuilder.nuevo().guardar()
        def edificio = EdificioBuilder.nuevo().con { e -> e.distancia = distanciaEdificio }.guardar()
        5.times { PuestoBuilder.nuevo().guardar() }
        def puestosValidos = [], puestosEsperados = []
        10.times { puestosValidos += PuestoBuilder.nuevo().con { p -> p.edificio = edificio }.guardar() }
        puestosValidos.eachWithIndex { puesto, index ->
            def asignacionBuilder = AsignacionPuestoBuilder.nuevo().con { a -> a.puesto = puesto }
            if (index < 5) {
                asignacionBuilder.con { a -> a.fechaLiberacion = new Date() - getRandomInt(10) }
                puestosEsperados += puesto
            }
            asignacionBuilder.guardar()
        }

        expect:
        distanciaEdificio.obtenerPuestosLibres() == puestosEsperados
    }
}
