package ec.com.tw.parking

import ec.com.tw.parking.builders.PuestoBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

import static RandomUtilsHelpers.getRandomDouble

@TestFor(Puesto)
@Mock([Edificio])
class PuestoSpec extends Specification {
    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def puesto = PuestoBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        puesto.validate()
        !puesto.hasErrors()
    }

    @Unroll
    void "Debe #campo no ser nulo"() {
        setup:
        def puesto = PuestoBuilder.nuevo().con { p -> p[campo] = null }.crear()

        expect:
        !puesto.validate()
        puesto.hasErrors()
        puesto.errors[campo]?.code == 'nullable'

        where:
        campo << ["tamanio", "numero", "edificio", "precio"]
    }

    @Unroll
    void "Debe #campo no ser blanco"() {
        setup:
        def puesto = PuestoBuilder.nuevo().crear()
        puesto[campo] = ""

        expect:
        !puesto.validate()
        puesto.hasErrors()
        puesto.errors[campo]?.code == 'blank'

        where:
        campo << ["numero"]
    }

    @Unroll
    void "Debe #campo tener menos o igual que #maxSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(maxSize + 1, 100, false)
        def puesto = PuestoBuilder.nuevo().con { p -> p[campo] = valor }.crear()

        expect:
        !puesto.validate()
        puesto.hasErrors()
        puesto.errors[campo]?.code == 'maxSize.exceeded'

        where:
        campo    | maxSize
        "numero" | 6
    }

    void "Debe el precio ser positivo"() {
        setup:
        def negativo = getRandomDouble(100) * -1
        def puesto = PuestoBuilder.nuevo().con { p -> p.precio = negativo }.crear()

        expect:
        !puesto.validate()
        puesto.hasErrors()
        puesto.errors['precio']?.code == "min.notmet"
    }

    void "Debe toString devolver el nombre del edificio y el numero"() {
        setup:
        def puesto = PuestoBuilder.nuevo().crear()

        expect:
        puesto.toString() == puesto.edificio.nombre + " #" + puesto.numero
    }
}
