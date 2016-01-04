package ec.com.tw.parking

import ec.com.tw.parking.builders.PuestoBuilder
import ec.com.tw.parking.helpers.RandomUtilsHelpers
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(Puesto)
@Mock([Edificio])
class PuestoSpec extends Specification {
    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def puesto = new PuestoBuilder().crear()

        then: 'la validacion debe pasar'
        puesto.validate()
        !puesto.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def puestoBuilder = new PuestoBuilder()
        puestoBuilder[campo] = null
        def puesto = puestoBuilder.crear()

        expect:
        !puesto.validate()
        puesto.hasErrors()
        puesto.errors[campo]?.code == 'nullable'

        where:
        campo << ["tamanio", "numero", "edificio"]
    }

    void "Debe ser no blanco"(campo) {
        setup:
        def puestoBuilder = new PuestoBuilder()
        def puesto = puestoBuilder.crear()
        puesto[campo] = ""

        expect:
        !puesto.validate()
        puesto.hasErrors()
        puesto.errors[campo]?.code == 'blank'

        where:
        campo << ["tamanio", "numero"]
    }


    void "Debe tener menos o igual que el maximo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(campo.maxSize + 1, 100, false)
        def puestoBuilder = new PuestoBuilder()
        puestoBuilder[campo.nombre] = valor
        def puesto = puestoBuilder.crear()

        expect:
        !puesto.validate()
        puesto.hasErrors()
        puesto.errors[campo.nombre]?.code == 'maxSize.exceeded'

        where:
        campo << [
            [nombre: "numero", maxSize: 6]
        ]
    }

    void "Debe el tamanio ser P o G"() {
        setup:
        def regex = /[A-FH-OQ-Z]/
        def tamanio = RandomUtilsHelpers.getRandomString(regex, 1, 1)
        def puestoBuilder = new PuestoBuilder()
        puestoBuilder.tamanio = tamanio
        def puesto = puestoBuilder.crear()

        expect:
        !puesto.validate()
        puesto.hasErrors()
        puesto.errors["tamanio"]?.code == 'not.inList'
    }

    void "Debe toString devolver el nombre del edificio y el numero"() {
        setup:
        def puesto = new PuestoBuilder().crear()

        expect:
        puesto.toString() == puesto.edificio.nombre + " " + puesto.numero
    }
}
