package ec.com.tw.parking

import ec.com.tw.parking.builders.AutoBuilder
import grails.test.mixin.*
import spock.lang.*

@TestFor(AutoController)
@Mock([Auto, MensajesBuilderTagLib])
class AutoControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
    }

    void "Debe devolver una instancia de auto"() {
        when:
        def autoInstanceReturned = controller.form_ajax().autoInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Auto, _) >> autoInstance
        autoInstanceReturned.properties == autoInstance.properties

        where:
        autoInstance << [new Auto(), new AutoBuilder().crear()]
    }

    void "Debe guardar un auto valido"() {
        setup:
        def autoInstance = new Auto()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Auto, _) >> autoInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as Auto, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un auto no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def autoInstance = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Auto, _) >> autoInstance
        0 * crudHelperServiceMock.guardarObjeto(_ as Auto, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un auto con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def autoInstance = new AutoBuilder().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Auto, _) >> autoInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as Auto, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un auto valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def autoInstance = new AutoBuilder().crear()
        def random = new Random()
        autoInstance.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Auto, _) >> autoInstance
        1 * crudHelperServiceMock.eliminarObjeto(_ as Auto) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un auto no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Auto, _) >> autoInstance
        0 * crudHelperServiceMock.eliminarObjeto(_ as Auto)
        response.text == expectedMessage

        where:
        autoInstance << [null, new Auto()]
    }
}
