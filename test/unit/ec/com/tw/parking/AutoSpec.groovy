package ec.com.tw.parking

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Auto)
@Mock([Usuario])
class AutoSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def auto = TestsHelpers.generaAutoValido()

        then: 'la validacion debe pasar'
        auto.validate()
        !auto.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def auto = TestsHelpers.generaAutoConCampo(campo, null)

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo]?.code == 'nullable'

        where:
        campo << ["usuario", "marca", "modelo", "placa", "tamanio"]
    }

    void "Debe ser no blanco"(campo) {
        setup:
        def auto = TestsHelpers.generaAutoConCampo(campo, "")

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo]?.code == 'blank'

        where:
        campo << ["marca", "modelo", "placa", "tamanio"]
    }

    void "Debe tener mas o igual del minimo de caracteres"(campo) {
        setup:
        def valor = TestsHelpers.getRandomString(1, campo.minSize, false)
        def auto = TestsHelpers.generaAutoConCampo(campo.nombre, valor)

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo.nombre]?.code == 'minSize.notmet'

        where:
        campo << [
            [nombre: "marca", minSize: 2],
            [nombre: "modelo", minSize: 2],
            [nombre: "placa", minSize: 8]
        ]
    }

    void "Debe tener menos o igual el maximo de caracteres"(campo) {
        setup:
        def valor = TestsHelpers.getRandomString(campo.maxSize + 1, 100, false)
        def auto = TestsHelpers.generaAutoConCampo(campo.nombre, valor)

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo.nombre]?.code == 'maxSize.exceeded'

        where:
        campo << [
            [nombre: "marca", maxSize: 20],
            [nombre: "modelo", maxSize: 25],
            [nombre: "placa", maxSize: 8],
            [nombre: "tamanio", maxSize: 1]
        ]
    }

    void "Debe el tamanio ser P o G"() {
        setup:
        def regex = /[A-FH-OQ-Z]/
        def tamanio = TestsHelpers.getRandomString(regex, 1, 1)
        def auto = TestsHelpers.generaAutoConCampo('tamanio', tamanio)

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors["tamanio"]?.code == 'not.inList'
    }
}
