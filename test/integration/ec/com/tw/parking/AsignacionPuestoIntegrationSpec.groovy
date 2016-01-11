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
        edificio = EdificioBuilder.nuevo().con {
            ed -> ed.distancia = distanciaEdificio
        }.guardar()
        def guardoPuestos = true
        def puestos = PuestoBuilder.nuevo().crearLista(20)
        puestos.eachWithIndex { puesto, index ->
            if (index < 10) {
                puesto.edificio = edificio
            } else {
                puesto.edificio.distancia.save()
                puesto.edificio.save()
            }
            if (!puesto.save()) {
                guardoPuestos = false
            }
        }
        if (guardoPuestos) {
            def guardoUsuariosYautos = true
            def autos = AutoBuilder.crearLista(20)
            autos.eachWithIndex { auto, index ->
                def usuario = auto.usuario
                if (index < 5) {
                    usuario.preferencia = tipoPreferencia
                } else {
                    usuario.preferencia.save()
                }
                if (!usuario.save()) {
                    guardoUsuariosYautos = false
                }
                if (!auto.save()) {
                    guardoUsuariosYautos = false
                }
            }
            if (guardoUsuariosYautos) {
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
            }
        }

        when:
        List<AsignacionPuesto> respuesta = AsignacionPuesto.obtenerPorPreferenciaYedificio(tipoPreferencia, edificio)

        then:
        respuesta == asignacionesEsperadas
    }
}
