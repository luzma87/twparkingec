package ec.com.tw.parking

import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Edificio)
class EdificioSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def edificio = EdificioBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        edificio.validate()
        !edificio.hasErrors()
    }

    @Unroll
    void "Debe #campo ser no nulo"() {
        setup:
        def edificio = EdificioBuilder.nuevo().con { e -> e[campo] = null }.crear()

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo]?.code == 'nullable'

        where:
        campo << ["nombre", "distancia", "esAmpliable"]
    }

    @Unroll
    void "Debe #campo poder ser nulo"() {
        setup:
        def edificio = EdificioBuilder.nuevo().con { e -> e[campo] = null }.crear()

        expect:
        edificio.validate()
        !edificio.hasErrors()

        where:
        campo << ["datosPago", "observaciones"]
    }

    @Unroll
    void "Debe #campo ser no blanco"() {
        setup:
        def edificio = EdificioBuilder.nuevo().crear()
        edificio[campo] = ""

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo]?.code == 'blank'

        where:
        campo << ["nombre", "datosPago"]
    }

    @Unroll
    void "Debe #campo tener mas o igual que #minSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, minSize, false)
        def edificio = EdificioBuilder.nuevo().con { e -> e[campo] = valor }.crear()

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo]?.code == 'minSize.notmet'

        where:
        campo    | minSize
        "nombre" | 2
    }

    @Unroll
    void "Debe #campo tener menos o igual que #maxSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(maxSize + 1, 300, false)
        def edificio = EdificioBuilder.nuevo().con { e -> e[campo] = valor }.crear()

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo]?.code == 'maxSize.exceeded'

        where:
        campo           | maxSize
        "nombre"        | 50
        "observaciones" | 150
    }

    void "Debe tener varios puestos"() {
        setup:
        def puesto = PuestoBuilder.nuevo().crear()
        puesto.save()
        def edificio = EdificioBuilder.nuevo().crear()
        edificio.save()

        expect:
        edificio.addToPuestos(puesto)
        edificio.puestos.size() == 1
    }

    void "Debe toString devolver el nombre"() {
        setup:
        def edificio = EdificioBuilder.nuevo().crear()

        expect:
        edificio.toString() == edificio.nombre
    }
}
