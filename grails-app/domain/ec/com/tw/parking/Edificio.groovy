package ec.com.tw.parking

class Edificio {

    String nombre
    DistanciaEdificio distancia

    static hasMany = [puestos: Puesto]

    static mapping = {
        table 'edificio'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort nombre: "asc"
        columns {
            id column: 'id'
            nombre column: 'nombre'
            distancia column: 'distancia_edificio_id'
        }
    }

    static constraints = {
        nombre nullable: false, blank: false, minSize: 2, maxSize: 50
        distancia nullable: false
    }

    String toString() {
        return this.nombre
    }
}
