package ec.com.tw.parking

import ec.com.tw.parking.builders.PuestoBuilder
import grails.test.mixin.*
import spock.lang.*

@TestFor(PuestoController)
@Mock([Puesto, MensajesBuilderTagLib])
class PuestoControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
    }

    void "Debe redireccionar a list cuando se ejecuta index"() {
        when:
        controller.index()
        then:
        response.redirectedUrl == "/puesto/list"
    }

    void "Debe obtener la lista de puestos y su numero"() {
        setup:
        puestoInstance.save()

        expect:
        controller.list() == [puestoInstanceList : [puestoInstance],
                              puestoInstanceCount: 1]

        where:
        puestoInstance = new PuestoBuilder().crear()
    }

    void "Debe devolver una instancia de puesto"() {
        when:
        def puestoInstanceReturned = controller.form_ajax().puestoInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Puesto, _) >> puestoInstance
        puestoInstanceReturned.properties == puestoInstance.properties

        where:
        puestoInstance << [new Puesto(), new PuestoBuilder().crear()]
    }

    void "Debe guardar un puesto valido"() {
        setup:
        def puestoInstance = new Puesto()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Puesto, _) >> puestoInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as Puesto, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un puesto no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def puestoInstance = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Puesto, _) >> puestoInstance
        0 * crudHelperServiceMock.guardarObjeto(_ as Puesto, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un puesto con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def puestoInstance = new PuestoBuilder().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Puesto, _) >> puestoInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as Puesto, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un puesto valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def puestoInstance = new PuestoBuilder().crear()
        def random = new Random()
        puestoInstance.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Puesto, _) >> puestoInstance
        1 * crudHelperServiceMock.eliminarObjeto(_ as Puesto) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un puesto no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Puesto, _) >> puestoInstance
        0 * crudHelperServiceMock.eliminarObjeto(_ as Puesto)
        response.text == expectedMessage

        where:
        puestoInstance << [null, new Puesto()]
    }
}
