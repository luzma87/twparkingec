package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomInt

@TestFor(AsignadorPuestosService)
@Mock([AsignacionPuesto, Auto, Puesto, Usuario])
class AsignadorPuestosServiceSpec extends Specification {

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
        Puesto puesto = PuestoBuilder.crearDefault()
        Auto auto = AutoBuilder.crearDefault()
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
        respuesta.fechaAsignacion.format("dd-MM-yyyy HH:mm:ss") == asignacion.fechaAsignacion.format("dd-MM-yyyy HH:mm:ss")
    }

    def "Debe retornar null si no puede guardar la asignacion"() {
        setup:
        Puesto puesto = PuestoBuilder.crearDefault()
        Auto auto = AutoBuilder.crearDefault()
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
}
