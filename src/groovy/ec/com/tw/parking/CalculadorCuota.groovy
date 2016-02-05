package ec.com.tw.parking

/**
 * Created by lmunda on 2/5/16 16:59.
 */
class CalculadorCuota {

    static final COSTO_TRANSFERENCIA = 0.5

    static double calcularCuota(puestosFaltantes, precioPuestosFaltantes) {
        def cantidadUsuariosActivos = Usuario.countByEstaActivo(true) + puestosFaltantes
        def puestos = Puesto.findAllByEstaActivo(true)
        def totalPrecioPuestos = (double) (puestos.sum { it.precio })
        totalPrecioPuestos += (puestosFaltantes * precioPuestosFaltantes)
        return (cantidadUsuariosActivos + COSTO_TRANSFERENCIA) / totalPrecioPuestos
    }

}
