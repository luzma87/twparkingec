package ec.com.tw.parking

import grails.test.mixin.*
import spock.lang.*

@TestFor(UsuarioController)
@Mock([Usuario, MensajesBuilderTagLib])
class UsuarioControllerSpec extends Specification {

    def crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = mockFor(CrudHelperService)
    }

    void "Debe redireccionar a list cuando se ejecuta index"() {
        when:
        controller.index()
        then:
        response.redirectedUrl == "/usuario/list"
    }

    void "Debe obtener la lista de usuarios y su numero"() {
        setup:
        usuarioInstance.save()

        expect:
        controller.list() == [usuarioInstanceList: [usuarioInstance],
                              usuarioInstanceCount: 1]

        where:
        usuarioInstance = TestsHelpers.getValidUsuario()
    }

    void "Debe devolver una nueva instancia de usuario"() {
        setup:
        mockUsuario(new Usuario())
        injectMock()

        expect:
        controller.form_ajax().usuarioInstance.properties == usuarioInstance.properties

        where:
        usuarioInstance = new Usuario()
    }

    void "Debe devolver una instancia de usuario cuando recibe id"() {
        setup:
        usuarioInstance.save()
        controller.params.id = usuarioInstance.id
        mockUsuario(usuarioInstance)
        injectMock()

        expect:
        controller.form_ajax().usuarioInstance.properties == usuarioInstance.properties

        where:
        usuarioInstance = TestsHelpers.getValidUsuario()
    }

    void "Debe guardar un usuario valido"() {
        setup:
        controller.params.nombre = TestsHelpers.getRandomNombre()
        controller.params.email = TestsHelpers.getRandomMail()
        controller.params.password = TestsHelpers.getRandomPass()
        controller.params.esAdmin = TestsHelpers.getRandomAdmin()
        def expectedMessage = "SUCCESS*default.saved.message"
        mockUsuario(new Usuario())
        mockGuardarUsuario(expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        def usuarioInstance = Usuario.get(1)
        usuarioInstance.nombre == controller.params.nombre
        usuarioInstance.email == controller.params.email
        usuarioInstance.password == controller.params.password
        usuarioInstance.esAdmin == controller.params.esAdmin
        response.text == expectedMessage
    }

    void "Debe actualizar un usuario valido"() {
        setup:
        usuarioInstance.save()
        def nombreNuevo = TestsHelpers.getRandomNombre()
        def expectedMessage = "SUCCESS*default.saved.message"
        controller.params.id = usuarioInstance.id
        controller.params.nombre = nombreNuevo
        mockUsuario(usuarioInstance)
        mockGuardarUsuario(expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        Usuario.get(1).nombre == nombreNuevo
        response.text == expectedMessage

        where:
        usuarioInstance = TestsHelpers.getValidUsuario()
    }

    void "Debe mostrar error al intentar actualizar un usuario no encontrado"() {
        setup:
        usuarioInstance.save()
        controller.params.id = 3
        mockUsuario(null)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        usuarioInstance = TestsHelpers.getValidUsuario()
    }

    void "Debe mostrar error al actualizar un usuario con datos invalidos"() {
        setup:
        usuarioInstance.save()
        def nombreInvalido = TestsHelpers.getRandomNombreInvalido()
        def expectedError = "ERROR*default.not.saved.message: <ul><li>Property [nombre] of class [class ec.com.tw.parking.Usuario] with value [" + nombreInvalido + "] exceeds the maximum size of [50]</li></ul>"
        controller.params.id = usuarioInstance.id
        controller.params.nombre = nombreInvalido
        mockUsuario(usuarioInstance)
        mockGuardarUsuario(expectedError)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        response.text == expectedError

        where:
        usuarioInstance = TestsHelpers.getValidUsuario()
    }

    void "Debe eliminar un usuario valido"() {
        setup:
        usuarioInstance.save()
        def expectedMessage = "SUCCESS*default.deleted.message"
        controller.params.id = usuarioInstance.id
        mockUsuario(usuarioInstance)
        mockEliminarUsuario(expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Usuario.count() == 0
        response.text == expectedMessage

        where:
        usuarioInstance = TestsHelpers.getValidUsuario()
    }

    void "Debe mostrar error al intentar eliminar un usuario no encontrado"() {
        setup:
        usuarioInstance.save()
        controller.params.id = 3
        mockUsuario(null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Usuario.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        usuarioInstance = TestsHelpers.getValidUsuario()
    }

    void "Debe mostrar error al intentar eliminar un usuario sin parametro id"() {
        setup:
        usuarioInstance.save()
        mockUsuario(null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Usuario.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        usuarioInstance = TestsHelpers.getValidUsuario()
    }

    def mockUsuario(expectedReturn) {
        crudHelperServiceMock.demand.obtenerObjeto { dominio, id -> return expectedReturn }
        return crudHelperServiceMock
    }

    def mockGuardarUsuario(expectedReturn) {
        crudHelperServiceMock.demand.guardarObjeto { entidad, objeto, params ->
            objeto.properties = params
            objeto.save(flush: true)
            return expectedReturn
        }
    }

    def mockEliminarUsuario(expectedReturn) {
        crudHelperServiceMock.demand.eliminarObjeto { entidad, objeto ->
            objeto.delete(flush: true)
            return expectedReturn
        }
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
