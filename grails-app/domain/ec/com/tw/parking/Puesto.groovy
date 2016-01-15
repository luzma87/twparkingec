package ec.com.tw.parking

class Puesto {

    Tamanio tamanio
    String numero
    Double precio = 0.0

    //TODO: prueba de integracion delete edificio => delete puestos
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
        }
    }

    static constraints = {
        tamanio nullable: false, blank: false
        numero nullable: false, blank: false, minSize: 1, maxSize: 6
        precio nullable: false, min: 0d
    }

    static ArrayList<Puesto> obtenerLibresPorTamanio(Tamanio tamanio) {
        return AsignacionPuesto.withCriteria {
            puesto {
                eq("tamanio", tamanio)
            }
            le("fechaLiberacion", new Date())
        }.puesto
    }

    String toString() {
        return this.edificio.nombre + " " + this.numero
    }
}
