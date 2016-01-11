package ec.com.tw.parking.builders

import ec.com.tw.parking.DistanciaEdificio
import ec.com.tw.parking.TipoTransicion

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomInt
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 01/4/15 17:46.
 */
class TipoTransicionBuilder {
    String nombre = getRandomString(3, 30, false)
    DistanciaEdificio distanciaOrigen = new DistanciaEdificioBuilder().crear()
    DistanciaEdificio distanciaDestino = new DistanciaEdificioBuilder().crear()
    Integer prioridad = getRandomInt(1, 10)

    public TipoTransicionBuilder() {
    }

    def getParams() {
        return [
            nombre          : this.nombre,
            distanciaOrigen : this.distanciaOrigen,
            distanciaDestino: this.distanciaDestino,
            prioridad       : this.prioridad
        ]
    }

    public TipoTransicion crear() {
        new TipoTransicion(getParams())
    }
}
