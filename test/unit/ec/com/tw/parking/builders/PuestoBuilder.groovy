package ec.com.tw.parking.builders

import ec.com.tw.parking.Edificio
import ec.com.tw.parking.Puesto

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomFromArray
import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomInt

/**
 * Created by lmunda on 1/4/16 10:18.
 */
class PuestoBuilder {
    String tamanio = getRandomFromArray(["P", "G"])
    String numero = getRandomInt(100)
    Edificio edificio = new EdificioBuilder().crear()

    public PuestoBuilder() {
    }

    def getParams() {
        return [
            tamanio : this.tamanio,
            numero  : this.numero,
            edificio: this.edificio
        ]
    }

    public Puesto crear() {
        new Puesto(getParams())
    }

}
