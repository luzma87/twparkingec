package ec.com.tw.parking

import spock.lang.Specification

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomInt

class MensajeFactoryTest extends Specification {

    final ERROR_PUESTO_FALTANTES_SIN_AMPLIABLE = "default.error.puestos.faltantes.sin.ampliable"

//    def "Debe devolver un mensaje de advertencia si la cantidad de edificios ampliables es 0"() {
//        setup:
//        GroovyMock(Edificio, global: true)
//        Edificio.findAllByEsAmpliable(true) >> []
//        def mensajeEsperado = ERROR_PUESTO_FALTANTES_SIN_AMPLIABLE
//        def puestosFaltantes = getRandomInt(1, 15)
//
//        expect:
//        MensajeFactory.construirMensajePuestosFaltantes(puestosFaltantes) == mensajeEsperado
//    }
}
