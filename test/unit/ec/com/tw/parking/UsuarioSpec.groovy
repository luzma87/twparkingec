package ec.com.tw.parking

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Usuario)
class UsuarioSpec extends Specification {

    def usuario

    def setup() {
        usuario = new Usuario([nombre: 'nombre', email: 'mail@test.com', password: '12346'])
    }

    def cleanup() {
    }

    void "No debe ser el nombre nulo"() {
        when: 'el nombre es nulo'
        usuario.nombre = null

        then: 'la validacion debe fallar'
        !usuario.validate()
    }

    void "Debe el nombre existir"() {
        when: 'el nombre no es nulo'

        then: 'la validacion debe pasar'
        usuario.validate()
    }

    void "No debe ser el nombre blanco"() {
        when: 'el nombre es blanco'
        usuario.nombre = ''

        then: 'la validacion debe fallar'
        !usuario.validate()
    }

    void "Debe el nombre tener caracteres"() {
        when: 'el nombre no es blanco'

        then: 'la validacion debe pasar'
        usuario.validate()
    }

    void "No debe el nombre tener menos de 3 caracteres"() {
        when: 'el nombre tiene menos de 3 caracteres'
        usuario.nombre = 'ab'

        then: 'la validacion debe fallar'
        !usuario.validate()
    }

    void "Debe el nombre tener mas de 3 caracteres"() {
        when: 'el nombre tiene 3 o mas caracteres'

        then: 'la validacion debe pasar'
        usuario.validate()
    }

    void "No debe el nombre tener mas de 50 caracteres"() {
        when: 'el nombre tiene mas de 50 caracteres'
        usuario.nombre = 'Neque porro quisquam est qui dolorem ipsum quia dolor sit amet consectetur adipisci velit'

        then: 'la validacion debe fallar'
        !usuario.validate()
    }

    void "Debe el nombre tener menos de 50 caracteres"() {
        when: 'el nombre tiene menos de 50 caracteres'
        usuario.nombre = 'Neque porro quisquam est qui dolorem ipsum quia'

        then: 'la validacion debe pasar'
        usuario.validate()
    }

}
