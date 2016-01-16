package ec.com.tw.parking

import ec.com.tw.parking.builders.HistoricoAsignacionPuestoBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(HistoricoAsignacionPuesto)
@Mock([Auto, Puesto, TipoPreferencia, Edificio, DistanciaEdificio])
class HistoricoAsignacionPuestoSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def historicoAsignacionPuesto = HistoricoAsignacionPuestoBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        historicoAsignacionPuesto.validate()
        !historicoAsignacionPuesto.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def historicoAsignacionPuesto = HistoricoAsignacionPuestoBuilder.nuevo()
            .con { h -> h[campo] = null }.crear()

        expect:
        !historicoAsignacionPuesto.validate()
        historicoAsignacionPuesto.hasErrors()
        historicoAsignacionPuesto.errors[campo]?.code == 'nullable'

        where:
        campo << ["auto", "puesto", "fechaAsignacion"]
    }

    void "Debe poder ser nulo"(campo) {
        setup:
        def historicoAsignacionPuesto = HistoricoAsignacionPuestoBuilder.nuevo()
            .con { h -> h[campo] = null }.crear()

        expect:
        historicoAsignacionPuesto.validate()
        !historicoAsignacionPuesto.hasErrors()

        where:
        campo << ["fechaLiberacion"]
    }

    void "Debe toString mostrar la informacion necesaria: fecha liberacion nula"() {
        setup:
        def historicoAsignacionPuesto = HistoricoAsignacionPuestoBuilder.nuevo().crear()

        expect:
        def persona = historicoAsignacionPuesto.auto.usuario.nombre
        def placa = historicoAsignacionPuesto.auto.placa
        def edificio = historicoAsignacionPuesto.puesto.edificio.nombre
        def numero = historicoAsignacionPuesto.puesto.numero
        def asignacion = historicoAsignacionPuesto.fechaAsignacion.format("dd-MM-yyyy")
        historicoAsignacionPuesto.toString() == persona + " (" + placa + ") → " + edificio + " " + numero +
            " desde " + asignacion
    }

    void "Debe toString mostrar la informacion necesaria: fecha liberacion no nula"() {
        setup:
        def historicoAsignacionPuesto = HistoricoAsignacionPuestoBuilder.nuevo()
            .con { h -> h.fechaLiberacion = new Date() }.crear()

        expect:
        def persona = historicoAsignacionPuesto.auto.usuario.nombre
        def placa = historicoAsignacionPuesto.auto.placa
        def edificio = historicoAsignacionPuesto.puesto.edificio.nombre
        def numero = historicoAsignacionPuesto.puesto.numero
        def asignacion = historicoAsignacionPuesto.fechaAsignacion.format("dd-MM-yyyy")
        def liberacion = historicoAsignacionPuesto.fechaLiberacion.format("dd-MM-yyyy")
        historicoAsignacionPuesto.toString() == persona + " (" + placa + ") → " + edificio + " " + numero +
            " desde " + asignacion + " hasta " + liberacion
    }


}
