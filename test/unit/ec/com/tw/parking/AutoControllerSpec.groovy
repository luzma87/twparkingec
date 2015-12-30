package ec.com.tw.parking

import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.*
import spock.lang.*

import static ec.com.tw.parking.helpers.MocksHelpers.mockEliminarObjeto
import static ec.com.tw.parking.helpers.MocksHelpers.mockGuardarObjeto
import static ec.com.tw.parking.helpers.MocksHelpers.mockObjeto

@TestFor(AutoController)
@Mock([Auto, MensajesBuilderTagLib])
class AutoControllerSpec extends Specification {

    def crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = mockFor(CrudHelperService)
    }

    void "Debe redireccionar a list cuando se ejecuta index"() {
        when:
        controller.index()
        then:
        response.redirectedUrl == "/auto/list"
    }

    void "Debe obtener la lista de autos y su numero"() {
        setup:
        autoInstance.save()

        expect:
        controller.list() == [autoInstanceList : [autoInstance],
                              autoInstanceCount: 1]

        where:
        autoInstance = new AutoBuilder().crear()
    }

    void "Debe devolver una nueva instancia de auto"() {
        setup:
        mockObjeto(crudHelperServiceMock, new Auto())
        injectMock()

        expect:
        controller.form_ajax().autoInstance.properties == autoInstance.properties

        where:
        autoInstance = new Auto()
    }

    void "Debe devolver una instancia de auto cuando recibe id"() {
        setup:
        autoInstance.save()
        controller.params.id = autoInstance.id
        mockObjeto(crudHelperServiceMock, autoInstance)
        injectMock()

        expect:
        controller.form_ajax().autoInstance.properties == autoInstance.properties

        where:
        autoInstance = new AutoBuilder().crear()
    }

    void "Debe guardar un auto valido"() {
        setup:
        def parametrosValidos = new AutoBuilder().getParams()
        controller.params.putAll(parametrosValidos)
        def expectedMessage = "SUCCESS*default.saved.message"
        mockObjeto(crudHelperServiceMock, new Auto())
        mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Auto.count() == 1
        def autoInstance = Auto.get(1)
        autoInstance.properties.each { campo, valor ->
            controller.params[campo] == valor
        }
        response.text == expectedMessage
    }

    void "Debe actualizar un auto valido"() {
        setup:
        autoInstance.save()
        def expectedMessage = "SUCCESS*default.saved.message"
        controller.params.id = autoInstance.id
        controller.params[campoNuevo.campo] = campoNuevo.valor
        mockObjeto(crudHelperServiceMock, autoInstance)
        mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Auto.count() == 1
        Auto.get(1)[campoNuevo.campo] == campoNuevo.valor
        response.text == expectedMessage

        where:
        autoBuilder = new AutoBuilder()
        autoInstance = autoBuilder.crear()
        campoNuevo = autoBuilder.getCampoNuevoValido()
    }

    void "Debe mostrar error al intentar actualizar un auto no encontrado"() {
        setup:
        autoInstance.save()
        controller.params.id = 3
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Auto.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        autoInstance = new AutoBuilder().crear()
    }

    void "Debe mostrar error al actualizar un auto con datos invalidos"() {
        setup:
        autoInstance.save()
        def expectedError = "ERROR*default.not.saved.message"
        controller.params.id = autoInstance.id
        controller.params[campoNuevo.campo] = campoNuevo.valor
        mockObjeto(crudHelperServiceMock, autoInstance)
        mockGuardarObjeto(crudHelperServiceMock, expectedError)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Auto.count() == 1
        response.text.startsWith(expectedError)

        where:
        autoBuilder = new AutoBuilder()
        autoInstance = autoBuilder.crear()
        campoNuevo = autoBuilder.getCampoNuevoInvalido()
    }

    void "Debe eliminar un auto valido"() {
        setup:
        autoInstance.save()
        def expectedMessage = "SUCCESS*default.deleted.message"
        controller.params.id = autoInstance.id
        mockObjeto(crudHelperServiceMock, autoInstance)
        mockEliminarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Auto.count() == 0
        response.text == expectedMessage

        where:
        autoInstance = new AutoBuilder().crear()
    }

    void "Debe mostrar error al intentar eliminar un auto no encontrado"() {
        setup:
        autoInstance.save()
        controller.params.id = 3
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Auto.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        autoInstance = new AutoBuilder().crear()
    }

    void "Debe mostrar error al intentar eliminar un auto sin parametro id"() {
        setup:
        autoInstance.save()
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Auto.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        autoInstance = new AutoBuilder().crear()
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
