package ec.com.tw.parking

import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.*
import spock.lang.*

import static ec.com.tw.parking.helpers.MocksHelpers.mockEliminarObjeto
import static ec.com.tw.parking.helpers.MocksHelpers.mockGuardarObjeto
import static ec.com.tw.parking.helpers.MocksHelpers.mockObjeto
import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

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
        controller.list() == [usuarioInstanceList : [usuarioInstance],
                              usuarioInstanceCount: 1]

        where:
        usuarioInstance = new UsuarioBuilder().crear()
    }

    void "Debe devolver una nueva instancia de usuario"() {
        setup:
        mockObjeto(crudHelperServiceMock, new Usuario())
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
        mockObjeto(crudHelperServiceMock, usuarioInstance)
        injectMock()

        expect:
        controller.form_ajax().usuarioInstance.properties == usuarioInstance.properties

        where:
        usuarioInstance = new UsuarioBuilder().crear()
    }

    void "Debe guardar un usuario valido"() {
        setup:
        def parametrosValidos = new UsuarioBuilder().getParams()
        controller.params.putAll(parametrosValidos)
        def expectedMessage = "SUCCESS*default.saved.message"
        mockObjeto(crudHelperServiceMock, new Usuario())
        mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        def usuarioInstance = Usuario.get(1)
        usuarioInstance.properties.each { campo, valor ->
            controller.params[campo] == valor
        }
        response.text == expectedMessage
    }

    void "Debe actualizar un usuario valido"() {
        setup:
        usuarioInstance.save()
        def expectedMessage = "SUCCESS*default.saved.message"
        controller.params.id = usuarioInstance.id
        controller.params[campoNuevo.key] = campoNuevo.value
        mockObjeto(crudHelperServiceMock, usuarioInstance)
        mockGuardarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        Usuario.get(1)[campoNuevo.key] == campoNuevo.value
        response.text == expectedMessage

        where:
        usuarioBuilder = new UsuarioBuilder()
        usuarioInstance = usuarioBuilder.crear()
        campoNuevo = usuarioBuilder.getParams().find()
    }

    void "Debe mostrar error al intentar actualizar un usuario no encontrado"() {
        setup:
        usuarioInstance.save()
        controller.params.id = 3
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        usuarioInstance = new UsuarioBuilder().crear()
    }

    void "Debe mostrar error al actualizar un usuario con datos invalidos"() {
        setup:
        usuarioInstance.save()
        def expectedError = "ERROR*default.not.saved.message"
        controller.params.id = usuarioInstance.id
        controller.params[campoNuevo.key] = getRandomString(550, 1550, true)
        mockObjeto(crudHelperServiceMock, usuarioInstance)
        mockGuardarObjeto(crudHelperServiceMock, expectedError)
        injectMock()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        response.text.startsWith(expectedError)

        where:
        usuarioBuilder = new UsuarioBuilder()
        usuarioInstance = usuarioBuilder.crear()
        campoNuevo = usuarioBuilder.getParams().find()
    }

    void "Debe eliminar un usuario valido"() {
        setup:
        usuarioInstance.save()
        def expectedMessage = "SUCCESS*default.deleted.message"
        controller.params.id = usuarioInstance.id
        mockObjeto(crudHelperServiceMock, usuarioInstance)
        mockEliminarObjeto(crudHelperServiceMock, expectedMessage)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Usuario.count() == 0
        response.text == expectedMessage

        where:
        usuarioInstance = new UsuarioBuilder().crear()
    }

    void "Debe mostrar error al intentar eliminar un usuario no encontrado"() {
        setup:
        usuarioInstance.save()
        controller.params.id = 3
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Usuario.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        usuarioInstance = new UsuarioBuilder().crear()
    }

    void "Debe mostrar error al intentar eliminar un usuario sin parametro id"() {
        setup:
        usuarioInstance.save()
        mockObjeto(crudHelperServiceMock, null)
        injectMock()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        Usuario.count() == 1
        response.text == "ERROR*default.not.found.message"

        where:
        usuarioInstance = new UsuarioBuilder().crear()
    }

    void "Debe  devolver una instancia de usuario cuando recibe id para cambiar password"() {
        setup:
        usuarioInstance.save()
        controller.params.id = usuarioInstance.id
        mockObjeto(crudHelperServiceMock, usuarioInstance)
        injectMock()

        expect:
        controller.password_ajax().usuarioInstance.properties == usuarioInstance.properties

        where:
        usuarioInstance = new UsuarioBuilder().crear()
    }

    def injectMock() {
        controller.crudHelperService = crudHelperServiceMock.createMock()
    }
}
