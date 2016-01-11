package ec.com.tw.parking

import ec.com.tw.parking.builders.TipoTransicionBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification

import static RandomUtilsHelpers.getRandomInt

@TestFor(TipoTransicion)
class TipoTransicionSpec extends Specification {
    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def tipoTransicion = new TipoTransicionBuilder().crear()

        then: 'la validacion debe pasar'
        tipoTransicion.validate()
        !tipoTransicion.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def tipoTransicionBuilder = new TipoTransicionBuilder()
        tipoTransicionBuilder[campo] = null
        def tipoTransicion = tipoTransicionBuilder.crear()

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors[campo]?.code == 'nullable'

        where:
        campo << ["nombre", "distanciaOrigen", "distanciaDestino", "prioridad"]
    }

    void "Debe ser no blanco"(campo) {
        setup:
        def tipoTransicionBuilder = new TipoTransicionBuilder()
        def tipoTransicion = tipoTransicionBuilder.crear()
        tipoTransicion[campo] = ""

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors[campo]?.code == 'blank'

        where:
        campo << ["nombre"]
    }

    void "Debe tener mas o igual del minimo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, campo.minSize, false)
        def tipoTransicionBuilder = new TipoTransicionBuilder()
        tipoTransicionBuilder[campo.nombre] = valor
        def tipoTransicion = tipoTransicionBuilder.crear()

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors[campo.nombre]?.code == 'minSize.notmet'

        where:
        campo << [
            [nombre: "nombre", minSize: 3]
        ]
    }

    void "Debe tener menos o igual que el maximo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(campo.maxSize + 1, 100, false)
        def tipoTransicionBuilder = new TipoTransicionBuilder()
        tipoTransicionBuilder[campo.nombre] = valor
        def tipoTransicion = tipoTransicionBuilder.crear()

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors[campo.nombre]?.code == 'maxSize.exceeded'

        where:
        campo << [
            [nombre: "nombre", maxSize: 30]
        ]
    }

    void "Debe la prioridad ser positiva y mayor que 0"() {
        setup:
        def negativo = getRandomInt(0, 10) * -1
        def tipoTransicionBuilder = new TipoTransicionBuilder()
        tipoTransicionBuilder.prioridad = negativo
        def tipoTransicion = tipoTransicionBuilder.crear()

        expect:
        !tipoTransicion.validate()
        tipoTransicion.hasErrors()
        tipoTransicion.errors['prioridad']?.code == "min.notmet"
    }

    void "Debe toString devolver el nombre"() {
        setup:
        def tipoTransicion = new TipoTransicionBuilder().crear()

        expect:
        tipoTransicion.toString() == tipoTransicion.nombre
    }
}
