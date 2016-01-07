package ec.com.tw.parking

class GeneradorNotificacionesService {
    def mensajeFactoryService
    def asignadorPuestosService
    final MENSAJE_ALERTA_PUESTOS_FALTANTES = "ALERTA: puestos faltantes"

    def generarNotificacion() {
        def totalPuestos = Puesto.count()
        def totalUsuariosActivos = Usuario.countByEstaActivo(true)

        if (totalUsuariosActivos > totalPuestos) {
            return generarNotificacionMasUsuariosQuePuestos(totalUsuariosActivos, totalPuestos)
        } else {
//            def preferenciaNoSale = TipoPreferencia.findByCodigo("N")
//            def usuariosNoSalen = Usuario.findAllByPreferencia(preferenciaNoSale)
//            def autosNoSalen = Auto.findAllByUsuarioInList(usuariosNoSalen)
//            def asignacionesUsuariosNoSalen = AsignacionPuesto.findAllByAutoInList(autosNoSalen)
//            if (usuariosNoSalen.size() > asignacionesUsuariosNoSalen.size()) {
//                asignadorPuestosService = new AsignadorPuestosNoSalenService()
//                asignadorPuestosService.asignarPuesto()
//            }
//            asignadorPuestosService = new AsignadorPuestosSalenService()
//            asignadorPuestosService.asignarPuesto()

            //generar notificaion con puestos
        }
    }

//    def obtenerMapaAsignacionPorDistanciaEdificio(List<AsignacionPuesto> asignaciones) {
//        def mapa = [:]
//
//        asignaciones.each { asignacion ->
//            def distancia = asignacion.puesto.edificio.distancia.codigo
//            if (!mapa[distancia]) {
//                mapa[distancia] = []
//            }
//            mapa[distancia] += asignacion
//        }
//        return mapa
//    }
//
//    AsignacionPuesto obtenerAsignacionConFechaMinima(List<AsignacionPuesto> asignaciones) {
//        return asignaciones.min { it.fechaAsignacion }
//    }
    private generarNotificacionMasUsuariosQuePuestos(totalUsuariosActivos, totalPuestos) {
        def puestosFaltantes = totalUsuariosActivos - totalPuestos
        def usuariosAdmin = Usuario.findAllByEsAdmin(true)
        def mensaje = "Faltan $puestosFaltantes puestos: se necesitan $totalUsuariosActivos y solamente existen $totalPuestos. "
        mensaje += mensajeFactoryService.construirMensaje(puestosFaltantes)

        return [
            destinatarios: usuariosAdmin,
            asunto       : MENSAJE_ALERTA_PUESTOS_FALTANTES,
            mensaje      : mensaje
        ]
    }
}
