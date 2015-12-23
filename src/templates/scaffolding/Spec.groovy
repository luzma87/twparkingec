<%=packageName ? "package ${packageName}\n\n" : ''%>
<% classNameLower = domainClass.propertyName %>
import grails.test.mixin.*
import spock.lang.*

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
        ${modelName} = TestsHelpers.getValid${className}()
    }

    void "Debe devolver una nueva instancia de ${classNameLower}"() {
        setup:
        TestsHelpers.mockObjeto(crudHelperServiceMock, new ${className}())
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
        TestsHelpers.mockObjeto(crudHelperServiceMock, ${modelName})
        injectMock()

        expect:
        controller.form_ajax().${modelName}.properties == ${modelName}.properties

        where:
        ${modelName} = TestsHelpers.getValid${className}()
    }

    void "Debe guardar un ${classNameLower} valido"() {
        setup:
//        TODO: aqui setear los parametros
//        controller.params.nombre = TestsHelpers.getRandomNombre()
        def expectedMessage = "SUCCESS*default.saved.message"
        TestsHelpers.mockObjeto(crudHelperServiceMock, new ${className}())
        TestsHelpers.mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        ${className}.count() == 1
        def ${modelName} = ${className}.get(1)
//        TODO: aqui validar los datos:
//        ${classNameLower}.nombre == controller.params.nombre
        response.text == expectedMessage
    }

    void "Debe actualizar un ${classNameLower} valido"() {
        setup:
        ${modelName}.save()
//        TODO: cambiar aqui a un campo existente y valido
        def nombreNuevo = TestsHelpers.getRandomNombre()
        def expectedMessage = "SUCCESS*default.saved.message"
        controller.params.id = ${modelName}.id
        controller.params.nombre = nombreNuevo
        TestsHelpers.mockObjeto(crudHelperServiceMock, ${modelName})
        TestsHelpers.mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        ${className}.count() == 1
        ${className}.get(1).nombre == nombreNuevo
        response.text == expectedMessage

        where:
        ${modelName} = TestsHelpers.getValid${className}()
    }

    void "Debe mostrar error al intentar actualizar un ${classNameLower} no encontrado"() {
        setup:
        ${modelName}.save()
        controller.params.id = 3
        TestsHelpers.mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        ${className}.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        ${modelName} = TestsHelpers.getValid${className}()
    }

    void "Debe mostrar error al actualizar un ${classNameLower} con datos invalidos"() {
        setup:
        ${modelName}.save()
//        TODO: cambiar aqui a un campo existente y no valido
        def nombreInvalido = TestsHelpers.getRandomNombreInvalido()
        def expectedError = "ERROR*default.not.saved.message: <ul><li>Property [nombre] of class [class ec.com.tw.parking.${className}] with value [" + nombreInvalido + "] exceeds the maximum size of [50]</li></ul>"
        controller.params.id = ${modelName}.id
        controller.params.nombre = nombreInvalido
        TestsHelpers.mockObjeto(crudHelperServiceMock, ${modelName})
        TestsHelpers.mockGuardarObjeto(crudHelperServiceMock, expectedError)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        ${className}.count() == 1
        response.text == expectedError

        where:
        ${modelName} = TestsHelpers.getValid${className}()
    }

    void "Debe eliminar un ${classNameLower} valido"() {
        setup:
        ${modelName}.save()
        def expectedMessage = "SUCCESS*default.deleted.message"
        controller.params.id = ${modelName}.id
        TestsHelpers.mockObjeto(crudHelperServiceMock, ${modelName})
        TestsHelpers.mockEliminarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        ${className}.count() == 0
        response.text == expectedMessage

        where:
        ${modelName} = TestsHelpers.getValid${className}()
    }

    void "Debe mostrar error al intentar eliminar un ${classNameLower} no encontrado"() {
        setup:
        ${modelName}.save()
        controller.params.id = 3
        TestsHelpers.mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        ${className}.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        ${modelName} = TestsHelpers.getValid${className}()
    }

    void "Debe mostrar error al intentar eliminar un ${classNameLower} sin parametro id"() {
        setup:
        ${modelName}.save()
        TestsHelpers.mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        ${className}.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        ${modelName} = TestsHelpers.getValid${className}()
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
