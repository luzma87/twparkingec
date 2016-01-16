package ec.com.tw.parking

class DistanciaEdificio {

    String codigo
    String descripcion

    static mapping = {
        table 'distancia_edificio'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort codigo: "asc"
        columns {
            id column: 'id'
            codigo column: 'codigo'
            descripcion column: 'descripcion'
        }
    }

    static constraints = {
        codigo nullable: false, blank: false, maxSize: 1
        descripcion nullable: false, blank: false, minSize: 3, maxSize: 10
    }

    String toString() {
        return this.descripcion
    }

    def obtenerDestino() {
        return TipoTransicion.findByDistanciaOrigen(this)?.distanciaDestino
    }

    def obtenerPuestosLibres() {
        def asg=AsignacionPuesto.withCriteria {
            and {
                puesto {
                    edificio {
                        eq("distancia", this)
                    }
                }
                isNotNull("fechaLiberacion")
                lt("fechaLiberacion", new Date())
            }
        }
        return asg.puesto
    }
}
