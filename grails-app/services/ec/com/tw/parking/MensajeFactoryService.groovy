package ec.com.tw.parking

class MensajeFactoryService {

    def calculadorCuotaService

    def construirMensajePuestosFaltantes(puestosFaltantes) {
        def mensaje
        def edificiosAmpliables = Edificio.findAllByEsAmpliable(true)
        switch (edificiosAmpliables.size()) {
            case 0:
                mensaje = "No se encontraron edificios ampliables, no se pudo recalcular la cuota"
                break
            case 1:
                def edificioAmpliable = edificiosAmpliables.first()
                def precio = Puesto.findByEdificioAndPrecioGreaterThan(edificioAmpliable, 0)?.precio
                def nuevaCuota = calculadorCuotaService.calcularCuota(puestosFaltantes, precio)
                mensaje = "Si se asume que los puestos faltantes se ubican en ${edificioAmpliable.nombre} (\$${precio}), " +
                    "la nueva cuota sería \$${nuevaCuota}"
                break
            default:
                mensaje = "Se encontraron ${edificiosAmpliables.size()} edificios ampliables, no se pudo recalcular la cuota"
        }
        return mensaje
    }

    String construirMensajeExito() {
        def mensaje = "<table class='table table-condensed table-bordered table-striped table-hover'>"
        mensaje += "<thead>"
        mensaje += "<tr>"
        mensaje += "<th>Thoughtworker</th>"
        mensaje += "<th>Puesto</th>"
        mensaje += "</tr>"
        mensaje += "</thead>"
        mensaje += "<tbody>"
        def asignacionesActuales = AsignacionPuesto.list()
        asignacionesActuales.each { asignacion ->
            mensaje += "<tr>"
            mensaje += "<td>"
            mensaje += asignacion.auto.usuario
            mensaje += "</td>"
            mensaje += "<td>"
            mensaje += asignacion
            mensaje += "</td>"
            mensaje += "</tr>"
            mensaje += "</tr>"
        }
        mensaje += "</tbody>"
        mensaje += "</table>"
        return mensaje
    }
}
