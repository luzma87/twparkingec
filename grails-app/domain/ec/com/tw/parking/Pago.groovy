package ec.com.tw.parking

import ec.com.tw.parking.enums.Mes

class Pago {
    Usuario usuario
    Date fechaPago
    Mes mes
    Integer anio
    Double monto

    static mapping = {
        table 'pago'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort usuario: "asc"
        columns {
            id column: 'id'
            usuario column: 'usuario_id'
            fechaPago column: 'fecha_pago'
            mes column: 'mes'
            anio column: 'anio'
            monto column: 'monto'
        }
    }

    static constraints = {
        usuario nullable: false
        fechaPago nullable: false
        mes nullable: false
        anio nullable: false
        monto nullable: false, min: 0D
    }
}
