package ec.com.tw.parking

import ec.com.tw.parking.helpers.RandomUtilsHelpers
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
        autoInstance = RandomUtilsHelpers.generaAutoValido()
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
        autoInstance = RandomUtilsHelpers.generaAutoValido()
    }

    void "Debe guardar un auto valido"() {
        setup:
        def auto = RandomUtilsHelpers.generaAutoValido()
        auto.properties.each { property, value ->
            controller.params[property] = value
        }
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
        autoInstance.properties.each { property, value ->
            value == controller.params[property]
        }
        response.text == expectedMessage
    }

    void "Debe actualizar un auto valido"() {
        setup:
        autoInstance.save()
        def marcaNueva = RandomUtilsHelpers.getRandomString(2, 20, false)
        def expectedMessage = "SUCCESS*default.saved.message"
        controller.params.id = autoInstance.id
        controller.params.marca = marcaNueva
        mockObjeto(crudHelperServiceMock, autoInstance)
        mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Auto.count() == 1
        Auto.get(1).marca == marcaNueva
        response.text == expectedMessage

        where:
        autoInstance = RandomUtilsHelpers.generaAutoValido()
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
        autoInstance = RandomUtilsHelpers.generaAutoValido()
    }

    void "Debe mostrar error al actualizar un auto con datos invalidos"() {
        setup:
        autoInstance.save()
        def marcaInvalida = RandomUtilsHelpers.getRandomString(21, 200, false)
        def expectedError = "ERROR*default.not.saved.message: <ul><li>Property [marca] of class [class ec.com.tw.parking.Auto] with value [" + marcaInvalida + "] exceeds the maximum size of [20]</li></ul>"
        controller.params.id = autoInstance.id
        controller.params.nombre = marcaInvalida
        mockObjeto(crudHelperServiceMock, autoInstance)
        mockGuardarObjeto(crudHelperServiceMock, expectedError)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Auto.count() == 1
        response.text == expectedError

        where:
        autoInstance = RandomUtilsHelpers.generaAutoValido()
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
        autoInstance = RandomUtilsHelpers.generaAutoValido()
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
        autoInstance = RandomUtilsHelpers.generaAutoValido()
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
        autoInstance = RandomUtilsHelpers.generaAutoValido()
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
