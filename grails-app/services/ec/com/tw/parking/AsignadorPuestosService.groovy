package ec.com.tw.parking

class AsignadorPuestosService {

    def asignarPuestosNoSalen(List<Usuario> usuariosNoSalen, List<AsignacionPuesto> asignacionesUsuariosNoSalen) {
        def puestosNecesarios = usuariosNoSalen.size() - asignacionesUsuariosNoSalen.size()
        def distanciaMatriz = DistanciaEdificio.findByCodigo("M")
        def edificioMatriz = Edificio.findByDistancia(distanciaMatriz)
        def puestosLibresEdificioMatriz = edificioMatriz.puestosLibres

        def autosEnEspera = []

        if (puestosLibresEdificioMatriz.size() < puestosNecesarios) {
            def preferenciaSale = TipoPreferencia.findByCodigo("S")
            def asignacionesUsuariosPreferenciaSaleEdificioMatriz =
                AsignacionPuesto.obtenerOcupadosPorPreferenciaYedificio(preferenciaSale, edificioMatriz)
                    .sort { a, b -> b.fechaAsignacion <=> a.fechaAsignacion }

            puestosNecesarios.times {
                def asignacion = asignacionesUsuariosPreferenciaSaleEdificioMatriz.remove(0)
                // TODO: prueba de integracion: debe correr liberar al correr esta funcion
                asignacion.liberar()
                autosEnEspera += [
                    auto           : asignacion.auto,
                    distanciaOrigen: asignacion.puesto.edificio.distancia
                ]
                puestosLibresEdificioMatriz += asignacion.puesto
            }
        }
        def usuariosSinAsignacion = obtenerUsuariosSinParqueadero(usuariosNoSalen, asignacionesUsuariosNoSalen)
        usuariosSinAsignacion.each { usuario ->
            def puesto = puestosLibresEdificioMatriz.remove(0)
            asignarPuestoAUsuario(puesto, usuario)
        }
        return autosEnEspera
    }

    def asignarPuestosSalen(autosEnEspera) {

    }

    def asignarPuestoAUsuario(Puesto puesto, Usuario usuario) {
        def auto = usuario.autos.find { it.esDefault }

        def asignacion = new AsignacionPuesto()
        asignacion.auto = auto
        asignacion.puesto = puesto
        asignacion.fechaAsignacion = new Date()
        return asignacion.save()
    }

    def obtenerUsuariosSinParqueadero(List<Usuario> usuariosNoSalen, List<AsignacionPuesto> asignacionesUsuariosNoSalen) {
        def usuariosConAsignacion = asignacionesUsuariosNoSalen.auto.usuario
        def usuariosEnAmbasListas = usuariosConAsignacion.intersect(usuariosNoSalen)
        def usuariosSinAsignacion = usuariosConAsignacion.plus(usuariosNoSalen)
        usuariosSinAsignacion.removeAll(usuariosEnAmbasListas)
        return usuariosSinAsignacion
    }

    def liberarPuestosPrioridad(autosEnEspera, prioridad, cantidadAliberar) {
        def transicionPrioridad = TipoTransicion.findByPrioridad(prioridad)
        def distanciaOrigenPrioridad = transicionPrioridad.distanciaOrigen
        def asignacionesPrioridad = AsignacionPuesto.obtenerPorDistancia(distanciaOrigenPrioridad)

        cantidadAliberar.times {
            def asignacion = asignacionesPrioridad[it]
            // TODO: prueba de integracion: debe correr liberar al correr esta funcion
            asignacion.liberar()
            autosEnEspera += [
                auto           : asignacion.auto,
                distanciaOrigen: distanciaOrigenPrioridad
            ]
        }
        return autosEnEspera
    }

    def obtenerCantidadPuestosAliberarPorPrioridad() {
        def cantidadPuestosAliberarPorPrioridad = [:]
        def transiciones = TipoTransicion.list([sort: "prioridad"])

        def liberadosAnterior = 0
        def objetivoLiberar = -1
        transiciones.eachWithIndex { transicion, index ->
            def prioridad = transicion.prioridad
            def totalOcupadosPrioridad = AsignacionPuesto.contarOcupadosPorPrioridad(prioridad)
            def totalLibresPrioridad = AsignacionPuesto.contarLibresPorPrioridad(prioridad)
            def totalPuestosPrioridad = totalOcupadosPrioridad + totalLibresPrioridad
            println "*************dd******* Prioridad $prioridad *****************************"
            println "objetivo liberar: " + objetivoLiberar
            println "total ocupados: " + totalOcupadosPrioridad
            println "total libres: " + totalLibresPrioridad
            println "total puestos: " + totalPuestosPrioridad
            if (index == 0) {
                objetivoLiberar = totalPuestosPrioridad
                liberadosAnterior = 0
                println "\tobjetivo liberar: " + objetivoLiberar
                println "\tliberados anterior: " + liberadosAnterior
            }
            def aLiberar = objetivoLiberar
            println "a liberar: " + aLiberar
            if (totalLibresPrioridad > 0) {
                aLiberar = index == 0 ? totalOcupadosPrioridad : (aLiberar - totalLibresPrioridad)
                println "\ta liberar: " + aLiberar
            }
            def puestosLiberados = calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestosPrioridad)
            liberadosAnterior = puestosLiberados + totalLibresPrioridad
            println "puestos liberados: " + puestosLiberados
            println "liberados anterior: " + liberadosAnterior
            cantidadPuestosAliberarPorPrioridad.put(prioridad, puestosLiberados)
        }

        return cantidadPuestosAliberarPorPrioridad
    }

    def calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) {
        println "\t\taLiberar: $aLiberar, liberadosAnterior: $liberadosAnterior, totalPuestos: $totalPuestos"
        if (liberadosAnterior == 0 || liberadosAnterior >= aLiberar) {
            return (totalPuestos > aLiberar) ? aLiberar : totalPuestos
        } else {
            def faltantes = aLiberar - liberadosAnterior
            return aLiberar + faltantes
        }
    }
}
