package ec.com.tw.parking

class Auto {

    String marca
    String modelo
    String placa
    Tamanio tamanio
    Boolean esDefault

    static belongsTo = [usuario: Usuario]

    static mapping = {
        table 'auto'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort usuario: "asc"
        columns {
            id column: 'id'
            marca column: 'marca'
            modelo column: 'modelo'
            placa column: 'placa'
            tamanio column: 'tamanio'
            usuario column: 'usuario_id'
            esDefault column: 'es_default'
        }
    }

    static constraints = {
        marca nullable: false, blank: false, minSize: 2, maxSize: 20
        modelo nullable: false, blank: false, minSize: 2, maxSize: 25
        placa nullable: false, blank: false, minSize: 7, maxSize: 8
        tamanio nullable: false, blank: false, maxSize: 1
        esDefault nullable: false
    }

    String toString() {
        return this.usuario.toString() + ": " + this.marca + " " + this.modelo + (this.esDefault ? "" : "*")
    }

}
