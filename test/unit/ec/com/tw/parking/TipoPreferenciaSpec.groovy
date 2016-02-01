package ec.com.tw.parking

import ec.com.tw.parking.builders.TipoPreferenciaBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(TipoPreferencia)
class TipoPreferenciaSpec extends Specification {
    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def tipoPreferencia = TipoPreferenciaBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        tipoPreferencia.validate()
        !tipoPreferencia.hasErrors()
    }

    @Unroll
    void "Debe #campo no ser nulo"() {
        setup:
        def tipoPreferencia = TipoPreferenciaBuilder.nuevo().con { d -> d[campo] = null }.crear()

        expect:
        !tipoPreferencia.validate()
        tipoPreferencia.hasErrors()
        tipoPreferencia.errors[campo]?.code == 'nullable'

        where:
        campo << ["codigo", "descripcion"]
    }

    @Unroll
    void "Debe #campo no ser blanco"() {
        setup:
        def tipoPreferencia = TipoPreferenciaBuilder.nuevo().crear()
        tipoPreferencia[campo] = ''

        expect:
        !tipoPreferencia.validate()
        tipoPreferencia.hasErrors()
        tipoPreferencia.errors[campo]?.code == 'blank'

        where:
        campo << ["codigo", "descripcion"]
    }

    @Unroll
    void "Debe #campo tener mas o igual #minSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, minSize, false)
        def tipoPreferencia = TipoPreferenciaBuilder.nuevo().con { d -> d[campo] = valor }.crear()

        expect:
        !tipoPreferencia.validate()
        tipoPreferencia.hasErrors()
        tipoPreferencia.errors[campo]?.code == 'minSize.notmet'

        where:
        campo         | minSize
        "descripcion" | 3
    }

    @Unroll
    void "Debe #campo tener menos o igual que #maxSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(maxSize + 1, 100, false)
        def tipoPreferencia = TipoPreferenciaBuilder.nuevo().con { d -> d[campo] = valor }.crear()

        expect:
        !tipoPreferencia.validate()
        tipoPreferencia.hasErrors()
        tipoPreferencia.errors[campo]?.code == 'maxSize.exceeded'

        where:
        campo         | maxSize
        "codigo"      | 1
        "descripcion" | 10
    }

    void "Debe toString devolver la descripcion"() {
        setup:
        def tipoPreferencia = TipoPreferenciaBuilder.nuevo().crear()

        expect:
        tipoPreferencia.toString() == tipoPreferencia.descripcion
    }
}
