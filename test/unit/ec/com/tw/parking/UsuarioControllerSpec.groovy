package ec.com.tw.parking

import grails.test.mixin.*
import spock.lang.*
import org.apache.commons.lang.RandomStringUtils

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
        response.redirectedUrl == '/usuario/list'
    }

    void "Debe llamar a listar usuarios y su numero"() {
        setup:
        usuarioInstance.save()

        expect:
        controller.list() == [usuarioInstanceList: [usuarioInstance], usuarioInstanceCount: 1]

        where:
        usuarioInstance = getValidUsuario()
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
        usuarioInstance = getValidUsuario()
    }

    void "Debe guardar un usuario valido"() {
        setup:
        controller.params.nombre = getRandomNombre()
        controller.params.email = getRandomMail()
        controller.params.password = getRandomPass()
        controller.params.esAdmin = getRandomAdmin()
        def expectedMessage = "SUCCESS*default.saved.message"
        mockUsuario(new Usuario())
        mockGuardarUsuario(expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        def usuario = Usuario.get(1)
        usuario.nombre == controller.params.nombre
        usuario.email == controller.params.email
        usuario.password == controller.params.password
        usuario.esAdmin == controller.params.esAdmin
        response.text == expectedMessage
    }

    void "Debe actualizar un usuario valido"() {
        setup:
        usuarioInstance.save()
        def nombreNuevo = getRandomNombre()
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
        usuarioInstance = getValidUsuario()
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
        usuarioInstance = getValidUsuario()
    }

    void "Debe mostrar error al actualizar un usuario con datos invalidos"() {
        setup:
        usuarioInstance.save()
        def nombreInvalido = getRandomNombreInvalido()
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
        usuarioInstance = getValidUsuario()
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
        usuarioInstance = getValidUsuario()
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
        usuarioInstance = getValidUsuario()
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
        usuarioInstance = getValidUsuario()
    }

    def getRandomString(int min, int max, boolean allChars) {
        def random = new Random()
        def length = random.nextInt(max - min) + min
        if (allChars) {
            return RandomStringUtils.random(length)
        }
        return RandomStringUtils.randomAlphabetic(length)
    }

    def getRandomNombre() {
        return getRandomString(3, 50, false)
    }

    def getRandomNombreInvalido() {
        return getRandomString(51, 150, false)
    }

    def getRandomMail() {
        return getRandomString(3, 90, false) + "@test.com"
    }

    def getRandomPass() {
        return getRandomString(3, 512, true)
    }

    def getRandomAdmin() {
        def random = new Random()
        return random.nextBoolean()
    }

    def getValidUsuario() {
        return new Usuario([nombre  : getRandomNombre(),
                            email   : getRandomMail(),
                            password: getRandomPass(),
                            esAdmin : getRandomAdmin()])
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
