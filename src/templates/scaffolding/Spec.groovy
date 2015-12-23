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
        mock${className}(new ${className}())
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
        mock${className}(${modelName})
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
        mock${className}(new ${className}())
        mockGuardar${className}(expectedMessage)
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
        mock${className}(${modelName})
        mockGuardar${className}(expectedMessage)
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
        mock${className}(null)
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
        mock${className}(${modelName})
        mockGuardar${className}(expectedError)
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
        mock${className}(${modelName})
        mockEliminar${className}(expectedMessage)
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
        mock${className}(null)
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
        mock${className}(null)
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

    def mock${className}(expectedReturn) {
        crudHelperServiceMock.demand.obtenerObjeto { dominio, id -> return expectedReturn }
        return crudHelperServiceMock
    }

    def mockGuardar${className}(expectedReturn) {
        crudHelperServiceMock.demand.guardarObjeto { entidad, objeto, params ->
            objeto.properties = params
            objeto.save(flush: true)
            return expectedReturn
        }
    }

    def mockEliminar${className}(expectedReturn) {
        crudHelperServiceMock.demand.eliminarObjeto { entidad, objeto ->
            objeto.delete(flush: true)
            return expectedReturn
        }
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
