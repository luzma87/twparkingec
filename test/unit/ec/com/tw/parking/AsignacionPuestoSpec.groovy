package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(AsignacionPuesto)
@Mock([Auto, Puesto])
class AsignacionPuestoSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def asignacionPuesto = new AsignacionPuestoBuilder().crear()

        then: 'la validacion debe pasar'
        asignacionPuesto.validate()
        !asignacionPuesto.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def asignacionPuestoBuilder = new AsignacionPuestoBuilder()
        asignacionPuestoBuilder[campo] = null
        def asignacionPuesto = asignacionPuestoBuilder.crear()

        expect:
        !asignacionPuesto.validate()
        asignacionPuesto.hasErrors()
        asignacionPuesto.errors[campo]?.code == 'nullable'

        where:
        campo << ["auto", "puesto", "fechaAsignacion"]
    }

    void "Debe poder ser nulo"(campo) {
        setup:
        def asignacionPuestoBuilder = new AsignacionPuestoBuilder()
        asignacionPuestoBuilder[campo] = null
        def asignacionPuesto = asignacionPuestoBuilder.crear()

        expect:
        asignacionPuesto.validate()
        !asignacionPuesto.hasErrors()

        where:
        campo << ["fechaLiberacion"]
    }

    void "Debe toString mostrar la informacion necesaria"() {
        setup:
        def asignacionPuesto = new AsignacionPuestoBuilder().crear()

        expect:
        def persona = asignacionPuesto.auto.usuario.nombre
        def placa = asignacionPuesto.auto.placa
        def edificio = asignacionPuesto.puesto.edificio.nombre
        def numero = asignacionPuesto.puesto.numero
        asignacionPuesto.toString() == persona + " (" + placa + ") → " + edificio + " " + numero
    }

}
