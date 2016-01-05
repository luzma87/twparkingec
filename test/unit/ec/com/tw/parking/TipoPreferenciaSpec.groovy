package ec.com.tw.parking

import ec.com.tw.parking.builders.TipoPreferenciaBuilder
import ec.com.tw.parking.helpers.RandomUtilsHelpers
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(TipoPreferencia)
class TipoPreferenciaSpec extends Specification {
    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def tipoPreferencia = new TipoPreferenciaBuilder().crear()

        then: 'la validacion debe pasar'
        tipoPreferencia.validate()
        !tipoPreferencia.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def tipoPreferenciaBuilder = new TipoPreferenciaBuilder()
        tipoPreferenciaBuilder[campo] = null
        def tipoPreferencia = tipoPreferenciaBuilder.crear()

        expect:
        !tipoPreferencia.validate()
        tipoPreferencia.hasErrors()
        tipoPreferencia.errors[campo]?.code == 'nullable'

        where:
        campo << ["codigo", "descripcion"]
    }

    void "Debe ser no blanco"(campo) {
        setup:
        def tipoPreferenciaBuilder = new TipoPreferenciaBuilder()
        def tipoPreferencia = tipoPreferenciaBuilder.crear()
        tipoPreferencia[campo] = ""

        expect:
        !tipoPreferencia.validate()
        tipoPreferencia.hasErrors()
        tipoPreferencia.errors[campo]?.code == 'blank'

        where:
        campo << ["codigo", "descripcion"]
    }

    void "Debe tener mas o igual del minimo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, campo.minSize, false)
        def tipoPreferenciaBuilder = new TipoPreferenciaBuilder()
        tipoPreferenciaBuilder[campo.nombre] = valor
        def tipoPreferencia = tipoPreferenciaBuilder.crear()

        expect:
        !tipoPreferencia.validate()
        tipoPreferencia.hasErrors()
        tipoPreferencia.errors[campo.nombre]?.code == 'minSize.notmet'

        where:
        campo << [
            [nombre: "descripcion", minSize: 3]
        ]
    }

    void "Debe tener menos o igual que el maximo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(campo.maxSize + 1, 100, false)
        def tipoPreferenciaBuilder = new TipoPreferenciaBuilder()
        tipoPreferenciaBuilder[campo.nombre] = valor
        def tipoPreferencia = tipoPreferenciaBuilder.crear()

        expect:
        !tipoPreferencia.validate()
        tipoPreferencia.hasErrors()
        tipoPreferencia.errors[campo.nombre]?.code == 'maxSize.exceeded'

        where:
        campo << [
            [nombre: "codigo", maxSize: 1],
            [nombre: "descripcion", maxSize: 10]
        ]
    }

    void "Debe toString devolver la descripcion"() {
        setup:
        def tipoPreferencia = new TipoPreferenciaBuilder().crear()

        expect:
        tipoPreferencia.toString() == tipoPreferencia.descripcion
    }
}
