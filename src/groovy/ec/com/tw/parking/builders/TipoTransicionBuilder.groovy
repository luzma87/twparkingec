package ec.com.tw.parking.builders

import ec.com.tw.parking.DistanciaEdificio
import ec.com.tw.parking.TipoTransicion

import java.util.function.Consumer

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomInt
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 01/4/15 17:46.
 */
class TipoTransicionBuilder {
    static TipoTransicionBuilder builder
    String nombre = getRandomString(3, 30, false)
    DistanciaEdificio distanciaOrigen = DistanciaEdificioBuilder.nuevo().crear()
    DistanciaEdificio distanciaDestino = DistanciaEdificioBuilder.nuevo().crear()
    Integer prioridad = getRandomInt(1, 10)

    private TipoTransicionBuilder() {
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

    public static TipoTransicionBuilder nuevo() {
        builder = new TipoTransicionBuilder()
        return builder
    }

    public TipoTransicionBuilder con(Consumer<TipoTransicionBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }

    public TipoTransicion guardar() {
        def tipoTransicion = crear()
        tipoTransicion.distanciaOrigen.save(failOnError: true)
        tipoTransicion.distanciaDestino.save(failOnError: true)
        return tipoTransicion.save(failOnError: true)
    }
}
