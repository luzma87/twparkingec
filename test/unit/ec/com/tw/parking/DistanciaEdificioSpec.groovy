package ec.com.tw.parking

import ec.com.tw.parking.builders.DistanciaEdificioBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification
import spock.lang.Unroll

@TestFor(DistanciaEdificio)
class DistanciaEdificioSpec extends Specification {
    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def distanciaEdificio = DistanciaEdificioBuilder.nuevo().crear()

        then: 'la validacion debe pasar'
        distanciaEdificio.validate()
        !distanciaEdificio.hasErrors()
    }

    @Unroll
    void "Debe #campo ser no nulo"() {
        setup:
        def distanciaEdificio = DistanciaEdificioBuilder.nuevo().con { d -> d[campo] = null }.crear()

        expect:
        !distanciaEdificio.validate()
        distanciaEdificio.hasErrors()
        distanciaEdificio.errors[campo]?.code == 'nullable'

        where:
        campo << ["codigo", "descripcion"]
    }

    @Unroll
    void "Debe #campo ser no blanco"() {
        setup:
        def distanciaEdificio = DistanciaEdificioBuilder.nuevo().crear()
        distanciaEdificio[campo] = ''

        expect:
        !distanciaEdificio.validate()
        distanciaEdificio.hasErrors()
        distanciaEdificio.errors[campo]?.code == 'blank'

        where:
        campo << ["codigo", "descripcion"]
    }

    @Unroll
    void "Debe #campo tener mas o igual que #minSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, minSize, false)
        def distanciaEdificio = DistanciaEdificioBuilder.nuevo().con { d -> d[campo] = valor }.crear()

        expect:
        !distanciaEdificio.validate()
        distanciaEdificio.hasErrors()
        distanciaEdificio.errors[campo]?.code == 'minSize.notmet'

        where:
        campo         | minSize
        "descripcion" | 3
    }

    @Unroll
    void "Debe #campo tener menos o igual que #maxSize caracteres"() {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(maxSize + 1, 100, false)
        def distanciaEdificio = DistanciaEdificioBuilder.nuevo().con { d -> d[campo] = valor }.crear()

        expect:
        !distanciaEdificio.validate()
        distanciaEdificio.hasErrors()
        distanciaEdificio.errors[campo]?.code == 'maxSize.exceeded'

        where:
        campo         | maxSize
        "codigo"      | 1
        "descripcion" | 10
    }

    void "Debe toString devolver la descripcion"() {
        setup:
        def distanciaEdificio = DistanciaEdificioBuilder.nuevo().crear()

        expect:
        distanciaEdificio.toString() == distanciaEdificio.descripcion
    }
}
