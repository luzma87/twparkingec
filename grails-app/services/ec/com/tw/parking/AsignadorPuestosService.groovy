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
            def asignacionesUsuariosPreferenciaSaleEdificioMatriz = AsignacionPuesto.withCriteria {
                puesto {
                    eq("edificio", edificioMatriz)
                }
                auto {
                    usuario {
                        eq("tipoPreferencia", preferenciaSale)
                    }
                }
                isNull("fechaLiberacion")
            }

            def max = asignacionesUsuariosPreferenciaSaleEdificioMatriz.size() - 1
            def random = new Random()
            puestosNecesarios.times {
                def posicion = random.nextInt(max)
                def asignacion = asignacionesUsuariosPreferenciaSaleEdificioMatriz[posicion]

                while (asignacion.fechaLiberacion != null) {
                    posicion = random.nextInt(max)
                    asignacion = asignacionesUsuariosPreferenciaSaleEdificioMatriz[posicion]
                }

                asignacion.fechaLiberacion = new Date()
                autosEnEspera += asignacion.auto
            }

            puestosLibresEdificioMatriz = edificioMatriz.puestosLibres
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
}
