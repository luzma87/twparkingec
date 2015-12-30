package ec.com.tw.parking.builders

import ec.com.tw.parking.Edificio

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 12/29/15 16:43.
 */
class EdificioBuilder {
    String nombre = getRandomString(2, 50, false)

    public EdificioBuilder() {
    }

    def getParams() {
        return [
            nombre: this.nombre
        ]
    }

    public Edificio crear() {
        new Edificio(getParams())
    }

    def getCampoNuevoValido() {
        return [campo: 'nombre', valor: this.nombre]
    }

    def getCampoNuevoInvalido() {
        return [campo: 'nombre', valor: getRandomString(51, 150, false)]
    }
}
