package ec.com.tw.parking

class AsignacionPuesto {

    Auto auto
    Puesto puesto
    Date fechaAsignacion
    Date fechaLiberacion

    static mapping = {
        table 'asignacion_puesto'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort auto: "asc"
        columns {
            id column: 'id'
            auto column: 'auto_id'
            puesto column: 'puesto_id'
            fechaAsignacion column: 'fecha_asignacion'
            fechaLiberacion column: 'fecha_liberacion'
        }
    }

    static constraints = {
        auto nullable: false
        puesto nullable: false
        fechaAsignacion nullable: false
        fechaLiberacion nullable: true
    }

    String toString() {
        def persona = this.auto.usuario.nombre
        def placa = this.auto.placa
        def edificio = this.puesto.edificio.nombre
        def numero = this.puesto.numero
        return persona + " (" + placa + ") → " + edificio + " " + numero
    }
}
