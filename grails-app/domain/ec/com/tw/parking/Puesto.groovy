package ec.com.tw.parking

import ec.com.tw.parking.enums.Tamanio

class Puesto {

    Tamanio tamanio
    String numero
    Double precio = 0.0
    Boolean estaActivo = true

    static belongsTo = [edificio: Edificio]

    static mapping = {
        table 'puesto'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort edificio: "asc"
        columns {
            id column: 'id'
            tamanio column: 'tamanio'
            numero column: 'numero'
            edificio column: 'edificio_id'
            precio column: 'precio'
            estaActivo column: 'esta_activo'
        }
    }

    static constraints = {
        tamanio nullable: false, blank: false
        numero nullable: false, blank: false, minSize: 1, maxSize: 6
        precio nullable: false, min: 0d
        estaActivo nullable: false
    }

    static ArrayList<Puesto> obtenerLibresPorTamanio(Tamanio tamanio) {
        def tamanios = tamanio.tamaniosCompatibles
        def asg = AsignacionPuesto.withCriteria {
            and {
                puesto {
                    inList("tamanio", tamanios)
                }
                lt("fechaLiberacion", new Date())
                isNotNull("fechaLiberacion")
            }
        }
        return asg.puesto
    }

    String toString() {
        return this.edificio.nombre + " #" + this.numero
    }

    static obtenerSinAsignacion() {
        def todosPuestos = Puesto.list()
        def puestosConAsignacion = AsignacionPuesto.withCriteria {
            projections {
                distinct("puesto")
            }
        }
        def puestosEnAmbasListas = todosPuestos.intersect(puestosConAsignacion)
        def puestosSinAsignacion = todosPuestos + puestosConAsignacion
        puestosSinAsignacion.removeAll(puestosEnAmbasListas)

        def puestosSinAsignacionActiva = []
        todosPuestos.each { puesto ->
            if (AsignacionPuesto.countByPuestoAndFechaLiberacionIsNotNull(puesto) > 0 &&
                AsignacionPuesto.countByPuestoAndFechaLiberacionIsNull(puesto) == 0) {
                puestosSinAsignacionActiva += puesto
            }
        }
        puestosSinAsignacion += puestosSinAsignacionActiva
        return puestosSinAsignacion
    }
}
