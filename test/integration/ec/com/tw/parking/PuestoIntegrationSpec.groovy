package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import ec.com.tw.parking.enums.Tamanio
import grails.test.spock.IntegrationSpec

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomInt

class PuestoIntegrationSpec extends IntegrationSpec {

    def "Debe retornar las asignaciones libres segun el tamanio del puesto"() {
        setup:
        def tamanioCorrecto = Tamanio.GRANDE
        def tamanioIncorrecto = Tamanio.PEQUENIO
        def puestosTamanioCorrecto = [], otrosPuestos = [], autos = []
        10.times {
            puestosTamanioCorrecto += PuestoBuilder.nuevo().con { pb -> pb.tamanio = tamanioCorrecto }.guardar()
        }
        5.times { otrosPuestos += PuestoBuilder.nuevo().con { pb -> pb.tamanio = tamanioIncorrecto }.guardar() }
        15.times { autos += AutoBuilder.nuevo().guardar() }
        List<Puesto> puestosEsperados = []
        5.times {
            puestosEsperados += puestosTamanioCorrecto[it]
            AsignacionPuestoBuilder.nuevo()
                .con { a -> a.auto = autos[it] }
                .con { a -> a.fechaLiberacion = new Date() - getRandomInt(1, 10) }
                .con { a -> a.puesto = puestosTamanioCorrecto[it] }.guardar()
            AsignacionPuestoBuilder.nuevo()
                .con { a -> a.auto = autos[it + 5] }
                .con { a -> a.puesto = puestosTamanioCorrecto[it + 5] }.guardar()
            AsignacionPuestoBuilder.nuevo()
                .con { a -> a.auto = autos[it + 10] }
                .con { a -> a.puesto = otrosPuestos[it] }.guardar()
        }

        expect:
        Puesto.obtenerLibresPorTamanio(tamanioCorrecto) == puestosEsperados
    }
}
