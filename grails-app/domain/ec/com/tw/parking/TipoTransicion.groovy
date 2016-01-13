package ec.com.tw.parking

class TipoTransicion {

    String nombre
    DistanciaEdificio distanciaOrigen
    DistanciaEdificio distanciaDestino
    Integer prioridad

    static mapping = {
        table 'tipo_transicion'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort prioridad: "asc"
        columns {
            id column: 'id'
            nombre column: 'nombre'
            distanciaOrigen column: 'distancia_edificio_origen_id'
            distanciaDestino column: 'distancia_edificio_destino_id'
            prioridad column: 'prioridad'
        }
    }

    static constraints = {
        nombre nullable: false, blank: false, minSize: 3, maxSize: 30
        distanciaOrigen nullable: false
        distanciaDestino nullable: false
        prioridad nullable: false, min: 1
    }

    String toString() {
        return this.nombre
    }
}
