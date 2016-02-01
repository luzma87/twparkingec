package ec.com.tw.parking

import ec.com.tw.parking.builders.PagoBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomDouble


@TestFor(Pago)
@Mock([Usuario])
class PagoSpec extends Specification {
    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def pago = PagoBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        pago.validate()
        !pago.hasErrors()
    }

    @Unroll
    void "Debe #campo no ser nulo"() {
        setup:
        def pago = PagoBuilder.nuevo().con { p -> p[campo] = null }.crear()

        expect:
        !pago.validate()
        pago.hasErrors()
        pago.errors[campo]?.code == 'nullable'

        where:
        campo << ["usuario", "fechaPago", "mes", "anio", "monto"]
    }

    void "Debe el monto ser positivo"() {
        setup:
        def negativo = getRandomDouble(100) * -1
        def pago = PagoBuilder.nuevo().con { p -> p.monto = negativo }.crear()

        expect:
        !pago.validate()
        pago.hasErrors()
        pago.errors['monto']?.code == "min.notmet"
    }
}
