package ec.com.tw.parking

import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomInt

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
        setup:
        def mapaEsperado = obtenerMatrizMensajeEsperadoDestinatarios(cantidadEdificios)

        when:
        def respuesta = service.asignarPuestos()

        then:
        respuesta.destinatarios.properties == mapaEsperado.destinatarios.properties
        respuesta.mensaje == mapaEsperado.mensaje

        where:
        cantidadEdificios << [0, getRandomInt(2, 10)/*, 1*/]
    }

    private obtenerMatrizMensajeEsperadoDestinatarios(cantidadEdificios) {
        def cantidadPuestos = getRandomInt(10)
        def cantidadUsuarios = cantidadPuestos + getRandomInt(1, 10)
        def usuariosAdmin = []
        cantidadUsuarios.times {
            usuariosAdmin += new UsuarioBuilder().crear()
        }
        def edificios = []
        cantidadEdificios.times {
            edificios += new EdificioBuilder().crear()
        }
        GroovyMock(Puesto, global: true)
        Puesto.count() >> cantidadPuestos
        GroovyMock(Usuario, global: true)
        Usuario.findAllByEsAdmin(true) >> usuariosAdmin
        Usuario.countByEstaActivo(true) >> cantidadUsuarios
        def puestosFaltantes = cantidadUsuarios - cantidadPuestos;
        def mensajeEsperado = "Faltan $puestosFaltantes puestos: se necesitan $cantidadUsuarios y solamente existen $cantidadPuestos."
        if (cantidadEdificios == 0) {
            mensajeEsperado += " No se encontraron edificios ampliables, no se pudo recalcular la cuota"
        } else if (cantidadEdificios > 1) {
            mensajeEsperado += " Se encontraron ${cantidadEdificios} edificios ampliables, no se pudo recalcular la cuota"
        }
        return [
            destinatarios: usuariosAdmin,
            mensaje      : mensajeEsperado
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