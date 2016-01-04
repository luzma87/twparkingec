package ec.com.tw.parking

class Edificio {

    String nombre

//     TODO
//    static hasMany = [puestos: Puesto]

    static mapping = {
        table 'edificio'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort nombre: "asc"
        columns {
            id column: 'id'
            nombre column: 'nombre'
        }
    }

    static constraints = {
        nombre nullable: false, blank: false, minSize: 2, maxSize: 50
    }

    String toString() {
        return this.nombre
    }
}
