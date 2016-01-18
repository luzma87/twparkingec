package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static RandomUtilsHelpers.getRandomInt
import static RandomUtilsHelpers.getRandomString

@TestFor(GeneradorNotificacionesService)
@Mock([CalculadorCuotaService, AsignadorPuestosService, Edificio, TipoPreferencia, Usuario, Auto])
class GeneradorNotificacionesServiceSpec extends Specification {

    static final CASO_MAS_USUARIOS_QUE_PUESTOS = 0
    static final CASO_EXITO = 1

    def mensajeFactoryServiceMock

    def setup() {
        mensajeFactoryServiceMock = mockMensajeFactoryService()
    }

    def "Debe retornar mapa con destinatarios, mensaje de alerta, y asunto: caso mas usuarios que puestos"() {
        given:
        def mapaEsperado = obtenerMatrizMensajeEsperadoDestinatarios(CASO_MAS_USUARIOS_QUE_PUESTOS)

        when:
        def respuesta = service.generarNotificacion()

        then:
        1 * mensajeFactoryServiceMock.construirMensajePuestosFaltantes(_) >> mapaEsperado.mensajeMock
        respuesta.destinatarios.nombre == mapaEsperado.destinatarios.nombre
        respuesta.asunto == mapaEsperado.asunto
        respuesta.mensaje == mapaEsperado.mensaje
    }

    def "Debe retornar mapa con destinatarios, mensaje de alerta, y asunto: caso de exito"() {
        given:
        def cantidadAsignaciones = getRandomInt(5, 15)
        def cantidadAutos = cantidadAsignaciones + getRandomInt(1, 5)
        mocksUsuariosAutosAsignaciones(cantidadAsignaciones, cantidadAutos)
        def mapaEsperado = obtenerMatrizMensajeEsperadoDestinatarios(CASO_EXITO)

        when:
        def respuesta = service.generarNotificacion()

        then:
        0 * mensajeFactoryServiceMock.construirMensajePuestosFaltantes(_)
        1 * mensajeFactoryServiceMock.construirMensajeExito() >> mapaEsperado.mensajeMock
        respuesta.destinatarios.properties == mapaEsperado.destinatarios.properties
        respuesta.asunto == mapaEsperado.asunto
        respuesta.mensaje == mapaEsperado.mensaje
    }

    def """Debe llamar a asignador puestos usuarios no salen en el caso de exito
            si cantidad autos no salen es mayor que sus asignaciones"""() {
        given:
        def cantidadAsignaciones = getRandomInt(5, 15)
        def cantidadAutos = cantidadAsignaciones + getRandomInt(1, 5)
        mocksUsuariosAutosAsignaciones(cantidadAsignaciones, cantidadAutos)

        when:
        service.generarNotificacion()

        then:
        1 * service.asignadorPuestosService.asignarPuestosNoSalen(_, _)
    }

    def """Debe llamar a asignador puestos usuarios salen en el caso de exito
           si cantidad autos no salen NO es mayor que sus asignaciones"""() {
        given:
        def cantidadAsignaciones = getRandomInt(5, 15)
        def cantidadAutos = cantidadAsignaciones
        mocksUsuariosAutosAsignaciones(cantidadAsignaciones, cantidadAutos)

        when:
        service.generarNotificacion()

        then:
        0 * service.asignadorPuestosService.asignarPuestosNoSalen(_, _)
        1 * service.asignadorPuestosService.asignarPuestosSalen(_)
    }

    private mocksUsuariosAutosAsignaciones(cantidadAsignaciones, cantidadAutos) {
        AsignadorPuestosService asignadorPuestosServiceMock = Mock(AsignadorPuestosService)
        service.asignadorPuestosService = asignadorPuestosServiceMock
        def asignacionesUsuariosNoSalen = AsignacionPuestoBuilder.lista(cantidadAsignaciones)
        def autosNoSalen = asignacionesUsuariosNoSalen.auto
        if (cantidadAutos > cantidadAsignaciones) {
            autosNoSalen += AutoBuilder.lista(cantidadAutos - cantidadAsignaciones)
        }
        def usuariosNoSalen = autosNoSalen.usuario
        GroovyMock(Usuario, global: true)
        Usuario.findAllByPreferencia(TipoPreferencia.findByCodigo('N')) >> usuariosNoSalen
        GroovyMock(Auto, global: true)
        Auto.findAllByUsuarioInList(usuariosNoSalen) >> autosNoSalen
        GroovyMock(AsignacionPuesto, global: true)
        AsignacionPuesto.findAllByAutoInList(autosNoSalen) >> asignacionesUsuariosNoSalen
    }

    private mockMensajeFactoryService() {
        MensajeFactoryService mensajeFactoryServiceMock = Mock(MensajeFactoryService)
        service.mensajeFactoryService = mensajeFactoryServiceMock
        return mensajeFactoryServiceMock
    }

    private construirMensaje(puestosFaltantes, cantidadUsuarios, cantidadPuestos) {
        def mensajeEsperado = "Faltan $puestosFaltantes puestos: se necesitan $cantidadUsuarios " +
            "y solamente existen $cantidadPuestos. "
        def mensajeEsperadoDelMock = getRandomString(10, 100, false)
        if (puestosFaltantes == 0) {
            mensajeEsperado = "Se han asignado los nuevos puestos de parqueo para este mes. La nueva organización es la siguiente: "
        }
        def mensaje = mensajeEsperado + mensajeEsperadoDelMock

        return [mensaje: mensaje, mensajeMock: mensajeEsperadoDelMock]
    }

    private obtenerMatrizMensajeEsperadoDestinatarios(caso) {
        def cantidadPuestos = getRandomInt(1, 10)
        def puestosFaltantes = getRandomInt(1, 10)
        def asunto = "ALERTA: puestos faltantes"
        if (caso == CASO_EXITO) {
            puestosFaltantes = 0
            asunto = "Nueva organización de parqueaderos"
        }
        def cantidadUsuarios = cantidadPuestos + puestosFaltantes
        def mensajes = construirMensaje(puestosFaltantes, cantidadUsuarios, cantidadPuestos)

        def usuarios = mockPuestoUsuario(cantidadUsuarios, cantidadPuestos, caso)

        return [
            destinatarios: usuarios,
            asunto       : asunto,
            mensaje      : mensajes.mensaje,
            mensajeMock  : mensajes.mensajeMock
        ]
    }

    private ArrayList mockPuestoUsuario(cantidadUsuarios, cantidadPuestos, caso) {
        def usuarios = []
        cantidadUsuarios.times {
            usuarios += UsuarioBuilder.nuevo().crear()
        }
        GroovyMock(Puesto, global: true)
        Puesto.count() >> cantidadPuestos
        GroovyMock(Usuario, global: true)
        Usuario.countByEstaActivo(true) >> cantidadUsuarios
        if (caso == CASO_EXITO) {
            Usuario.findAllByEstaActivo(true) >> usuarios
        } else {
            Usuario.findAllByEsAdminAndEstaActivo(true, true) >> usuarios
        }
        return usuarios
    }
}