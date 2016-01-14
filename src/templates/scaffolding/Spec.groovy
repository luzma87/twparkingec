<%=packageName ? "package ${packageName}\n\n" : ''%>
<% classNameLower = domainClass.propertyName %>
import ec.com.tw.parking.builders.${className}Builder
import grails.test.mixin.*
import spock.lang.*

@TestFor(${className}Controller)
@Mock([${className}, MensajesBuilderTagLib])
class ${className}ControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
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
        ${modelName} = ${className}Builder.nuevo().crear()
    }

    void "Debe devolver una instancia de ${classNameLower}"() {
        when:
        def ${modelName}Returned = controller.form_ajax().${modelName}

        then:
        1 * crudHelperServiceMock.obtenerObjeto(${className}, _) >> ${modelName}
        ${modelName}Returned.properties == ${modelName}.properties

        where:
        ${modelName} << [new ${className}(), ${className}Builder.nuevo().crear()]
    }

    void "Debe guardar un ${classNameLower} valido"() {
        setup:
        def ${modelName} = new ${className}()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(${className}, _) >> ${modelName}
        1 * crudHelperServiceMock.guardarObjeto(_ as ${className}, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un ${classNameLower} no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def ${modelName} = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(${className}, _) >> ${modelName}
        0 * crudHelperServiceMock.guardarObjeto(_ as ${className}, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un ${classNameLower} con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def ${modelName} = ${className}Builder.nuevo().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(${className}, _) >> ${modelName}
        1 * crudHelperServiceMock.guardarObjeto(_ as ${className}, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un ${classNameLower} valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def ${modelName} = ${className}Builder.nuevo().crear()
        def random = new Random()
        ${modelName}.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(${className}, _) >> ${modelName}
        1 * crudHelperServiceMock.eliminarObjeto(_ as ${className}) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un ${classNameLower} no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(${className}, _) >> ${modelName}
        0 * crudHelperServiceMock.eliminarObjeto(_ as ${className})
        response.text == expectedMessage

        where:
        ${modelName} << [null, new ${className}()]
    }
}
