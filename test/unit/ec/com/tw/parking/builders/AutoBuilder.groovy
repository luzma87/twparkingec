package ec.com.tw.parking.builders

import ec.com.tw.parking.Auto
import ec.com.tw.parking.Usuario

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomFromArray
import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 12/29/15 16:43.
 */
class AutoBuilder {
    String marca = getRandomString(2, 20, false)
    String modelo = getRandomString(2, 20, false)
    String placa = getRandomString(8)
    String tamanio = getRandomFromArray(["P", "G"])

    Usuario usuario = new UsuarioBuilder().crear()

    public AutoBuilder() {
    }

    def getParams() {
        return [
            marca  : this.marca,
            modelo : this.modelo,
            placa  : this.placa,
            tamanio: this.tamanio,
            usuario: this.usuario
        ]
    }

    public Auto crear() {
        new Auto(getParams())
    }

    def getCampoNuevoValido() {
        return [campo: 'marca', valor: this.marca]
    }

    def getCampoNuevoInvalido() {
        return [campo: 'marca', valor: getRandomString(21, 150, false)]
    }
}