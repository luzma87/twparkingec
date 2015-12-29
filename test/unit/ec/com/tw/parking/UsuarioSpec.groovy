package ec.com.tw.parking

import ec.com.tw.parking.builders.UsuarioBuilder
import ec.com.tw.parking.helpers.RandomUtilsHelpers
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Usuario)
class UsuarioSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def usuario = new UsuarioBuilder().crear()

        then: 'la validacion debe pasar'
        usuario.validate()
        !usuario.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def usuarioBuilder = new UsuarioBuilder()
        usuarioBuilder[campo] = null
        def usuario = usuarioBuilder.crear()

        expect:
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors[campo]?.code == 'nullable'

        where:
        campo << ["nombre", "email", "password", "esAdmin"]
    }

    void "Debe ser no blanco"(campo) {
        setup:
        def usuarioBuilder = new UsuarioBuilder()
        def usuario = usuarioBuilder.crear()
        usuario[campo] = ""

        expect:
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors[campo]?.code == 'blank'

        where:
        campo << ["nombre", "email", "password"]
    }

    void "Debe tener mas o igual del minimo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, campo.minSize, false)
        def usuarioBuilder = new UsuarioBuilder()
        usuarioBuilder[campo.nombre] = valor
        def usuario = usuarioBuilder.crear();

        expect:
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors[campo.nombre]?.code == 'minSize.notmet'

        where:
        campo << [
            [nombre: "nombre", minSize: 3]
        ]
    }

    void "Debe tener menos o igual el maximo de caracteres"(campo) {
        setup:
        def valor
        if (campo.nombre == "email") {
            valor = RandomUtilsHelpers.getRandomMail(campo.maxSize + 1, 850)
        } else {
            valor = RandomUtilsHelpers.getRandomString(campo.maxSize + 1, 850, false)
        }
        def usuarioBuilder = new UsuarioBuilder()
        usuarioBuilder[campo.nombre] = valor
        def usuario = usuarioBuilder.crear();

        expect:
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors[campo.nombre]?.code == 'maxSize.exceeded'

        where:
        campo << [
            [nombre: "nombre", maxSize: 50],
            [nombre: "email", maxSize: 100],
            [nombre: "password", maxSize: 512]
        ]
    }

    void "Debe el email tener el formato correcto"() {
        when: 'el email tiene un formato incorrecto'
        def usuarioBuilder = new UsuarioBuilder()
        usuarioBuilder.email = RandomUtilsHelpers.getRandomString(1, 100, false)
        def usuario = usuarioBuilder.crear();

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['email']?.code == 'email.invalid'
    }

    void "Debe toString devolver el nombre"() {
        setup:
        def usuario = new UsuarioBuilder().crear()

        expect:
        usuario.toString() == usuario.nombre
    }
}
