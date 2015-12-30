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

    def getParams() {
        return [
            nombre  : this.nombre,
            email   : this.email,
            password: this.password,
            esAdmin : this.esAdmin
        ]
    }

    def getCampoNuevoValido() {
        return [campo: 'nombre', valor: this.nombre]
    }

    def getCampoNuevoInvalido() {
        return [campo: 'nombre', valor: getRandomString(51, 150, false)]
    }

    def Usuario crear() {
        new Usuario(getParams())
    }
}
