package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(AsignacionPuesto)
@Mock([Auto, Puesto, TipoPreferencia, Edificio, DistanciaEdificio])
class AsignacionPuestoSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def asignacionPuesto = AsignacionPuestoBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        asignacionPuesto.validate()
        !asignacionPuesto.hasErrors()
    }

    @Unroll
    void "Debe #campo ser no nulo"() {
        setup:
        def asignacionPuesto = AsignacionPuestoBuilder.nuevo().con { a -> a[campo] = null }.crear()

        expect:
        !asignacionPuesto.validate()
        asignacionPuesto.hasErrors()
        asignacionPuesto.errors[campo]?.code == 'nullable'

        where:
        campo << ["auto", "puesto", "fechaAsignacion"]
    }

    @Unroll
    void "Debe #campo poder ser nulo"(campo) {
        setup:
        def asignacionPuesto = AsignacionPuestoBuilder.nuevo().con { a -> a[campo] = null }.crear()

        expect:
        asignacionPuesto.validate()
        !asignacionPuesto.hasErrors()

        where:
        campo << ["fechaLiberacion"]
    }

    void "Debe toString mostrar la informacion necesaria"() {
        setup:
        def asignacionPuesto = AsignacionPuestoBuilder.nuevo().crear()

        expect:
        def persona = asignacionPuesto.auto.usuario.nombre
        def placa = asignacionPuesto.auto.placa
        def edificio = asignacionPuesto.puesto.edificio.nombre
        def numero = asignacionPuesto.puesto.numero
        asignacionPuesto.toString() == persona + " (" + placa + ") → " + edificio + " #" + numero
    }
}
