package ec.com.tw.parking


import ec.com.tw.parking.builders.PagoBuilder
import grails.test.mixin.*
import spock.lang.*

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomDouble

@TestFor(PagoController)
@Mock([Pago, MensajesBuilderTagLib])
class PagoControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock
    CalculadorCuotaService calculadorCuotaServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock

        calculadorCuotaServiceMock = Mock(CalculadorCuotaService)
        controller.calculadorCuotaService = calculadorCuotaServiceMock
    }

    void "Debe obtener la lista de pagos y su numero"() {
        setup:
        def pagoInstance = PagoBuilder.nuevo().crear()
        def hoy = new Date()
        def anio = hoy.format("yyyy").toInteger()
        def mesActual = hoy.format("MM").toInteger()
        def cuota = getRandomDouble(1, 100)
        def anios = []
        for (int i = anio; i >= 2016; i--) {
            anios += i
        }

        when:
        pagoInstance.save()
        def listReturns = controller.list()

        then:
        1 * calculadorCuotaServiceMock.calcularCuota() >> cuota
        listReturns.anio == anio
        listReturns.pagos == [:]
        listReturns.anios == anios
        listReturns.mesActual == mesActual
        listReturns.cuota.toDouble() == cuota.toDouble()
    }

    void "Debe devolver una instancia de pago"() {
        setup:
        def cuota = getRandomDouble(1, 100)

        when:
        def pagoInstanceReturned = controller.form_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Pago, _) >> pagoInstance
        1 * calculadorCuotaServiceMock.calcularCuota() >> cuota
        pagoInstanceReturned.pagoInstance == pagoInstance
        pagoInstanceReturned.cuota == cuota

        where:
        pagoInstance << [new Pago(), PagoBuilder.nuevo().crear()]
    }

    void "Debe guardar un pago valido"() {
        setup:
        def pagoInstance = new Pago()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Pago, _) >> pagoInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as Pago, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un pago no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def pagoInstance = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Pago, _) >> pagoInstance
        0 * crudHelperServiceMock.guardarObjeto(_ as Pago, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un pago con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def pagoInstance = PagoBuilder.nuevo().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Pago, _) >> pagoInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as Pago, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un pago valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def pagoInstance = PagoBuilder.nuevo().crear()
        def random = new Random()
        pagoInstance.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Pago, _) >> pagoInstance
        1 * crudHelperServiceMock.eliminarObjeto(_ as Pago) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un pago no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Pago, _) >> pagoInstance
        0 * crudHelperServiceMock.eliminarObjeto(_ as Pago)
        response.text == expectedMessage

        where:
        pagoInstance << [null, new Pago()]
    }
}
