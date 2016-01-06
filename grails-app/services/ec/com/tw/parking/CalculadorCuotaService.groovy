package ec.com.tw.parking

class CalculadorCuotaService {

    def calcularCuota(puestosFaltantes, precio) {
        def cantidadUsuarios = Usuario.countByEstaActivo(true)
        def puestos = Puesto.list()
        def sumaTotal = puestos.sum { it.precio }
        if (puestosFaltantes > 0) {
            sumaTotal += (puestosFaltantes * precio)
        }
        return sumaTotal / cantidadUsuarios
    }

    def calcularCuota() {
        return calcularCuota(0, 0)
    }
}
