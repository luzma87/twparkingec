package ec.com.tw.parking

import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(Usuario)
@Mock([Auto])
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
        campo << ["nombre", "email", "password", "esAdmin", "cedula", "estaActivo", "preferencia"]
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
        campo << ["nombre", "email", "password", "cedula"]
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
            [nombre: "nombre", minSize: 3],
            [nombre: "cedula", minSize: 10]
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
            [nombre: "password", maxSize: 512],
            [nombre: "cedula", maxSize: 10]
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

    void "Debe tener varios autos"() {
        setup:
        def auto = new AutoBuilder().crear()
        auto.save()
        def usuario = new UsuarioBuilder().crear()
        usuario.save()

        expect:
        usuario.addToAutos(auto)
        usuario.autos.size() == 1
    }

    void "Debe toString devolver el nombre"() {
        setup:
        def usuario = new UsuarioBuilder().crear()

        expect:
        usuario.toString() == usuario.nombre
    }
}
