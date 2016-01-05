package ec.com.tw.parking

import ec.com.tw.parking.builders.TipoPreferenciaBuilder
import grails.test.mixin.*
import spock.lang.*

@TestFor(TipoPreferenciaController)
@Mock([TipoPreferencia, MensajesBuilderTagLib])
class TipoPreferenciaControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
    }

    void "Debe redireccionar a list cuando se ejecuta index"() {
        when:
        controller.index()
        then:
        response.redirectedUrl == "/tipoPreferencia/list"
    }

    void "Debe obtener la lista de tipoPreferencias y su numero"() {
        setup:
        tipoPreferenciaInstance.save()

        expect:
        controller.list() == [tipoPreferenciaInstanceList: [tipoPreferenciaInstance],
                              tipoPreferenciaInstanceCount: 1]

        where:
        tipoPreferenciaInstance = new TipoPreferenciaBuilder().crear()
    }

    void "Debe devolver una instancia de tipoPreferencia"() {
        when:
        def tipoPreferenciaInstanceReturned = controller.form_ajax().tipoPreferenciaInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoPreferencia, _) >> tipoPreferenciaInstance
        tipoPreferenciaInstanceReturned.properties == tipoPreferenciaInstance.properties

        where:
        tipoPreferenciaInstance << [new TipoPreferencia(), new TipoPreferenciaBuilder().crear()]
    }

    void "Debe guardar un tipoPreferencia valido"() {
        setup:
        def tipoPreferenciaInstance = new TipoPreferencia()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoPreferencia, _) >> tipoPreferenciaInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as TipoPreferencia, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un tipoPreferencia no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def tipoPreferenciaInstance = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoPreferencia, _) >> tipoPreferenciaInstance
        0 * crudHelperServiceMock.guardarObjeto(_ as TipoPreferencia, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un tipoPreferencia con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def tipoPreferenciaInstance = new TipoPreferenciaBuilder().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoPreferencia, _) >> tipoPreferenciaInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as TipoPreferencia, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un tipoPreferencia valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def tipoPreferenciaInstance = new TipoPreferenciaBuilder().crear()
        def random = new Random()
        tipoPreferenciaInstance.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoPreferencia, _) >> tipoPreferenciaInstance
        1 * crudHelperServiceMock.eliminarObjeto(_ as TipoPreferencia) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un tipoPreferencia no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(TipoPreferencia, _) >> tipoPreferenciaInstance
        0 * crudHelperServiceMock.eliminarObjeto(_ as TipoPreferencia)
        response.text == expectedMessage

        where:
        tipoPreferenciaInstance << [null, new TipoPreferencia()]
    }
}
