package ec.com.tw.parking



import ec.com.tw.parking.builders.EdificioBuilder
import grails.test.mixin.*
import spock.lang.*

@TestFor(EdificioController)
@Mock([Edificio, MensajesBuilderTagLib])
class EdificioControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
    }

    void "Debe redireccionar a list cuando se ejecuta index"() {
        when:
        controller.index()
        then:
        response.redirectedUrl == "/edificio/list"
    }

    void "Debe obtener la lista de edificios y su numero"() {
        setup:
        edificioInstance.save()

        expect:
        controller.list() == [edificioInstanceList: [edificioInstance],
                              edificioInstanceCount: 1]

        where:
        edificioInstance = new EdificioBuilder().crear()
    }

    void "Debe devolver una instancia de edificio"() {
        when:
        def edificioInstanceReturned = controller.form_ajax().edificioInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Edificio, _) >> edificioInstance
        edificioInstanceReturned.properties == edificioInstance.properties

        where:
        edificioInstance << [new Edificio(), new EdificioBuilder().crear()]
    }

    void "Debe guardar un edificio valido"() {
        setup:
        def edificioInstance = new Edificio()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Edificio, _) >> edificioInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as Edificio, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un edificio no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def edificioInstance = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Edificio, _) >> edificioInstance
        0 * crudHelperServiceMock.guardarObjeto(_ as Edificio, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un edificio con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def edificioInstance = new EdificioBuilder().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Edificio, _) >> edificioInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as Edificio, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un edificio valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def edificioInstance = new EdificioBuilder().crear()
        def random = new Random()
        edificioInstance.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Edificio, _) >> edificioInstance
        1 * crudHelperServiceMock.eliminarObjeto(_ as Edificio) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un edificio no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Edificio, _) >> edificioInstance
        0 * crudHelperServiceMock.eliminarObjeto(_ as Edificio)
        response.text == expectedMessage

        where:
        edificioInstance << [null, new Edificio()]
    }
}
