package ec.com.tw.parking

class Puesto {

    String tamanio
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
        tamanio nullable: false, blank: false, inList: ["P", "G"]
        numero nullable: false, blank: false, minSize: 1, maxSize: 6
        precio nullable: false, min: 0d
    }

    String toString() {
        return this.edificio.nombre + " " + this.numero
    }
}
