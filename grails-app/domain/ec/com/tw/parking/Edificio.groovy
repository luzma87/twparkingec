package ec.com.tw.parking

class Edificio {

    String nombre
    DistanciaEdificio distancia
    Boolean esAmpliable
    String datosPago
    String observaciones

    static hasMany = [puestos: Puesto]

    static mapping = {
        table 'edificio'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort nombre: "asc"
        puestos sort: "numero", order: "asc"
        columns {
            id column: 'id'
            nombre column: 'nombre'
            distancia column: 'distancia_edificio_id'
            datosPago column: 'datos_pago'
            datosPago type: 'text'
            observaciones column: 'observaciones'
        }
    }

    static constraints = {
        nombre nullable: false, blank: false, minSize: 2, maxSize: 50
        distancia nullable: false
        esAmpliable nullable: false
        datosPago nullable: true, blank: false
        observaciones nullable: true, blank: true, maxSize: 150
    }

    String toString() {
        return this.nombre
    }

    def getPuestosLibres() {
        if (this.puestos && this.puestos.size() > 0) {
            def asignaciones = AsignacionPuesto.findAllByPuestoInList(this.puestos)
            return asignaciones.findAll {
                it.puesto.estaActivo && (it.fechaLiberacion <= new Date() || it.fechaLiberacion == null)
            }.puesto
        }
        return []
    }
}
