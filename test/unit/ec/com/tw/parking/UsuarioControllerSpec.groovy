package ec.com.tw.parking

import grails.test.mixin.*
import spock.lang.*
import org.apache.commons.lang.RandomStringUtils

@TestFor(UsuarioController)
@Mock([Usuario, MensajesBuilderTagLib])
class UsuarioControllerSpec extends Specification {

    def getRandomNombre() {
        def random = new Random()
        def lengthNombre = random.nextInt(50 - 3) + 3
        return RandomStringUtils.randomAlphabetic(lengthNombre)
    }

    def getRandomNombreInvalido() {
        def random = new Random()
        def lengthNombre = random.nextInt(150 - 50) + 50
        return RandomStringUtils.randomAlphabetic(lengthNombre)
    }

    def getRandomMail() {
        def random = new Random()
        def lengthMail = random.nextInt(90 - 3) + 3
        return RandomStringUtils.randomAlphabetic(lengthMail) + "@test.com"
    }

    def getRandomPass() {
        def random = new Random()
        def lengthPass = random.nextInt(512 - 3) + 3
        return RandomStringUtils.random(lengthPass)
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
        expect:
        controller.form_ajax().usuarioInstance.properties == usuarioInstance.properties

        where:
        usuarioInstance = new Usuario()
    }

    void "Debe devolver una instancia de usuario cuando recibe id"() {
        setup:
        usuarioInstance.save()
        controller.params.id = usuarioInstance.id

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
        response.text == "SUCCESS*default.saved.message"
    }

    void "Debe actualizar un usuario valido"() {
        setup:
        usuarioInstance.save()
        def nombreNuevo = getRandomNombre()
        controller.params.id = usuarioInstance.id
        controller.params.nombre = nombreNuevo

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        Usuario.get(1).nombre == nombreNuevo
        response.text == "SUCCESS*default.saved.message"

        where:
        usuarioInstance = getValidUsuario()
    }

    void "Debe mostrar error al actualizar un usuario no encontrado"() {
        setup:
        usuarioInstance.save()
        controller.params.id = 3

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
        controller.params.id = usuarioInstance.id
        controller.params.nombre = nombreInvalido

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        Usuario.count() == 1
        def expectedError = "ERROR*default.not.saved.message: <ul><li>Property [nombre] of class [class ec.com.tw.parking.Usuario] with value [" + nombreInvalido + "] exceeds the maximum size of [50]</li></ul>"
        response.text == expectedError

        where:
        usuarioInstance = getValidUsuario()
    }

}
