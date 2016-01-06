package ec.com.tw.parking

class MensajeFactoryService {

    def calculadorCuotaService

    def construirMensaje(puestosFaltantes) {
        def mensaje
        def edificiosAmpliables = Edificio.findAllByEsAmpliable(true)
        switch (edificiosAmpliables.size()) {
            case 0:
                mensaje = "No se encontraron edificios ampliables, no se pudo recalcular la cuota"
                break
            case 1:
                def edificioAmpliable = edificiosAmpliables.first()
                def precio = Puesto.findByEdificioAndPrecioGreaterThan(edificioAmpliable, 0).precio
                def nuevaCuota = calculadorCuotaService.calcularCuota(puestosFaltantes, precio)
                mensaje = "Si se asume que los puestos faltantes se ubican en ${edificioAmpliable.nombre} (\$${precio}), " +
                    "la nueva cuota sería \$${nuevaCuota}"
                break
            default:
                mensaje = "Se encontraron ${edificiosAmpliables.size()} edificios ampliables, no se pudo recalcular la cuota"
        }
        return mensaje
    }
}
