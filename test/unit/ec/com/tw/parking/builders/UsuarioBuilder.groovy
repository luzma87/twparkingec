package ec.com.tw.parking.builders

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
    String password = getRandomString(3, 512, true)
    Boolean esAdmin = getRandomBoolean()

    public UsuarioBuilder() {
    }

    public Usuario crear() {
        new Usuario([
            nombre  : this.nombre,
            email   : this.email,
            password: this.password,
            esAdmin : this.esAdmin
        ])
    }
}
