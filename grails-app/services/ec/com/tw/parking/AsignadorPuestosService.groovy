package ec.com.tw.parking

import grails.transaction.Transactional

@Transactional
class AsignadorPuestosService {
    def asignarPuestos() {

    }

    List<AsignacionPuesto> obtenerTodasAsignaciones() {
        return AsignacionPuesto.list()
    }

    def obtenerMapaAsignacionPorDistanciaEdificio(List<AsignacionPuesto> asignaciones) {
        def mapa = [:]

        asignaciones.each { asignacion ->
            def distancia = asignacion.puesto.edificio.distancia.codigo
            if (!mapa[distancia]) {
                mapa[distancia] = []
            }
            mapa[distancia] += asignacion
        }
        return mapa
    }

    AsignacionPuesto obtenerAsignacionConFechaMinima(List<AsignacionPuesto> asignaciones) {
        return asignaciones.min { it.fechaAsignacion }
    }

    List<TipoTransicion> obtenerTodosTiposTransicion() {
        return TipoTransicion.list()
    }
}
