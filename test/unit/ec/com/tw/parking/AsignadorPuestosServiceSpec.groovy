package ec.com.tw.parking

import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomInt
import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

@TestFor(AsignadorPuestosService)
@Mock([CalculadorCuotaService, Edificio])
class AsignadorPuestosServiceSpec extends Specification {

//    List<AsignacionPuesto> listaAsignaciones = []
//
//    def setup() {
//        10.times {
//            listaAsignaciones.add(new AsignacionPuestoBuilder().crear())
//        }
//    }

    def "Debe retornar mapa con destinatarios admin y mensaje de alerta si la cantidad de usuario supera la cantidad de puestos"() {
        given:
        def cantidadPuestos = getRandomInt(1, 10)
        def puestosFaltantes = getRandomInt(1, 10)
        def cantidadUsuarios = cantidadPuestos + puestosFaltantes
        def mensajes = construirMensajes(puestosFaltantes, cantidadUsuarios, cantidadPuestos)
        def mensajeFactoryServiceMock = mockMensajeFactoryService()

        when:
        def mapaEsperado = obtenerMatrizMensajeEsperadoDestinatarios(cantidadUsuarios, cantidadPuestos, mensajes.mensaje)
        def respuesta = service.asignarPuestos()

        then:
        1 * mensajeFactoryServiceMock.construirMensaje(_) >> mensajes.mensajeMock
        respuesta.destinatarios.properties == mapaEsperado.destinatarios.properties
        respuesta.mensaje == mapaEsperado.mensaje
    }

    private mockMensajeFactoryService() {
        MensajeFactoryService mensajeFactoryServiceMock = Mock(MensajeFactoryService)
        service.mensajeFactoryService = mensajeFactoryServiceMock
        return mensajeFactoryServiceMock
    }

    private construirMensajes(puestosFaltantes, cantidadUsuarios, cantidadPuestos) {
        def mensajeEsperado = "Faltan $puestosFaltantes puestos: se necesitan $cantidadUsuarios " +
            "y solamente existen $cantidadPuestos. "
        def mensajeEsperadoDelMock = getRandomString(10, 100, false)
        def mensaje = mensajeEsperado + mensajeEsperadoDelMock

        return [mensaje: mensaje, mensajeMock: mensajeEsperadoDelMock]
    }

    private obtenerMatrizMensajeEsperadoDestinatarios(cantidadUsuarios, cantidadPuestos, mensaje) {
        def usuariosAdmin = []
        cantidadUsuarios.times {
            usuariosAdmin += new UsuarioBuilder().crear()
        }
        GroovyMock(Puesto, global: true)
        Puesto.count() >> cantidadPuestos
        GroovyMock(Usuario, global: true)
        Usuario.countByEstaActivo(true) >> cantidadUsuarios
        Usuario.findAllByEsAdmin(true) >> usuariosAdmin

        return [
            destinatarios: usuariosAdmin,
            mensaje      : mensaje
        ]
    }

//    @Ignore
//    def "Debe devolver una lista con todas las asignaciones de puesto"() {
//        when:
//        GroovyMock(AsignacionPuesto, global: true)
//        AsignacionPuesto.list() >> listaAsignaciones
//        def respuesta = service.obtenerTodasAsignaciones()
//
//        then:
//        respuesta == listaAsignaciones
//    }
//
//    def "Debe retornar un mapa con claves de distancia edificio"() {
//        setup:
//        def distancia1 = new DistanciaEdificioBuilder()
//        def distancia2 = new DistanciaEdificioBuilder()
//        def distancia3 = new DistanciaEdificioBuilder()
//        def mapa = [:]
//        def lista1 = crearListaPorDistanciaEdificio(getRandomInt(1, 15), distancia1.crear())
//        def lista2 = crearListaPorDistanciaEdificio(getRandomInt(1, 15), distancia2.crear())
//        def lista3 = crearListaPorDistanciaEdificio(getRandomInt(1, 15), distancia3.crear())
//        mapa.put(distancia1.params.codigo, lista1)
//        mapa.put(distancia2.params.codigo, lista2)
//        mapa.put(distancia3.params.codigo, lista3)
//
//        expect:
//        service.obtenerMapaAsignacionPorDistanciaEdificio(lista1 + lista2 + lista3) == mapa
//    }
//
//    def "Debe retornar la asignacion con fecha minima de una lista de asignaciones"() {
//        setup:
//        def asignacionConFechaMinima = listaAsignaciones.min { it.fechaAsignacion }
//
//        expect:
//        service.obtenerAsignacionConFechaMinima(listaAsignaciones) == asignacionConFechaMinima
//    }
//
//    @Ignore
//    def "Debe devolver una lista de tipo transicion"() {
//        setup:
//        def listaTipoTransiciones = []
//        3.times {
//            listaTipoTransiciones.add(new TipoTransicionBuilder().crear())
//        }
//
//        when:
//        GroovyMock(TipoTransicion, global: true)
//        TipoTransicion.list() >> listaTipoTransiciones
//        def respuesta = service.obtenerTodosTiposTransicion()
//
//        then:
//        respuesta == listaTipoTransiciones
//    }
//
//    @Ignore
//    def "Debe retornar la cantidad total de puestos"() {
//        setup:
//        def cant = getRandomInt(100)
//
//        when:
//        GroovyMock(Puesto, global: true)
//        Puesto.count() >> cant
//        def respuesta = service.obtenerCantidadTotalPuestos()
//
//        then:
//        respuesta == cant
//    }
//
//    @Ignore
//    def "Debe retornar la cantidad de usuarios activos"() {
//        setup:
//        def cant = getRandomInt(100)
//
//        when:
//        GroovyMock(Usuario, global: true)
//        Usuario.countByEstaActivo(true) >> cant
//        def respuesta = service.obtenerCantidadTotalUsuarios()
//
//        then:
//        respuesta == cant
//    }
//
//    private List<AsignacionPuesto> crearListaPorDistanciaEdificio(cantidad, distancia) {
//        def lista = []
//        cantidad.times {
//            def asignacionPuestoBuilder = new AsignacionPuestoBuilder()
//            asignacionPuestoBuilder.puesto.edificio.distancia = distancia
//            def asignacion = asignacionPuestoBuilder.crear()
//            lista += asignacion
//        }
//        return lista
//    }

}