package ec.com.tw.parking.builders

import ec.com.tw.parking.DistanciaEdificio

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 01/4/15 17:46.
 */
class DistanciaEdificioBuilder {
    String codigo = getRandomString(1)
    String descripcion = getRandomString(3, 10, false)

    public DistanciaEdificioBuilder() {
    }

    def getParams() {
        return [
            codigo     : this.codigo,
            descripcion: this.descripcion
        ]
    }

    public DistanciaEdificio crear() {
        new DistanciaEdificio(getParams())
    }
}
