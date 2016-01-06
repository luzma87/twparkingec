package ec.com.tw.parking

class CalculadorCuotaService {

    def calcularCuota() {
        def cantidadUsuarios = Usuario.countByEstaActivo(true)
        def puestos = Puesto.list()
        def sumaTotal = puestos.sum { it.precio }
        return sumaTotal / cantidadUsuarios
    }
}
