package ec.com.tw.parking

import ec.com.tw.parking.builders.TipoTransicionBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

import static RandomUtilsHelpers.getRandomInt

@TestFor(TipoTransicion)
class TipoTransicionSpec extends Specification {
    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def tipoTransicion = TipoTransicionBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        tipoTransicion.validate()
        !tipoTransicion.hasErrors()
    }

    @Unroll
    void "Debe #campo no ser nulo"() {
        setup:
        def tipoTransicion = TipoTransicionBuilder.nuevo().con { d -> d[campo] = null }.crear()

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors[campo]?.code == 'nullable'

        where:
        campo << ["nombre", "distanciaOrigen", "distanciaDestino", "prioridad"]
    }

    @Unroll
    void "Debe #campo no ser blanco"() {
        setup:
        def tipoTransicion = TipoTransicionBuilder.nuevo().crear()
        tipoTransicion[campo] = ''

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors[campo]?.code == 'blank'

        where:
        campo << ["nombre"]
    }

    @Unroll
    void "Debe #campo tener mas o igual #minSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, minSize, false)
        def tipoTransicion = TipoTransicionBuilder.nuevo().con { d -> d[campo] = valor }.crear()

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors[campo]?.code == 'minSize.notmet'

        where:
        campo    | minSize
        "nombre" | 3
    }

    @Unroll
    void "Debe #campo tener menos o igual que #maxSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(maxSize + 1, 100, false)
        def tipoTransicion = TipoTransicionBuilder.nuevo().con { d -> d[campo] = valor }.crear()

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors[campo]?.code == 'maxSize.exceeded'

        where:
        campo    | maxSize
        "nombre" | 30
    }

    void "Debe la prioridad ser positiva y mayor que 0"() {
        setup:
        def negativo = getRandomInt(0, 10) * -1
        def tipoTransicion = TipoTransicionBuilder.nuevo().con { d -> d.prioridad = negativo }.crear()

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors['prioridad']?.code == "min.notmet"
    }

    void "Debe toString devolver el nombre"() {
        setup:
        def tipoTransicion = TipoTransicionBuilder.nuevo().crear()

        expect:
        tipoTransicion.toString() == tipoTransicion.nombre
    }
}
