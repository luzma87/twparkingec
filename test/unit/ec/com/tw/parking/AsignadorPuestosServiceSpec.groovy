package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Ignore
import spock.lang.Specification

import static RandomUtilsHelpers.getRandomInt

@TestFor(AsignadorPuestosService)
@Mock([AsignacionPuesto, Auto, Puesto, Usuario])
class AsignadorPuestosServiceSpec extends Specification {

    public static final String FORMATO_FECHA = "dd-MM-yyyy HH:mm:ss"

    def "Debe retonar los usuarios que no tienen asignacion"() {
        given:
        def usuariosSinAsignacion = UsuarioBuilder.crearLista(getRandomInt(3, 15))
        def asignacionesUsuariosNoSalen = AsignacionPuestoBuilder.crearLista(getRandomInt(1, 15))
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

    def "Debe retornar lista vacia de usuarios en espera cuando existen puestos disponibles en edificio matriz"() {
        setup:
        def cantidadAsignaciones = getRandomInt(1, 15)
        def cantidadUsuariosAdicionales = getRandomInt(2, 5)
        def distancia = DistanciaEdificio.findByCodigo("M")
        List<AsignacionPuesto> asignacionesUsuariosNoSalen = AsignacionPuestoBuilder.crearLista(cantidadAsignaciones)
        List<Usuario> usuariosNoSalen = asignacionesUsuariosNoSalen.auto.usuario + UsuarioBuilder.crearLista(cantidadUsuariosAdicionales)
        def edificio = EdificioBuilder.nuevo().crear()
        def asignacionesLibres = AsignacionPuestoBuilder.crearLista(usuariosNoSalen.size() + getRandomInt(5, 20))
        edificio.puestos = PuestoBuilder.nuevo().crearLista(asignacionesLibres.size() + getRandomInt(5, 25))
        GroovyMock(Edificio, global: true)
        Edificio.findByDistancia(distancia) >> edificio
        GroovyMock(AsignacionPuesto, global: true)
        AsignacionPuesto.findAllByPuestoInList(edificio.puestos) >> asignacionesLibres
        def myService = Spy(AsignadorPuestosService)

        when:
        def respuesta = myService.asignarPuestosNoSalen(usuariosNoSalen, asignacionesUsuariosNoSalen)

        then:
        respuesta == []
        (1.._) * myService.asignarPuestoAUsuario(_, _) >> null
    }

    @Ignore()
    def """Debe remover usuarios sin preferencia de sus asignaciones en edificio matriz y retornar lista de espera
           al asignar usuarios con preferencia no salen"""() {
        def cantidadAsignaciones = getRandomInt(1, 15)
        def cantidadUsuariosAdicionales = getRandomInt(2, 5)
        def distancia = DistanciaEdificio.findByCodigo("M")
        List<AsignacionPuesto> asignacionesUsuariosNoSalen = AsignacionPuestoBuilder.crearLista(cantidadAsignaciones)
        List<Usuario> usuariosNoSalen = asignacionesUsuariosNoSalen.auto.usuario + UsuarioBuilder.crearLista(cantidadUsuariosAdicionales)
        def edificio = EdificioBuilder.nuevo().crear()
        def puestosNecesarios = usuariosNoSalen.size() - asignacionesUsuariosNoSalen.size()
        def asignacionesLibres = AsignacionPuestoBuilder.crearLista(puestosNecesarios - 1)
        edificio.puestos = PuestoBuilder.nuevo().crearLista(asignacionesLibres.size())
        def autosEnEspera = AutoBuilder.crearLista(cantidadUsuariosAdicionales)
        GroovyMock(Edificio, global: true)
        Edificio.findByDistancia(distancia) >> edificio
        GroovyMock(AsignacionPuesto, global: true)
        AsignacionPuesto.findAllByPuestoInList(edificio.puestos) >> asignacionesLibres
        AsignacionPuesto.withCriteria { Map params } >> []
        def myService = Spy(AsignadorPuestosService)

        when:
        def respuesta = myService.asignarPuestosNoSalen(usuariosNoSalen, asignacionesUsuariosNoSalen)

        then:
        respuesta == autosEnEspera
        (1.._) * myService.asignarPuestoAUsuario(_, _) >> null
    }
}
