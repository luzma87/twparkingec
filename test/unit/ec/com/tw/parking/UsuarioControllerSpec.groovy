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
        usuarioInstance = TestsHelpers.generaUsuarioValido()
    }

    void "Debe devolver una nueva instancia de usuario"() {
        setup:
        TestsHelpers.mockObjeto(crudHelperServiceMock, new Usuario())
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
        TestsHelpers.mockObjeto(crudHelperServiceMock, usuarioInstance)
        injectMock()

        expect:
        controller.form_ajax().usuarioInstance.properties == usuarioInstance.properties

        where:
        usuarioInstance = TestsHelpers.generaUsuarioValido()
    }

    void "Debe guardar un usuario valido"() {
        setup:
        controller.params.nombre = TestsHelpers.getRandomString(3, 50, false)
        controller.params.email = TestsHelpers.getRandomMail()
        controller.params.password = TestsHelpers.getRandomString(3, 512, true)
        controller.params.esAdmin = TestsHelpers.getRandomAdmin()
        def expectedMessage = "SUCCESS*default.saved.message"
        TestsHelpers.mockObjeto(crudHelperServiceMock, new Usuario())
        TestsHelpers.mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
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
        def nombreNuevo = TestsHelpers.getRandomString(3, 50, false)
        def expectedMessage = "SUCCESS*default.saved.message"
        controller.params.id = usuarioInstance.id
        controller.params.nombre = nombreNuevo
        TestsHelpers.mockObjeto(crudHelperServiceMock, usuarioInstance)
        TestsHelpers.mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        Usuario.get(1).nombre == nombreNuevo
        response.text == expectedMessage

        where:
        usuarioInstance = TestsHelpers.generaUsuarioValido()
    }

    void "Debe mostrar error al intentar actualizar un usuario no encontrado"() {
        setup:
        usuarioInstance.save()
        controller.params.id = 3
        TestsHelpers.mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        usuarioInstance = TestsHelpers.generaUsuarioValido()
    }

    void "Debe mostrar error al actualizar un usuario con datos invalidos"() {
        setup:
        usuarioInstance.save()
        def nombreInvalido = TestsHelpers.getRandomString(51, 150, false)
        def expectedError = "ERROR*default.not.saved.message: <ul><li>Property [nombre] of class [class ec.com.tw.parking.Usuario] with value [" + nombreInvalido + "] exceeds the maximum size of [50]</li></ul>"
        controller.params.id = usuarioInstance.id
        controller.params.nombre = nombreInvalido
        TestsHelpers.mockObjeto(crudHelperServiceMock, usuarioInstance)
        TestsHelpers.mockGuardarObjeto(crudHelperServiceMock, expectedError)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        response.text == expectedError

        where:
        usuarioInstance = TestsHelpers.generaUsuarioValido()
    }

    void "Debe eliminar un usuario valido"() {
        setup:
        usuarioInstance.save()
        def expectedMessage = "SUCCESS*default.deleted.message"
        controller.params.id = usuarioInstance.id
        TestsHelpers.mockObjeto(crudHelperServiceMock, usuarioInstance)
        TestsHelpers.mockEliminarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Usuario.count() == 0
        response.text == expectedMessage

        where:
        usuarioInstance = TestsHelpers.generaUsuarioValido()
    }

    void "Debe mostrar error al intentar eliminar un usuario no encontrado"() {
        setup:
        usuarioInstance.save()
        controller.params.id = 3
        TestsHelpers.mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Usuario.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        usuarioInstance = TestsHelpers.generaUsuarioValido()
    }

    void "Debe mostrar error al intentar eliminar un usuario sin parametro id"() {
        setup:
        usuarioInstance.save()
        TestsHelpers.mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Usuario.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        usuarioInstance = TestsHelpers.generaUsuarioValido()
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
