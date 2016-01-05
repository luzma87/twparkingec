package ec.com.tw.parking



import ec.com.tw.parking.builders.DistanciaEdificioBuilder
import grails.test.mixin.*
import spock.lang.*

@TestFor(DistanciaEdificioController)
@Mock([DistanciaEdificio, MensajesBuilderTagLib])
class DistanciaEdificioControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
    }

    void "Debe redireccionar a list cuando se ejecuta index"() {
        when:
        controller.index()
        then:
        response.redirectedUrl == "/distanciaEdificio/list"
    }

    void "Debe obtener la lista de distanciaEdificios y su numero"() {
        setup:
        distanciaEdificioInstance.save()

        expect:
        controller.list() == [distanciaEdificioInstanceList: [distanciaEdificioInstance],
                              distanciaEdificioInstanceCount: 1]

        where:
        distanciaEdificioInstance = new DistanciaEdificioBuilder().crear()
    }

    void "Debe devolver una instancia de distanciaEdificio"() {
        when:
        def distanciaEdificioInstanceReturned = controller.form_ajax().distanciaEdificioInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(DistanciaEdificio, _) >> distanciaEdificioInstance
        distanciaEdificioInstanceReturned.properties == distanciaEdificioInstance.properties

        where:
        distanciaEdificioInstance << [new DistanciaEdificio(), new DistanciaEdificioBuilder().crear()]
    }

    void "Debe guardar un distanciaEdificio valido"() {
        setup:
        def distanciaEdificioInstance = new DistanciaEdificio()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(DistanciaEdificio, _) >> distanciaEdificioInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as DistanciaEdificio, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un distanciaEdificio no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def distanciaEdificioInstance = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(DistanciaEdificio, _) >> distanciaEdificioInstance
        0 * crudHelperServiceMock.guardarObjeto(_ as DistanciaEdificio, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un distanciaEdificio con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def distanciaEdificioInstance = new DistanciaEdificioBuilder().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(DistanciaEdificio, _) >> distanciaEdificioInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as DistanciaEdificio, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un distanciaEdificio valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def distanciaEdificioInstance = new DistanciaEdificioBuilder().crear()
        def random = new Random()
        distanciaEdificioInstance.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(DistanciaEdificio, _) >> distanciaEdificioInstance
        1 * crudHelperServiceMock.eliminarObjeto(_ as DistanciaEdificio) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un distanciaEdificio no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(DistanciaEdificio, _) >> distanciaEdificioInstance
        0 * crudHelperServiceMock.eliminarObjeto(_ as DistanciaEdificio)
        response.text == expectedMessage

        where:
        distanciaEdificioInstance << [null, new DistanciaEdificio()]
    }
}
