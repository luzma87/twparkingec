package ec.com.tw.parking

import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(Usuario)
@Mock([Auto])
class UsuarioSpec extends Specification {

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def usuario = UsuarioBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        usuario.validate()
        !usuario.hasErrors()
    }

    @Unroll
    void "Debe #campo no ser nulo"() {
        setup:
        def usuario = UsuarioBuilder.nuevo().con { d -> d[campo] = null }.crear()

        expect:
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors[campo]?.code == 'nullable'

        where:
        campo << ["nombre", "email", "password", "esAdmin", "cedula", "estaActivo", "preferencia"]
    }

    @Unroll
    void "Debe #campo ser no blanco"() {
        setup:
        def usuario = UsuarioBuilder.nuevo().crear()
        usuario[campo] = ""

        expect:
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors[campo]?.code == 'blank'

        where:
        campo << ["nombre", "email", "password", "cedula"]
    }

    @Unroll
    void "Debe #campo tener mas o igual que #minSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, minSize, false)
        def usuario = UsuarioBuilder.nuevo().con { d -> d[campo] = valor }.crear()

        expect:
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors[campo]?.code == 'minSize.notmet'

        where:
        campo    | minSize
        "nombre" | 3
        "cedula" | 10
    }

    @Unroll
    void "Debe #campo tener menos o igual que #maxSize caracteres"() {
        setup:
        def valor
        if (campo == "email") {
            valor = RandomUtilsHelpers.getRandomMail(maxSize + 1, 850)
        } else {
            valor = RandomUtilsHelpers.getRandomString(maxSize + 1, 850, false)
        }
        def usuario = UsuarioBuilder.nuevo().con { d -> d[campo] = valor }.crear()

        expect:
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors[campo]?.code == 'maxSize.exceeded'

        where:
        campo      | maxSize
        "nombre"   | 50
        "email"    | 100
        "password" | 512
        "cedula"   | 10
    }

    void "Debe el email tener el formato correcto"() {
        when: 'el email tiene un formato incorrecto'
        def valor = RandomUtilsHelpers.getRandomString(1, 100, false)
        def usuario = UsuarioBuilder.nuevo().con { d -> d.email = valor }.crear()

        then: 'la validacion debe fallar'
        !usuario.validate()
        usuario.hasErrors()
        usuario.errors['email']?.code == 'email.invalid'
    }

    void "Debe tener varios autos"() {
        setup:
        def auto = AutoBuilder.nuevo().crear()
        auto.save()
        def usuario = UsuarioBuilder.nuevo().crear()
        usuario.save()

        expect:
        usuario.addToAutos(auto)
        usuario.autos.size() == 1
    }

    void "Debe toString devolver el nombre"() {
        setup:
        def usuario = UsuarioBuilder.nuevo().crear()

        expect:
        usuario.toString() == usuario.nombre
    }
}
