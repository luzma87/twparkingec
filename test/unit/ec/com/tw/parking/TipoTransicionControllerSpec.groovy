package ec.com.tw.parking


import ec.com.tw.parking.builders.TipoTransicionBuilder
import grails.test.mixin.*
import spock.lang.*

@TestFor(TipoTransicionController)
@Mock([TipoTransicion, MensajesBuilderTagLib])
class TipoTransicionControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
    }

    void "Debe redireccionar a list cuando se ejecuta index"() {
        when:
        controller.index()
        then:
        response.redirectedUrl == "/tipoTransicion/list"
    }

    void "Debe obtener la lista de tipoTransicions y su numero"() {
        setup:
        tipoTransicionInstance.save()

        expect:
        controller.list() == [tipoTransicionInstanceList : [tipoTransicionInstance],
                              tipoTransicionInstanceCount: 1]

        where:
        tipoTransicionInstance = new TipoTransicionBuilder().crear()
    }

    void "Debe devolver una instancia de tipoTransicion"() {
        when:
        def tipoTransicionInstanceReturned = controller.form_ajax().tipoTransicionInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoTransicion, _) >> tipoTransicionInstance
        tipoTransicionInstanceReturned.properties == tipoTransicionInstance.properties

        where:
        tipoTransicionInstance << [new TipoTransicion(), new TipoTransicionBuilder().crear()]
    }

    void "Debe guardar un tipoTransicion valido"() {
        setup:
        def tipoTransicionInstance = new TipoTransicion()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoTransicion, _) >> tipoTransicionInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as TipoTransicion, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un tipoTransicion no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def tipoTransicionInstance = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoTransicion, _) >> tipoTransicionInstance
        0 * crudHelperServiceMock.guardarObjeto(_ as TipoTransicion, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un tipoTransicion con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def tipoTransicionInstance = new TipoTransicionBuilder().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoTransicion, _) >> tipoTransicionInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as TipoTransicion, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un tipoTransicion valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def tipoTransicionInstance = new TipoTransicionBuilder().crear()
        def random = new Random()
        tipoTransicionInstance.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoTransicion, _) >> tipoTransicionInstance
        1 * crudHelperServiceMock.eliminarObjeto(_ as TipoTransicion) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un tipoTransicion no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoTransicion, _) >> tipoTransicionInstance
        0 * crudHelperServiceMock.eliminarObjeto(_ as TipoTransicion)
        response.text == expectedMessage

        where:
        tipoTransicionInstance << [null, new TipoTransicion()]
    }
}
