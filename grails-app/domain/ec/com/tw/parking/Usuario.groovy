package ec.com.tw.parking

class Usuario {

    String nombre
    String email
    String password

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
        }
    }

    static constraints = {
        nombre nullable: false, blank: false, minSize: 3, maxSize: 50
        email nullable: false, blank: false, email: true, maxSize: 100
        password nullable: false, blank: false, password: true, maxSize: 512
    }

}
