package ec.com.tw.parking

import grails.test.mixin.TestFor
import spock.lang.Specification
import org.apache.commons.lang.RandomStringUtils

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Usuario)
class UsuarioSpec extends Specification {

    def usuario

    def setup() {
        def random = new Random()
        def lengthNombre = random.nextInt(50 - 3) + 3
        def lengthMail = random.nextInt(90 - 3) + 3
        def lengthPass = random.nextInt(512 - 3) + 3
        def nombre = RandomStringUtils.randomAlphabetic(lengthNombre)
        def mail = RandomStringUtils.randomAlphabetic(lengthMail) + "@test.com"
        def pass = RandomStringUtils.random(lengthPass)
        usuario = new Usuario([nombre  : nombre,
                               email   : mail,
                               password: pass,
                               esAdmin : true])
    }

    def cleanup() {
    }

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'

        then: 'la validacion debe pasar'
        usuario.validate()
        !usuario.hasErrors()
    }

    void "No debe ser el nombre nulo"() {
        when: 'el nombre es nulo'
        usuario.nombre = null

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['nombre'].code == 'nullable'
    }

    void "No debe ser el nombre blanco"() {
        when: 'el nombre es blanco'
        usuario.nombre = ''

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['nombre'].code == 'blank'
    }

    void "No debe el nombre tener menos de 3 caracteres"() {
        when: 'el nombre tiene menos de 3 caracteres'
        usuario.nombre = 'ab'

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['nombre'].code == 'minSize.notmet'
    }

    void "No debe el nombre tener mas de 50 caracteres"() {
        when: 'el nombre tiene mas de 50 caracteres'
        usuario.nombre = 'Neque porro quisquam est qui dolorem ipsum quia dolor sit amet consectetur adipisci velit'

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['nombre'].code == 'maxSize.exceeded'
    }

    void "No debe ser el email nulo"() {
        when: 'el email es nulo'
        usuario.email = null

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['email'].code == 'nullable'
    }

    void "No debe ser el email blanco"() {
        when: 'el email es blanco'
        usuario.email = ''

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['email'].code == 'blank'
    }

    void "Debe el email tener el formato correcto"() {
        when: 'el email tiene un formato incorrecto'
        usuario.email = 'mail'

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['email'].code == 'email.invalid'
    }

    void "No debe el email tener mas de 100 caracteres"() {
        when: 'el email tiene mas de 100 caracteres'
        usuario.email = 'Nequeporroquisquamestquidoloremipsumquiadolorsitametconsecteturadipiscivelitsit@ametconsecteturadipiscivelit.com'

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['email'].code == 'maxSize.exceeded'
    }

    void "No debe ser el password nulo"() {
        when: 'el password es nulo'
        usuario.password = null

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['password'].code == 'nullable'
    }

    void "No debe ser el password blanco"() {
        when: 'el password es blanco'
        usuario.password = ''

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['password'].code == 'blank'
    }

    void "No debe el password tener mas de 512 caracteres"() {
        when: 'el password tiene mas de 512 caracteres'
        usuario.password = 'NequeporroquisquamestquidoloremipsumquiadolorsitametconsecteturadipiscivelitsitametconsecteturadipiscivelitcomNequeporroquisquamestquidoloremipsumquiadolorsitametconsecteturadipiscivelitsitametconsecteturadipiscivelitcomNequeporroquisquamestquidoloremipsumquiadolorsitametconsecteturadipiscivelitsitametconsecteturadipiscivelitcomNequeporroquisquamestquidoloremipsumquiadolorsitametconsecteturadipiscivelitsitametconsecteturadipiscivelitcomNequeporroquisquamestquidoloremipsumquiadolorsitametconsecteturadipiscivelitsitametconsecteturadipiscivelitcom'

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['password'].code == 'maxSize.exceeded'
    }

    void "No debe esAdmin ser nulo"() {
        when: 'esAdmin es nulo'
        usuario.esAdmin = null

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['esAdmin'].code == 'nullable'
    }
}
