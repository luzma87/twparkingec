package ec.com.tw.parking

import ec.com.tw.parking.builders.DistanciaEdificioBuilder
import grails.test.mixin.TestFor
import spock.lang.Specification

@TestFor(DistanciaEdificio)
class DistanciaEdificioSpec extends Specification {
    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'
        def distanciaEdificio = new DistanciaEdificioBuilder().crear()

        then: 'la validacion debe pasar'
        distanciaEdificio.validate()
        !distanciaEdificio.hasErrors()
    }

    void "Debe ser no nulo"(campo) {
        setup:
        def distanciaEdificioBuilder = new DistanciaEdificioBuilder()
        distanciaEdificioBuilder[campo] = null
        def distanciaEdificio = distanciaEdificioBuilder.crear()

        expect:
        !distanciaEdificio.validate()
        distanciaEdificio.hasErrors()
        distanciaEdificio.errors[campo]?.code == 'nullable'

        where:
        campo << ["codigo", "descripcion"]
    }

    void "Debe ser no blanco"(campo) {
        setup:
        def distanciaEdificioBuilder = new DistanciaEdificioBuilder()
        def distanciaEdificio = distanciaEdificioBuilder.crear()
        distanciaEdificio[campo] = ""

        expect:
        !distanciaEdificio.validate()
        distanciaEdificio.hasErrors()
        distanciaEdificio.errors[campo]?.code == 'blank'

        where:
        campo << ["codigo", "descripcion"]
    }

    void "Debe tener mas o igual del minimo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(1, campo.minSize, false)
        def distanciaEdificioBuilder = new DistanciaEdificioBuilder()
        distanciaEdificioBuilder[campo.nombre] = valor
        def distanciaEdificio = distanciaEdificioBuilder.crear()

        expect:
        !distanciaEdificio.validate()
        distanciaEdificio.hasErrors()
        distanciaEdificio.errors[campo.nombre]?.code == 'minSize.notmet'

        where:
        campo << [
            [nombre: "descripcion", minSize: 3]
        ]
    }

    void "Debe tener menos o igual que el maximo de caracteres"(campo) {
        setup:
        def valor = RandomUtilsHelpers.getRandomString(campo.maxSize + 1, 100, false)
        def distanciaEdificioBuilder = new DistanciaEdificioBuilder()
        distanciaEdificioBuilder[campo.nombre] = valor
        def distanciaEdificio = distanciaEdificioBuilder.crear()

        expect:
        !distanciaEdificio.validate()
        distanciaEdificio.hasErrors()
        distanciaEdificio.errors[campo.nombre]?.code == 'maxSize.exceeded'

        where:
        campo << [
            [nombre: "codigo", maxSize: 1],
            [nombre: "descripcion", maxSize: 10]
        ]
    }

    void "Debe toString devolver la descripcion"() {
        setup:
        def distanciaEdificio = new DistanciaEdificioBuilder().crear()

        expect:
        distanciaEdificio.toString() == distanciaEdificio.descripcion
    }
}
