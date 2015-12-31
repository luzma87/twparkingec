package ec.com.tw.parking

import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.*
import spock.lang.*

@TestFor(UsuarioController)
@Mock([Usuario, MensajesBuilderTagLib])
class UsuarioControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
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

    void "Debe devolver una instancia de usuario"() {
        when:
        def usuarioReturned = controller.form_ajax().usuarioInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Usuario, _) >> usuario
        usuarioReturned.properties == usuario.properties

        where:
        usuario << [new Usuario(), new UsuarioBuilder().crear()]
    }

    void "Debe guardar un usuario valido"() {
        setup:
        Usuario usuario = new Usuario()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Usuario, _) >> usuario
        1 * crudHelperServiceMock.guardarObjeto('usuario', _ as Usuario, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un usuario no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def usuario = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Usuario, _) >> usuario
        0 * crudHelperServiceMock.guardarObjeto('usuario', _ as Usuario, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un usuario con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def usuario = new UsuarioBuilder().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Usuario, _) >> usuario
        1 * crudHelperServiceMock.guardarObjeto('usuario', _ as Usuario, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un usuario valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def usuario = new UsuarioBuilder().crear()
        def random = new Random()
        usuario.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Usuario, _) >> usuario
        1 * crudHelperServiceMock.eliminarObjeto('usuario', _ as Usuario) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un usuario no encontrado o sin id"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Usuario, _) >> usuario
        0 * crudHelperServiceMock.eliminarObjeto('usuario', _ as Usuario)
        response.text == expectedMessage

        where:
        usuario << [null, new Usuario()]
    }

    void "Debe  devolver una instancia de usuario cuando recibe id para cambiar password"() {
        when:
        def usuarioReturned = controller.password_ajax().usuarioInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(Usuario, _) >> usuario
        usuarioReturned.properties == usuario.properties

        where:
        usuario << [new Usuario(), new UsuarioBuilder().crear()]
    }
}
