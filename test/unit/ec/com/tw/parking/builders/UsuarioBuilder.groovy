package ec.com.tw.parking.builders

import ec.com.tw.parking.TipoPreferencia
import ec.com.tw.parking.Usuario

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomBoolean
import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomMail
import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 12/29/15 15:39.
 */
class UsuarioBuilder {
    String nombre = getRandomString(3, 50, false)
    String email = getRandomMail()
    String password = getRandomString(3, 512, false)
    Boolean esAdmin = getRandomBoolean()
    String cedula = getRandomString(10)
    Boolean estaActivo = getRandomBoolean()
    TipoPreferencia preferencia = new TipoPreferenciaBuilder().crear()

    public UsuarioBuilder() {
    }

    def getParams() {
        return [
            nombre     : this.nombre,
            email      : this.email,
            password   : this.password,
            esAdmin    : this.esAdmin,
            cedula     : this.cedula,
            estaActivo : this.estaActivo,
            preferencia: this.preferencia
        ]
    }

    def Usuario crear() {
        new Usuario(getParams())
    }
}
