package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static RandomUtilsHelpers.getRandomInt

@TestFor(AsignadorPuestosService)
@Mock([AsignacionPuesto, Auto, Puesto, Usuario])
class AsignadorPuestosServiceSpec extends Specification {

    public static final String FORMATO_FECHA = "dd-MM-yyyy HH:mm:ss"

    def "Debe retonar los usuarios que no tienen asignacion"() {
        given:
        def usuariosSinAsignacion = UsuarioBuilder.lista(getRandomInt(3, 15))
        def asignacionesUsuariosNoSalen = AsignacionPuestoBuilder.lista(getRandomInt(1, 15))
        def usuariosNoSalen = asignacionesUsuariosNoSalen.auto.usuario + usuariosSinAsignacion

        when:
        def respuesta = service.obtenerUsuariosSinParqueadero(usuariosNoSalen, asignacionesUsuariosNoSalen)

        then:
        respuesta == usuariosSinAsignacion
    }

    def "Debe crear y retornar asignacion de puesto a usuario"() {
        setup:
        Puesto puesto = PuestoBuilder.nuevo().crear()
        Auto auto = AutoBuilder.nuevo().crear()
        Usuario usuario = auto.usuario
        usuario.save()
        auto.esDefault = true
        auto.save()
        puesto.save()

        def builder = new AsignacionPuestoBuilder()
        builder.puesto = puesto
        builder.auto = auto
        def asignacion = builder.crear()

        when:
        AsignacionPuesto respuesta = service.asignarPuestoAUsuario(puesto, usuario)

        then:
        respuesta.puesto.properties == asignacion.puesto.properties
        respuesta.auto.properties == asignacion.auto.properties
        respuesta.fechaAsignacion.format(FORMATO_FECHA) == asignacion.fechaAsignacion.format(FORMATO_FECHA)
    }

    def "Debe retornar null si no puede guardar la asignacion"() {
        setup:
        Puesto puesto = PuestoBuilder.nuevo().crear()
        Auto auto = AutoBuilder.nuevo().crear()
        Usuario usuario = auto.usuario
        usuario.save()
        auto.esDefault = true
        auto.save()
        puesto.save()

        def builder = new AsignacionPuestoBuilder()
        builder.puesto = puesto
        builder.auto = auto
        mockDomain(AsignacionPuesto, [builder.properties])
        AsignacionPuesto.metaClass.save {
            return null
        }

        when:
        AsignacionPuesto respuesta = service.asignarPuestoAUsuario(puesto, usuario)

        then:
        respuesta == null
    }

    private establecerRespuesta(opciones) {
        def asignacionesUsuariosNoSalen = AsignacionPuestoBuilder.lista(getRandomInt(1, 15))
        def usuariosNoSalen = asignacionesUsuariosNoSalen.auto.usuario + UsuarioBuilder.lista(getRandomInt(2, 5))
        def edificio = EdificioBuilder.nuevo().crear()
        def asignacionesLibres = []
        def cantidadAsignacionesLibres = usuariosNoSalen.size()
        def cantidadPuestos = 0
        def autosEnEspera = null
        if (opciones.hayPuestosLibres) {
            cantidadAsignacionesLibres += getRandomInt(5, 20)
            cantidadPuestos = getRandomInt(5, 15)
        } else {
            cantidadAsignacionesLibres -= asignacionesUsuariosNoSalen.size()
            def puestosNecesarios = usuariosNoSalen.size() - asignacionesUsuariosNoSalen.size()
            autosEnEspera = obtenerAutosEnEspera(puestosNecesarios, edificio)
        }
        (cantidadAsignacionesLibres - 1).times {
            asignacionesLibres += AsignacionPuestoBuilder.nuevo().con { a -> a.puesto.edificio = edificio }.crear()
        }
        cantidadPuestos += asignacionesLibres.size()
        edificio.puestos = PuestoBuilder.lista(cantidadPuestos)

        mockEdificioYasignacion(edificio, asignacionesLibres)

        return [
            usuariosNoSalen            : usuariosNoSalen,
            asignacionesUsuariosNoSalen: asignacionesUsuariosNoSalen,
            autosEnEspera              : autosEnEspera
        ]
    }

    def obtenerAutosEnEspera(puestosNecesarios, Edificio edificio) {
        def asignacionesNoLibres = []
        (puestosNecesarios + getRandomInt(5, 15)).times {
            asignacionesNoLibres += AsignacionPuestoBuilder.nuevo().con { a -> a.puesto.edificio = edificio }.crear()
        }
        def asignacionesNoLibres2 = asignacionesNoLibres.clone().sort { a, b -> b.fechaAsignacion <=> a.fechaAsignacion }
        asignacionesNoLibres2 = asignacionesNoLibres2[0..puestosNecesarios - 1]
        AsignacionPuesto.obtenerOcupadosPorPreferenciaYedificio(_, edificio) >> asignacionesNoLibres
        return asignacionesNoLibres2.auto
    }

    def mockEdificioYasignacion(edificio, asignacionesLibres) {
        GroovyMock(Edificio, global: true)
        Edificio.findByDistancia(DistanciaEdificio.findByCodigo("M")) >> edificio
        GroovyMock(AsignacionPuesto, global: true)
        AsignacionPuesto.findAllByPuestoInList(edificio.puestos) >> asignacionesLibres
    }

    def "Debe retornar lista vacia de usuarios en espera cuando existen puestos disponibles en edificio matriz"() {
        setup:
        def objetoRespuesta = establecerRespuesta(hayPuestosLibres: true)
        def myService = Spy(AsignadorPuestosService)

        when:
        def respuesta = myService.asignarPuestosNoSalen(objetoRespuesta.usuariosNoSalen, objetoRespuesta.asignacionesUsuariosNoSalen)

        then:
        respuesta == []
        (1.._) * myService.asignarPuestoAUsuario(_, _) >> null
    }

    def """Debe remover usuarios sin preferencia de sus asignaciones en edificio matriz y retornar lista de espera
           al asignar usuarios con preferencia no salen"""() {
        def objetoRespuesta = establecerRespuesta(hayPuestosLibres: false)
        def myService = Spy(AsignadorPuestosService)

        when:
        def respuesta = myService.asignarPuestosNoSalen(objetoRespuesta.usuariosNoSalen, objetoRespuesta.asignacionesUsuariosNoSalen)

        then:
        respuesta == objetoRespuesta.autosEnEspera
        (1.._) * myService.asignarPuestoAUsuario(_, _) >> null
    }

}
