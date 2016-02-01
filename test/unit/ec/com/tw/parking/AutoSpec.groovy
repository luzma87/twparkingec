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

    @Unroll
    void "Debe #campo tener mas o igual que #minSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, minSize, false)
        def auto = AutoBuilder.nuevo().con { a -> a[campo] = valor }.crear()

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo]?.code == 'minSize.notmet'

        where:
        campo    | minSize
        "marca"  | 2
        "modelo" | 2
        "placa"  | 7
    }

    @Unroll
    void "Debe #campo tener menos o igual que #maxSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(maxSize + 1, 100, false)
        def auto = AutoBuilder.nuevo().con { a -> a[campo] = valor }.crear()

        expect:
        !auto.validate()
        auto.hasErrors()
        auto.errors[campo]?.code == 'maxSize.exceeded'

        where:
        campo    | maxSize
        "marca"  | 20
        "modelo" | 25
        "placa"  | 8
    }

    void "Debe toString devolver el usuario, la marca, el modelo, la placa y si es default"() {
        setup:
        def auto = AutoBuilder.nuevo().crear()

        expect:
        auto.toString() == auto.usuario.toString() + ": " + auto.marca + " " + auto.modelo +
            (auto.esDefault ? "" : "*") + " (" + auto.placa + ")"
    }
}
