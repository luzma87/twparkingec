package ec.com.tw.parking

import ec.com.tw.parking.builders.AutoBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Auto)
@Mock([Usuario])
class AutoSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def auto = AutoBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        auto.validate()
        !auto.hasErrors()
    }

    @Unroll
    void "Debe #campo ser no nulo"() {
        setup:
        def auto = AutoBuilder.nuevo().con { a -> a[campo] = null }.crear()

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo]?.code == 'nullable'

        where:
        campo << ["usuario", "marca", "modelo", "placa", "esDefault"]
    }

    @Unroll
    void "Debe #campo ser no blanco"() {
        setup:
        def auto = AutoBuilder.nuevo().crear()
        auto[campo] = ""

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo]?.code == 'blank'

        where:
        campo << ["marca", "modelo", "placa"]
    }

    void "Debe tener mas o igual del minimo de caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, campo.minSize, false)
        def auto = AutoBuilder.nuevo().con { a -> a[campo.nombre] = valor }.crear()

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo.nombre]?.code == 'minSize.notmet'

        where:
        campo << [
            [nombre: "marca", minSize: 2],
            [nombre: "modelo", minSize: 2],
            [nombre: "placa", minSize: 7]
        ]
    }

    void "Debe tener menos o igual que el maximo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(campo.maxSize + 1, 100, false)
        def auto = AutoBuilder.nuevo().con { a -> a[campo.nombre] = valor }.crear()

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo.nombre]?.code == 'maxSize.exceeded'

        where:
        campo << [
            [nombre: "marca", maxSize: 20],
            [nombre: "modelo", maxSize: 25],
            [nombre: "placa", maxSize: 8]
        ]
    }

    void "Debe toString devolver el usuario, la marca, el modelo y si es default"() {
        setup:
        def auto = AutoBuilder.nuevo().crear()

        expect:
        auto.toString() == auto.usuario.toString() + ": " + auto.marca + " " + auto.modelo + (auto.esDefault ? "" : "*")
    }
}
