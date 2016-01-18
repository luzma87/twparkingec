package ec.com.tw.parking

class GeneradorNotificacionesService {
    def mensajeFactoryService
    def asignadorPuestosService

    static final MENSAJE_ALERTA_PUESTOS_FALTANTES = "ALERTA: puestos faltantes"
    static final MENSAJE_EXITO = "Nueva organización de parqueaderos"

    def generarNotificacion() {
        def totalPuestos = Puesto.count()
        def totalUsuariosActivos = Usuario.countByEstaActivo(true)

        if (totalUsuariosActivos > totalPuestos) {
            return generarNotificacionMasUsuariosQuePuestos(totalUsuariosActivos, totalPuestos)
        } else {
            def preferenciaNoSale = TipoPreferencia.findByCodigo("N")
            def usuariosNoSalen = Usuario.findAllByPreferencia(preferenciaNoSale)
            def autosNoSalen = Auto.findAllByUsuarioInList(usuariosNoSalen)
            def asignacionesUsuariosNoSalen = AsignacionPuesto.findAllByAutoInList(autosNoSalen)
            def autosEnEspera = []
            if (usuariosNoSalen.size() > asignacionesUsuariosNoSalen.size()) {
                autosEnEspera = asignadorPuestosService.asignarPuestosNoSalen(usuariosNoSalen, asignacionesUsuariosNoSalen)
            }
            asignadorPuestosService.asignarPuestosSalen(autosEnEspera)

            return generadorNotificacionExito()
        }
    }

    private generadorNotificacionExito() {
        def usuarios = Usuario.findAllByEstaActivo(true)
        def mensaje = "Se han asignado los nuevos puestos de parqueo para este mes. " +
            "La nueva organización es la siguiente: "
        mensaje += mensajeFactoryService.construirMensajeExito()

        return [
            destinatarios: usuarios,
            asunto       : MENSAJE_EXITO,
            mensaje      : mensaje
        ]
    }

    private generarNotificacionMasUsuariosQuePuestos(totalUsuariosActivos, totalPuestos) {
        def puestosFaltantes = totalUsuariosActivos - totalPuestos
        def usuariosAdmin = Usuario.findAllByEsAdminAndEstaActivo(true, true)
        def mensaje = "Faltan $puestosFaltantes puestos: se necesitan $totalUsuariosActivos y solamente existen $totalPuestos. "
        mensaje += mensajeFactoryService.construirMensajePuestosFaltantes(puestosFaltantes)

        return [
            destinatarios: usuariosAdmin,
            asunto       : MENSAJE_ALERTA_PUESTOS_FALTANTES,
            mensaje      : mensaje
        ]
    }
}
