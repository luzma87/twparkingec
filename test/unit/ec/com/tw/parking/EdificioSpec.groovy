package ec.com.tw.parking

import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(Edificio)
class EdificioSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def edificio = EdificioBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        edificio.validate()
        !edificio.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def edificio = EdificioBuilder.nuevo().con { e -> e[campo] = null }.crear()

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo]?.code == 'nullable'

        where:
        campo << ["nombre", "distancia", "esAmpliable"]
    }

    void "Debe poder ser nulo"(campo) {
        setup:
        def edificio = EdificioBuilder.nuevo().con { e -> e[campo] = null }.crear()

        expect:
        edificio.validate()
        !edificio.hasErrors()

        where:
        campo << ["datosPago", "observaciones"]
    }

    void "Debe ser no blanco"(campo) {
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

    void "Debe tener mas o igual del minimo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, campo.minSize, false)
        def edificio = EdificioBuilder.nuevo().con { e -> e[campo.nombre] = valor }.crear()

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
        def valor = RandomUtilsHelpers.getRandomString(campo.maxSize + 1, 300, false)
        def edificio = EdificioBuilder.nuevo().con { e -> e[campo.nombre] = valor }.crear()

        expect:
        !edificio.validate()
        edificio.hasErrors()
        edificio.errors[campo.nombre]?.code == 'maxSize.exceeded'

        where:
        campo << [
            [nombre: "nombre", maxSize: 50],
            [nombre: "observaciones", maxSize: 150]
        ]
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
