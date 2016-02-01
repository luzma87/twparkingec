package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.DistanciaEdificioBuilder
import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import ec.com.tw.parking.builders.TipoTransicionBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import ec.com.tw.parking.enums.Tamanio
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.IgnoreRest
import spock.lang.Specification
import spock.lang.Unroll

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
        AsignacionPuesto respuesta = service.asignarPuestoAusuario(puesto, usuario)

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
        AsignacionPuesto respuesta = service.asignarPuestoAusuario(puesto, usuario)

        then:
        respuesta == null
    }

    def obtenerAutosEnEspera(puestosNecesarios, Edificio edificio) {
        def asignacionesNoLibres = []
        (puestosNecesarios + getRandomInt(5, 15)).times {
            asignacionesNoLibres += AsignacionPuestoBuilder.nuevo().con { a -> a.puesto.edificio = edificio }.crear()
        }
        def asignacionesNoLibres2 = asignacionesNoLibres.clone()
            .sort { a, b -> b.fechaAsignacion <=> a.fechaAsignacion }
        asignacionesNoLibres2 = asignacionesNoLibres2[0..puestosNecesarios - 1]
        AsignacionPuesto.obtenerOcupadosPorPreferenciaYedificio(_, edificio) >> asignacionesNoLibres
        def respuestaEsperada = []

        asignacionesNoLibres2.eachWithIndex { asignacion, index ->
            respuestaEsperada[index] = [
                distanciaOrigen: asignacion.puesto.edificio.distancia,
                auto           : asignacion.auto
            ]
        }

        return respuestaEsperada
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
        def respuesta = myService.asignarPuestosNoSalen(objetoRespuesta.usuariosNoSalen,
            objetoRespuesta.asignacionesUsuariosNoSalen)

        then:
        respuesta == []
        (1.._) * myService.asignarPuestoAusuario(_, _) >> null
    }

    def """Debe remover usuarios sin preferencia de sus asignaciones en edificio matriz y retornar lista de espera
           al asignar usuarios con preferencia no salen"""() {
        def objetoRespuesta = establecerRespuesta(hayPuestosLibres: false)
        def myService = Spy(AsignadorPuestosService)

        when:
        def respuesta = myService.asignarPuestosNoSalen(objetoRespuesta.usuariosNoSalen,
            objetoRespuesta.asignacionesUsuariosNoSalen)

        then:
        respuesta == objetoRespuesta.autosEnEspera
        (1.._) * myService.asignarPuestoAusuario(_, _) >> null
    }

    def "Debe liberar puestos de una cantidad de usuarios de una prioridad y ponerlos en lista de espera"() {
        setup:
        def cantidadAliberar = getRandomInt(1, 10)
        def autosEnEsperaRecibida = obtenerAutosEnEsperaRecibida()
        def transicionPrioridad = TipoTransicionBuilder.nuevo().crear()

        GroovyMock(TipoTransicion, global: true)
        TipoTransicion.findByPrioridad(transicionPrioridad.prioridad) >> transicionPrioridad

        def distanciaOrigenPrioridad = transicionPrioridad.distanciaOrigen
        def edificio = EdificioBuilder.nuevo().con { e -> e.distancia = distanciaOrigenPrioridad }.crear()
        def puestos = []
        10.times { puestos += PuestoBuilder.nuevo().con { p -> p.edificio = edificio }.crear() }
        def asignacionesPrioridad = []
        puestos.each { puesto ->
            asignacionesPrioridad += AsignacionPuestoBuilder.nuevo()
                .con { a -> a.puesto = puesto }
                .crear()
        }
        GroovyMock(AsignacionPuesto, global: true)
        AsignacionPuesto.obtenerOcupadosPorDistanciaYpreferenciaSale(distanciaOrigenPrioridad) >> asignacionesPrioridad

        def autosPrioridad = []
        cantidadAliberar.times {
            def asignacion = asignacionesPrioridad[it]
            autosPrioridad += [
                auto           : asignacion.auto,
                distanciaOrigen: asignacion.puesto.edificio.distancia
            ]
        }

        def autosEnEsperaEsperada = autosEnEsperaRecibida + autosPrioridad

        when:
        def autosEnEsperaObtenida = service.liberarPuestosPrioridad(autosEnEsperaRecibida,
            transicionPrioridad.prioridad, cantidadAliberar)

        then:
        autosEnEsperaObtenida == autosEnEsperaEsperada
    }

    def """Debe retornar aLiberar cuando
                totalPuestos es mayor que aLiberar y
                liberadosAnterior es 0"""() {
        setup:
        def aLiberar = getRandomInt(1, 10)
        def liberadosAnterior = 0
        def totalPuestos = aLiberar + getRandomInt(1, 10)

        expect:
        service.calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) == aLiberar
    }

    def """Debe retornar totalPuestos cuando
                totalPuestos es menor que aLiberar y
                liberadosAnterior es 0"""() {
        setup:
        def aLiberar = getRandomInt(6, 10)
        def liberadosAnterior = 0
        def totalPuestos = aLiberar - getRandomInt(1, 5)

        expect:
        service.calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) == totalPuestos
    }

    def """Debe retornar totalPuestos cuando
                totalPuestos es igual que aLiberar y
                liberadosAnterior es 0"""() {
        setup:
        def aLiberar = getRandomInt(6, 10)
        def liberadosAnterior = 0
        def totalPuestos = aLiberar

        expect:
        service.calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) == totalPuestos
    }

    def """Debe retornar totalPuestos cuando
                totalPuestos es menor que aLiberar y
                liberadosAnterior es igual que aLiberar"""() {
        setup:
        def aLiberar = getRandomInt(6, 10)
        def liberadosAnterior = aLiberar
        def totalPuestos = aLiberar - getRandomInt(1, 5)

        expect:
        service.calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) == totalPuestos
    }

    def """Debe retornar totalPuestos cuando
                totalPuestos es menor que aLiberar y
                liberadosAnterior es mayor que aLiberar"""() {
        setup:
        def aLiberar = getRandomInt(1, 10)
        def liberadosAnterior = aLiberar + getRandomInt(1, 5)
        def totalPuestos = aLiberar - getRandomInt(1, 5)

        expect:
        service.calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) == totalPuestos
    }


    def """Debe retornar aLiberar cuando
                totalPuestos es mayor que aLiberar y
                liberadosAnterior es igual que aLiberar"""() {
        setup:
        def aLiberar = getRandomInt(1, 10)
        def liberadosAnterior = aLiberar
        def totalPuestos = aLiberar + getRandomInt(1, 5)

        expect:
        service.calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) == aLiberar
    }

    def """Debe retornar aLiberar cuando
                totalPuestos es mayor que aLiberar y
                liberadosAnterior es mayor que aLiberar"""() {
        setup:
        def aLiberar = getRandomInt(1, 10)
        def liberadosAnterior = aLiberar + getRandomInt(1, 5)
        def totalPuestos = aLiberar + getRandomInt(1, 5)

        expect:
        service.calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) == aLiberar
    }

    def """Debe retornar aLiberar + los q faltaron en liberadosAnterior cuando
                totalPuestos es mayor que aLiberar + los faltantes y
                liberadosAnterior es menor que aLiberar"""() {
        setup:
        def aLiberar = getRandomInt(6, 10)
        def liberadosAnterior = aLiberar - getRandomInt(1, 5)
        def totalPuestos = aLiberar + liberadosAnterior + getRandomInt(10)
        def faltantes = aLiberar - liberadosAnterior

        expect:
        service.calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) == aLiberar + faltantes
    }

    @IgnoreRest
    @Unroll
    def """Debe retornar una matriz con las prioridades y la cantidad de puestos a liberar para cada una
            puestos1: #puestos1, puestos2: #puestos2, puestos3: #puestos3, matriz: #matrizEsperada"""() {
        setup:
        inicializarDatosYmocks(puestos1, puestos2, puestos3)

        when:
        def matrizObtenida = service.obtenerCantidadPuestosAliberarPorPrioridad()

        then:
        matrizObtenida == matrizEsperada

        where:
        puestos1                 | puestos2                 | puestos3                 || matrizEsperada
        [ocupados: 2, libres: 0] | [ocupados: 3, libres: 0] | [ocupados: 6, libres: 0] || [1: 2, 2: 2, 3: 2]
        [ocupados: 3, libres: 0] | [ocupados: 2, libres: 0] | [ocupados: 6, libres: 0] || [1: 3, 2: 2, 3: 4]
        [ocupados: 2, libres: 1] | [ocupados: 4, libres: 0] | [ocupados: 6, libres: 0] || [1: 2, 2: 3, 3: 3]
        [ocupados: 3, libres: 0] | [ocupados: 4, libres: 1] | [ocupados: 6, libres: 0] || [1: 3, 2: 2, 3: 3]
        [ocupados: 3, libres: 0] | [ocupados: 4, libres: 0] | [ocupados: 6, libres: 1] || [1: 3, 2: 3, 3: 2]
        [ocupados: 0, libres: 3] | [ocupados: 4, libres: 0] | [ocupados: 6, libres: 0] || [1: 0, 2: 3, 3: 3]
        [ocupados: 0, libres: 3] | [ocupados: 4, libres: 0] | [ocupados: 6, libres: 1] || [1: 0, 2: 3, 3: 2]
        [ocupados: 2, libres: 1] | [ocupados: 4, libres: 1] | [ocupados: 6, libres: 1] || [1: 2, 2: 2, 3: 2]
        [ocupados: 2, libres: 0] | [ocupados: 5, libres: 0] | [ocupados: 8, libres: 0] || [1: 2, 2: 2, 3: 2]
    }

    @Unroll
    def "Debe obtener un puesto adecuado para un auto #tamanioValido"() {
        setup:
        ArrayList<Puesto> puestosValidos = []
        getRandomInt(1, 10).times {
            puestosValidos += PuestoBuilder.nuevo().con { p -> p.tamanio = tamanioValido }.crear()
        }
        Auto auto = AutoBuilder.nuevo().con { a -> a.tamanio = tamanioAuto }.crear()

        expect:
        puestosValidos.contains(service.obtenerPuestoAdecuado(puestosValidos, auto))

        where:
        tamanioAuto      | tamanioValido
        Tamanio.PEQUENIO | Tamanio.PEQUENIO
        Tamanio.MEDIANO  | Tamanio.MEDIANO
        Tamanio.GRANDE   | Tamanio.GRANDE
        Tamanio.PEQUENIO | Tamanio.MEDIANO
        Tamanio.PEQUENIO | Tamanio.GRANDE
        Tamanio.MEDIANO  | Tamanio.GRANDE
    }

    @Unroll
    def "Debe retornar null al intentar obtener un puesto no encontrado para un auto #tamanioInvalido"() {
        setup:
        ArrayList<Puesto> puestosInvalidos = []
        getRandomInt(1, 10).times {
            puestosInvalidos += PuestoBuilder.nuevo().con { p -> p.tamanio = tamanioInvalido }.crear()
        }
        Auto auto = AutoBuilder.nuevo().con { a -> a.tamanio = tamanioAuto }.crear()

        expect:
        service.obtenerPuestoAdecuado(puestosInvalidos, auto) == null

        where:
        tamanioAuto     | tamanioInvalido
        Tamanio.MEDIANO | Tamanio.PEQUENIO
        Tamanio.GRANDE  | Tamanio.PEQUENIO
        Tamanio.GRANDE  | Tamanio.MEDIANO
    }

    @IgnoreRest
    def "Debe ordenar los autos en espera en orden de tamanio decreciente"() {
        setup:
        def cantAutosP = getRandomInt(1, 10)
        def cantAutosM = getRandomInt(1, 10)
        def cantAutosG = getRandomInt(1, 10)
        def autosP = [], autosM = [], autosG = []
        cantAutosP.times {
            autosP += AutoBuilder.nuevo().con { a -> a.tamanio = Tamanio.PEQUENIO }.crear()
        }
        cantAutosM.times {
            autosM += AutoBuilder.nuevo().con { a -> a.tamanio = Tamanio.MEDIANO }.crear()
        }
        cantAutosG.times {
            autosG += AutoBuilder.nuevo().con { a -> a.tamanio = Tamanio.GRANDE }.crear()
        }

        def autos = autosG + autosM + autosP
        def autosEsperados = []
        autos.each { auto ->
            autosEsperados += [
                auto           : auto,
                distanciaOrigen: DistanciaEdificioBuilder.nuevo().crear()
            ]
        }
        def autosInput = autosEsperados.clone()
        Collections.shuffle(autosInput)
        def inicioG = 0
        def finG = inicioG + cantAutosG - 1
        def inicioM = finG + 1
        def finM = inicioM + cantAutosM - 1
        def inicioP = finM + 1
        def finP = inicioP + cantAutosP - 1

        when:
        def respuesta = service.ordenarAutosPorTamanio(autosInput)
        def tamanios = respuesta.auto.tamanio

        then:
        tamanios[inicioG..finG] == [Tamanio.GRANDE] * autosG.size()
        tamanios[inicioM..finM] == [Tamanio.MEDIANO] * autosM.size()
        tamanios[inicioP..finP] == [Tamanio.PEQUENIO] * autosP.size()
    }

    private inicializarDatosYmocks(cantidadPrioridad1, cantidadPrioridad2, cantidadPrioridad3) {
        def distancia1 = DistanciaEdificioBuilder.nuevo().crear()
        def distancia2 = DistanciaEdificioBuilder.nuevo().crear()
        def distancia3 = DistanciaEdificioBuilder.nuevo().crear()

        def tipoTransicion1 = TipoTransicionBuilder.nuevo()
            .con { tt -> tt.distanciaOrigen = distancia1 }
            .con { tt -> tt.distanciaDestino = distancia2 }
            .con { tt -> tt.prioridad = 1 }.crear()
        def tipoTransicion2 = TipoTransicionBuilder.nuevo()
            .con { tt -> tt.distanciaOrigen = distancia2 }
            .con { tt -> tt.distanciaDestino = distancia3 }
            .con { tt -> tt.prioridad = 2 }.crear()
        def tipoTransicion3 = TipoTransicionBuilder.nuevo()
            .con { tt -> tt.distanciaOrigen = distancia3 }
            .con { tt -> tt.distanciaDestino = distancia1 }
            .con { tt -> tt.prioridad = 3 }.crear()

        def asignacionesPrioridad1 = [], asignacionesPrioridad2 = [], asignacionesPrioridad3 = []
        (cantidadPrioridad1.libres + cantidadPrioridad1.ocupados).times {
            asignacionesPrioridad1 += AsignacionPuestoBuilder.nuevo()
                .con { a -> a.puesto.edificio.distancia = distancia1 }
        }
        (cantidadPrioridad2.libres + cantidadPrioridad2.ocupados).times {
            asignacionesPrioridad2 += AsignacionPuestoBuilder.nuevo()
                .con { a -> a.puesto.edificio.distancia = distancia2 }
        }
        (cantidadPrioridad3.libres + cantidadPrioridad3.ocupados).times {
            asignacionesPrioridad3 += AsignacionPuestoBuilder.nuevo()
                .con { a -> a.puesto.edificio.distancia = distancia3 }
        }

        GroovyMock(TipoTransicion, global: true)
        TipoTransicion.list(_) >> [tipoTransicion1, tipoTransicion2, tipoTransicion3]
        GroovyMock(AsignacionPuesto, global: true)
        AsignacionPuesto.contarOcupadosPorPrioridad(1) >> cantidadPrioridad1.ocupados
        AsignacionPuesto.contarLibresPorPrioridad(1) >> cantidadPrioridad1.libres
        AsignacionPuesto.contarOcupadosPorPrioridad(2) >> cantidadPrioridad2.ocupados
        AsignacionPuesto.contarLibresPorPrioridad(2) >> cantidadPrioridad2.libres
        AsignacionPuesto.contarOcupadosPorPrioridad(3) >> cantidadPrioridad3.ocupados
        AsignacionPuesto.contarLibresPorPrioridad(3) >> cantidadPrioridad3.libres
    }

    private establecerRespuesta(opciones) {
        def autosEnEspera = null
        def cantidadPuestos = opciones.hayPuestosLibres ? getRandomInt(5, 15) : 0
        def edificio = EdificioBuilder.nuevo().crear()
        def asignacionesUsuariosNoSalen = AsignacionPuestoBuilder.lista(getRandomInt(1, 15))
        def usuariosNoSalen = asignacionesUsuariosNoSalen.auto.usuario + UsuarioBuilder.lista(getRandomInt(2, 5))
        def cantidadAsignacionesLibres = usuariosNoSalen.size() + (opciones.hayPuestosLibres ?
            getRandomInt(5, 20) :
            -asignacionesUsuariosNoSalen.size())
        if (!opciones.hayPuestosLibres) {
            def puestosNecesarios = usuariosNoSalen.size() - asignacionesUsuariosNoSalen.size()
            autosEnEspera = obtenerAutosEnEspera(puestosNecesarios, edificio)
        }
        def asignacionesLibres = []
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

    private obtenerAutosEnEsperaRecibida() {
        def autosEnEsperaRecibida = []
        def distanciaOrigen = DistanciaEdificioBuilder.nuevo().crear()
        getRandomInt(1, 10).times {
            autosEnEsperaRecibida += [
                auto           : AutoBuilder.nuevo().crear(),
                distanciaOrigen: distanciaOrigen
            ]
        }
        return autosEnEsperaRecibida
    }
}
