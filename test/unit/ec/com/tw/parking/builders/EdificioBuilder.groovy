package ec.com.tw.parking.builders

import ec.com.tw.parking.DistanciaEdificio
import ec.com.tw.parking.Edificio

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomBoolean
import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 12/29/15 16:43.
 */
class EdificioBuilder {
    String nombre = getRandomString(2, 50, false)
    DistanciaEdificio distancia = new DistanciaEdificioBuilder().crear()
    String datosPago = getRandomString(300, 1050, false)
    Boolean esAmpliable = getRandomBoolean()

    public EdificioBuilder() {
    }

    def getParams() {
        return [
            nombre     : this.nombre,
            distancia  : this.distancia,
            datosPago  : this.datosPago,
            esAmpliable: this.esAmpliable
        ]
    }

    public Edificio crear() {
        new Edificio(getParams())
    }
}
