package ec.com.tw.parking

class Usuario {

    String nombre
    String email
    String password
    Boolean esAdmin = false
    String cedula
    Boolean estaActivo = true
    TipoPreferencia preferencia

    static hasMany = [autos: Auto]

    static mapping = {
        table 'usuario'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort nombre: "asc"
        columns {
            id column: 'id'
            nombre column: 'nombre'
            email column: 'email'
            password column: 'password'
            esAdmin column: 'es_admin'
            estaActivo column: 'esta_activo'
            preferencia column: 'tipo_preferencia_id'
        }
    }

    static constraints = {
        nombre nullable: false, blank: false, minSize: 3, maxSize: 50
        email nullable: false, blank: false, email: true, maxSize: 100, unique: true
        password nullable: false, blank: false, maxSize: 512, password: true
        esAdmin nullable: false
        cedula nullable: false, blank: false, minSize: 10, maxSize: 10, unique: true
        estaActivo nullable: false
        preferencia nullable: false
    }

    String toString() {
        return this.nombre
    }

}
