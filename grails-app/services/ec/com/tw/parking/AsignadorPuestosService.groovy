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
            asignarPuestoAusuario(puesto, usuario)
        }
        return autosEnEspera
    }

    def asignarPuestosSalen(autosEnEspera) {
        def puestosAliberarPorPrioridad = obtenerCantidadPuestosAliberarPorPrioridad()
        puestosAliberarPorPrioridad.each { prioridad, puestosAliberar ->
            autosEnEspera = liberarPuestosPrioridad(autosEnEspera, prioridad, puestosAliberar)
        }
        autosEnEspera = ordenarAutosPorTamanio(autosEnEspera)
        autosEnEspera.each { autoEnEspera ->
            Auto auto = autoEnEspera.auto
            DistanciaEdificio distanciaOrigen = autoEnEspera.distanciaOrigen
            DistanciaEdificio distanciaDestino = distanciaOrigen.obtenerDestino()
            def puestosLibres = distanciaDestino.obtenerPuestosLibres()
            Puesto puestoAdecuado = obtenerPuestoAdecuado(puestosLibres, auto)
            if (puestoAdecuado) {
                asignarPuestoAauto(puestoAdecuado, auto)
            } else {
                asignarPuestoFaltante(autoEnEspera)
            }
        }
    }

    def asignarPuestoFaltante(autoSinPuesto) {
        def puestos = Puesto.obtenerLibresPorTamanio(autoSinPuesto.auto.tamanio)
        DistanciaEdificio distanciaOrigen = autoSinPuesto.distanciaOrigen
        DistanciaEdificio distanciaDestino = distanciaOrigen.obtenerDestino()
        DistanciaEdificio nuevaDistanciaDestino = distanciaDestino.obtenerDestino()
        def puestoPosible = puestos.find { it.edificio.distancia == nuevaDistanciaDestino }
        if (!puestoPosible) {
            puestoPosible = puestos.first()
        }
        asignarPuestoAauto(puestoPosible, autoSinPuesto.auto)
    }

    def ordenarAutosPorTamanio(autosEnEspera) {
        return autosEnEspera.sort { -it.auto.tamanio.valor }
    }

    Puesto obtenerPuestoAdecuado(ArrayList<Puesto> puestos, Auto auto) {
        return puestos.find { it.tamanio.valor >= auto.tamanio.valor }
    }

    def asignarPuestoAusuario(Puesto puesto, Usuario usuario) {
        def auto = usuario.autos.find { it.esDefault }
        return asignarPuestoAauto(puesto, auto)
    }

    def asignarPuestoAauto(Puesto puesto, Auto auto) {
        AsignacionPuesto.findAllByAuto(auto).each {
            it.delete()
        }

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
            if (index == 0) {
                objetivoLiberar = totalPuestosPrioridad
                liberadosAnterior = 0
            }
            def aLiberar = objetivoLiberar
            if (totalLibresPrioridad > 0) {
                aLiberar = index == 0 ? totalOcupadosPrioridad : (aLiberar - totalLibresPrioridad)
            }
            def puestosLiberados = calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestosPrioridad)
            liberadosAnterior = puestosLiberados + totalLibresPrioridad
            cantidadPuestosAliberarPorPrioridad.put(prioridad, puestosLiberados)
        }

        return cantidadPuestosAliberarPorPrioridad
    }

    def calcularPuestosLiberados(aLiberar, liberadosAnterior, totalPuestos) {
        if (liberadosAnterior == 0 || liberadosAnterior >= aLiberar) {
            return (totalPuestos > aLiberar) ? aLiberar : totalPuestos
        } else {
            def faltantes = aLiberar - liberadosAnterior
            return aLiberar + faltantes
        }
    }
}
