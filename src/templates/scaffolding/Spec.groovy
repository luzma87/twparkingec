<%=packageName ? "package ${packageName}\n\n" : ''%>
<% classNameLower = domainClass.propertyName %>
import ec.com.tw.parking.builders.${className}Builder
import grails.test.mixin.*
import spock.lang.*

import static ec.com.tw.parking.helpers.MocksHelpers.mockEliminarObjeto
import static ec.com.tw.parking.helpers.MocksHelpers.mockGuardarObjeto
import static ec.com.tw.parking.helpers.MocksHelpers.mockObjeto

@TestFor(${className}Controller)
@Mock([${className}, MensajesBuilderTagLib])
class ${className}ControllerSpec extends Specification {

    def crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = mockFor(CrudHelperService)
    }

    void "Debe redireccionar a list cuando se ejecuta index"() {
        when:
        controller.index()
        then:
        response.redirectedUrl == "/${classNameLower}/list"
    }

    void "Debe obtener la lista de ${classNameLower}s y su numero"() {
        setup:
        ${modelName}.save()

        expect:
        controller.list() == [${modelName}List: [${modelName}],
                              ${modelName}Count: 1]

        where:
        ${modelName} = new ${className}Builder().crear()
    }

    void "Debe devolver una nueva instancia de ${classNameLower}"() {
        setup:
        mockObjeto(crudHelperServiceMock, new ${className}())
        injectMock()

        expect:
        controller.form_ajax().${modelName}.properties == ${modelName}.properties

        where:
        ${modelName} = new ${className}()
    }

    void "Debe devolver una instancia de ${classNameLower} cuando recibe id"() {
        setup:
        ${modelName}.save()
        controller.params.id = ${modelName}.id
        mockObjeto(crudHelperServiceMock, ${modelName})
        injectMock()

        expect:
        controller.form_ajax().${modelName}.properties == ${modelName}.properties

        where:
        ${modelName} = new ${className}Builder().crear()
    }

    void "Debe guardar un ${classNameLower} valido"() {
        setup:
        def parametrosValidos = new ${className}Builder().getParams()
        controller.params.putAll(parametrosValidos)
        def expectedMessage = "SUCCESS*default.saved.message"
        mockObjeto(crudHelperServiceMock, new ${className}())
        mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        ${className}.count() == 1
        def ${modelName} = ${className}.get(1)
        ${modelName}.properties.each {campo, valor ->
            controller.params[campo] == valor
        }
        response.text == expectedMessage
    }

    void "Debe actualizar un ${classNameLower} valido"() {
        setup:
        ${modelName}.save()
        def expectedMessage = "SUCCESS*default.saved.message"
        controller.params.id = ${modelName}.id
        controller.params[campoNuevo.campo] = campoNuevo.valor
        mockObjeto(crudHelperServiceMock, ${modelName})
        mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        ${className}.count() == 1
        ${className}.get(1)[campoNuevo.campo] == campoNuevo.valor
        response.text == expectedMessage

        where:
        ${classNameLower}Builder = new ${className}Builder()
        ${modelName} = ${classNameLower}Builder.crear()
        campoNuevo = ${classNameLower}Builder.getCampoNuevoValido()
    }

    void "Debe mostrar error al intentar actualizar un ${classNameLower} no encontrado"() {
        setup:
        ${modelName}.save()
        controller.params.id = 3
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        ${className}.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        ${modelName} = new ${className}Builder().crear()
    }

    void "Debe mostrar error al actualizar un ${classNameLower} con datos invalidos"() {
        setup:
        ${modelName}.save()
        def expectedError = "ERROR*default.not.saved.message"
        controller.params.id = ${modelName}.id
        controller.params[campoNuevo.campo] = campoNuevo.valor
        mockObjeto(crudHelperServiceMock, ${modelName})
        mockGuardarObjeto(crudHelperServiceMock, expectedError)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        ${className}.count() == 1
        response.text.startsWith(expectedError)

        where:
        ${classNameLower}Builder = new ${className}Builder()
        ${modelName} = ${classNameLower}Builder.crear()
        campoNuevo = ${classNameLower}Builder.getCampoNuevoInvalido()
    }

    void "Debe eliminar un ${classNameLower} valido"() {
        setup:
        ${modelName}.save()
        def expectedMessage = "SUCCESS*default.deleted.message"
        controller.params.id = ${modelName}.id
        mockObjeto(crudHelperServiceMock, ${modelName})
        mockEliminarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        ${className}.count() == 0
        response.text == expectedMessage

        where:
        ${modelName} = new ${className}Builder().crear()
    }

    void "Debe mostrar error al intentar eliminar un ${classNameLower} no encontrado"() {
        setup:
        ${modelName}.save()
        controller.params.id = 3
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        ${className}.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        ${modelName} = new ${className}Builder().crear()
    }

    void "Debe mostrar error al intentar eliminar un ${classNameLower} sin parametro id"() {
        setup:
        ${modelName}.save()
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        ${className}.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        ${modelName} = new ${className}Builder().crear()
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
