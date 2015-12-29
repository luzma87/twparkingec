package ec.com.tw.parking


import grails.test.mixin.*
import org.junit.Test
import spock.lang.*

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
        autoInstance = TestsHelpers.generaAutoValido()
    }

    void "Debe devolver una nueva instancia de auto"() {
        setup:
        TestsHelpers.mockObjeto(crudHelperServiceMock, new Auto())
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
        TestsHelpers.mockObjeto(crudHelperServiceMock, autoInstance)
        injectMock()

        expect:
        controller.form_ajax().autoInstance.properties == autoInstance.properties

        where:
        autoInstance = TestsHelpers.generaAutoValido()
    }

    void "Debe guardar un auto valido"() {
        setup:
        def auto = TestsHelpers.generaAutoValido()
        auto.properties.each { property, value ->
            controller.params[property] = value
        }
        def expectedMessage = "SUCCESS*default.saved.message"
        TestsHelpers.mockObjeto(crudHelperServiceMock, new Auto())
        TestsHelpers.mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
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
        def marcaNueva = TestsHelpers.getRandomString(2, 20, false)
        def expectedMessage = "SUCCESS*default.saved.message"
        controller.params.id = autoInstance.id
        controller.params.marca = marcaNueva
        TestsHelpers.mockObjeto(crudHelperServiceMock, autoInstance)
        TestsHelpers.mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Auto.count() == 1
        Auto.get(1).marca == marcaNueva
        response.text == expectedMessage

        where:
        autoInstance = TestsHelpers.generaAutoValido()
    }

    void "Debe mostrar error al intentar actualizar un auto no encontrado"() {
        setup:
        autoInstance.save()
        controller.params.id = 3
        TestsHelpers.mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Auto.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        autoInstance = TestsHelpers.generaAutoValido()
    }

    void "Debe mostrar error al actualizar un auto con datos invalidos"() {
        setup:
        autoInstance.save()
        def marcaInvalida = TestsHelpers.getRandomString(21, 200, false)
        def expectedError = "ERROR*default.not.saved.message: <ul><li>Property [marca] of class [class ec.com.tw.parking.Auto] with value [" + marcaInvalida + "] exceeds the maximum size of [20]</li></ul>"
        controller.params.id = autoInstance.id
        controller.params.nombre = marcaInvalida
        TestsHelpers.mockObjeto(crudHelperServiceMock, autoInstance)
        TestsHelpers.mockGuardarObjeto(crudHelperServiceMock, expectedError)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Auto.count() == 1
        response.text == expectedError

        where:
        autoInstance = TestsHelpers.generaAutoValido()
    }

    void "Debe eliminar un auto valido"() {
        setup:
        autoInstance.save()
        def expectedMessage = "SUCCESS*default.deleted.message"
        controller.params.id = autoInstance.id
        TestsHelpers.mockObjeto(crudHelperServiceMock, autoInstance)
        TestsHelpers.mockEliminarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Auto.count() == 0
        response.text == expectedMessage

        where:
        autoInstance = TestsHelpers.generaAutoValido()
    }

    void "Debe mostrar error al intentar eliminar un auto no encontrado"() {
        setup:
        autoInstance.save()
        controller.params.id = 3
        TestsHelpers.mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Auto.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        autoInstance = TestsHelpers.generaAutoValido()
    }

    void "Debe mostrar error al intentar eliminar un auto sin parametro id"() {
        setup:
        autoInstance.save()
        TestsHelpers.mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Auto.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        autoInstance = TestsHelpers.generaAutoValido()
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
