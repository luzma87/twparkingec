package ec.com.tw.parking

import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.helpers.RandomUtilsHelpers
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.support.GrailsUnitTestMixin} for usage instructions
 */
@TestFor(Edificio)
class EdificioSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def edificio = new EdificioBuilder().crear()

        then: 'la validacion debe pasar'
        edificio.validate()
        !edificio.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def edificioBuilder = new EdificioBuilder()
        edificioBuilder[campo] = null
        def edificio = edificioBuilder.crear()

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo]?.code == 'nullable'

        where:
        campo << ["nombre"]
    }

    void "Debe ser no blanco"(campo) {
        setup:
        def edificioBuilder = new EdificioBuilder()
        def edificio = edificioBuilder.crear()
        edificio[campo] = ""

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo]?.code == 'blank'

        where:
        campo << ["nombre"]
    }

    void "Debe tener mas o igual del minimo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, campo.minSize, false)
        def edificioBuilder = new EdificioBuilder()
        edificioBuilder[campo.nombre] = valor
        def edificio = edificioBuilder.crear()

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo.nombre]?.code == 'minSize.notmet'

        where:
        campo << [
            [nombre: "nombre", minSize: 2]
        ]
    }

    void "Debe tener menos o igual que el maximo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(campo.maxSize + 1, 100, false)
        def edificioBuilder = new EdificioBuilder()
        edificioBuilder[campo.nombre] = valor
        def edificio = edificioBuilder.crear()

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo.nombre]?.code == 'maxSize.exceeded'

        where:
        campo << [
            [nombre: "nombre", maxSize: 50]
        ]
    }

    void "Debe toString devolver el nombre"() {
        setup:
        def edificio = new EdificioBuilder().crear()

        expect:
        edificio.toString() == edificio.nombre
    }
}
