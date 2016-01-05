package ec.com.tw.parking

import grails.transaction.Transactional

@Transactional
class AsignadorPuestosService {
    def asignarPuestos() {
        def totalPuestos = Puesto.count()
        def totalUsuariosActivos = Usuario.countByEstaActivo(true)

        if (totalUsuariosActivos > totalPuestos) {
            def puestosFaltantes = totalUsuariosActivos - totalPuestos
            def usuariosAdmin = Usuario.findAllByEsAdmin(true)
            def mensaje = "Faltan $puestosFaltantes puestos: se necesitan $totalUsuariosActivos y solamente existen $totalPuestos."
            return [
                destinatarios: usuariosAdmin,
                mensaje      : mensaje
            ]
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
}
