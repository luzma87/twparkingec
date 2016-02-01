package ec.com.tw.parking



import ec.com.tw.parking.builders.PagoBuilder
import grails.test.mixin.*
import spock.lang.*

@TestFor(PagoController)
@Mock([Pago, MensajesBuilderTagLib])
class PagoControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
    }

    void "Debe obtener la lista de pagos y su numero"() {
        setup:
        pagoInstance.save()

        expect:
        controller.list() == [pagoInstanceList: [pagoInstance],
                              pagoInstanceCount: 1]

        where:
        pagoInstance = PagoBuilder.nuevo().crear()
    }

    void "Debe devolver una instancia de pago"() {
        when:
        def pagoInstanceReturned = controller.form_ajax().pagoInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Pago, _) >> pagoInstance
        pagoInstanceReturned.properties == pagoInstance.properties

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
