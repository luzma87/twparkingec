package ec.com.tw.parking

import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Usuario)
class UsuarioSpec extends Specification {

    def setup() {
    }

    def cleanup() {
    }

    void "No Debe ser el nombre nulo"() {
        when: 'el nombre es nulo'
        def usuario = new Usuario(nombre: null)

        then: 'la validacion debe fallar'
        usuario.validate()
    }

    void "No debe ser el nombre blanco"() {
        when: 'el nombre es blanco'
        def usuario = new Usuario(nombre: '')

        then: 'la validacion debe fallar'
        usuario.validate()
    }

}
