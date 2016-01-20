package ec.com.tw.parking

import java.text.NumberFormat

class GeneradorNotificacionesService {
    def mensajeFactoryService
    def asignadorPuestosService
    def calculadorCuotaService

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

    def generadorNotificacionExito() {
        def usuarios = Usuario.findAllByEstaActivo(true)
        def cuota = calculadorCuotaService.calcularCuota()
        NumberFormat format = NumberFormat.getNumberInstance()
        format.maximumFractionDigits = 2
        cuota = format.format(cuota)
        def mensaje = "<p>Se han asignado los nuevos puestos de parqueo para este mes.</p>" +
            "<p>La cuota para este mes es de \$" + cuota + ". Por favor hagan el depósito lo más pronto posible, los datos son:</p>" +
            "<p>Cuenta de ahorros banco del Pichincha<br/>" +
            "4038057000<br/>" +
            "Luz Marina Unda<br/>" +
            "CI: 1715068159</p>" +
            "<p>Porfa envíen el mail de confirmación a twparkingec@thoughtworks.com Y a twparkingec@gmail.com</p>" +
            "<p>Porfa entregar los controles a Gaby antes de que empiece el nuevo mes para que las personas " +
            "que los necesitan puedan recogerlos</p>" +
            "<p>La nueva organización es la siguiente:<p>"
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
        def mensaje = "<p>Faltan $puestosFaltantes puestos: se necesitan $totalUsuariosActivos y solamente existen $totalPuestos. "
        mensaje += mensajeFactoryService.construirMensajePuestosFaltantes(puestosFaltantes)
        mensaje += "</p>"
        return [
            destinatarios: usuariosAdmin,
            asunto       : MENSAJE_ALERTA_PUESTOS_FALTANTES,
            mensaje      : mensaje
        ]
    }
}
