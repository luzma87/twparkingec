package ec.com.tw.parking

class CalculadorCuotaService {

    def calcularCuota(puestosFaltantes, precio) {
        def cantidadUsuarios = Usuario.countByEstaActivo(true)
        def puestos = Puesto.list()
        println "puestos: " + puestos
        def sumaTotal = puestos.sum { it.precio }
        if (puestosFaltantes > 0) {
            sumaTotal += (puestosFaltantes * precio)
        }
        sumaTotal += 0.5
        println "suma total: " + sumaTotal
        println "cantidad usuarios: " + cantidadUsuarios
        return sumaTotal / cantidadUsuarios
    }

    def calcularCuota() {
        return calcularCuota(0, 0)
    }
}
