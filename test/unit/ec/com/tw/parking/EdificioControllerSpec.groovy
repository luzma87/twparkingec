package ec.com.tw.parking


import ec.com.tw.parking.builders.EdificioBuilder
import grails.test.mixin.*
import spock.lang.*

import static ec.com.tw.parking.helpers.MocksHelpers.mockEliminarObjeto
import static ec.com.tw.parking.helpers.MocksHelpers.mockGuardarObjeto
import static ec.com.tw.parking.helpers.MocksHelpers.mockObjeto

@TestFor(EdificioController)
@Mock([Edificio, MensajesBuilderTagLib])
class EdificioControllerSpec extends Specification {

    def crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = mockFor(CrudHelperService)
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
        controller.list() == [edificioInstanceList : [edificioInstance],
                              edificioInstanceCount: 1]

        where:
        edificioInstance = new EdificioBuilder().crear()
    }

    void "Debe devolver una nueva instancia de edificio"() {
        setup:
        mockObjeto(crudHelperServiceMock, new Edificio())
        injectMock()

        expect:
        controller.form_ajax().edificioInstance.properties == edificioInstance.properties

        where:
        edificioInstance = new Edificio()
    }

    void "Debe devolver una instancia de edificio cuando recibe id"() {
        setup:
        edificioInstance.save()
        controller.params.id = edificioInstance.id
        mockObjeto(crudHelperServiceMock, edificioInstance)
        injectMock()

        expect:
        controller.form_ajax().edificioInstance.properties == edificioInstance.properties

        where:
        edificioInstance = new EdificioBuilder().crear()
    }

    void "Debe guardar un edificio valido"() {
        setup:
        def parametrosValidos = new EdificioBuilder().getParams()
        controller.params.putAll(parametrosValidos)
        def expectedMessage = "SUCCESS*default.saved.message"
        mockObjeto(crudHelperServiceMock, new Edificio())
        mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Edificio.count() == 1
        def edificioInstance = Edificio.get(1)
        edificioInstance.properties.each { campo, valor ->
            controller.params[campo] == valor
        }
        response.text == expectedMessage
    }

    void "Debe actualizar un edificio valido"() {
        setup:
        edificioInstance.save()
        def expectedMessage = "SUCCESS*default.saved.message"
        controller.params.id = edificioInstance.id
        controller.params[campoNuevo.campo] = campoNuevo.valor
        mockObjeto(crudHelperServiceMock, edificioInstance)
        mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Edificio.count() == 1
        Edificio.get(1)[campoNuevo.campo] == campoNuevo.valor
        response.text == expectedMessage

        where:
        edificioBuilder = new EdificioBuilder()
        edificioInstance = edificioBuilder.crear()
        campoNuevo = edificioBuilder.getCampoNuevoValido()
    }

    void "Debe mostrar error al intentar actualizar un edificio no encontrado"() {
        setup:
        edificioInstance.save()
        controller.params.id = 3
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Edificio.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        edificioInstance = new EdificioBuilder().crear()
    }

    void "Debe mostrar error al actualizar un edificio con datos invalidos"() {
        setup:
        edificioInstance.save()
        def expectedError = "ERROR*default.not.saved.message"
        controller.params.id = edificioInstance.id
        controller.params[campoNuevo.campo] = campoNuevo.valor
        mockObjeto(crudHelperServiceMock, edificioInstance)
        mockGuardarObjeto(crudHelperServiceMock, expectedError)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Edificio.count() == 1
        response.text.startsWith(expectedError)

        where:
        edificioBuilder = new EdificioBuilder()
        edificioInstance = edificioBuilder.crear()
        campoNuevo = edificioBuilder.getCampoNuevoInvalido()
    }

    void "Debe eliminar un edificio valido"() {
        setup:
        edificioInstance.save()
        def expectedMessage = "SUCCESS*default.deleted.message"
        controller.params.id = edificioInstance.id
        mockObjeto(crudHelperServiceMock, edificioInstance)
        mockEliminarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Edificio.count() == 0
        response.text == expectedMessage

        where:
        edificioInstance = new EdificioBuilder().crear()
    }

    void "Debe mostrar error al intentar eliminar un edificio no encontrado"() {
        setup:
        edificioInstance.save()
        controller.params.id = 3
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Edificio.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        edificioInstance = new EdificioBuilder().crear()
    }

    void "Debe mostrar error al intentar eliminar un edificio sin parametro id"() {
        setup:
        edificioInstance.save()
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Edificio.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        edificioInstance = new EdificioBuilder().crear()
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
