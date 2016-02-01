package ec.com.tw.parking.builders

import ec.com.tw.parking.DistanciaEdificio
import ec.com.tw.parking.Edificio

import java.util.function.Consumer

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomBoolean
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 12/29/15 16:43.
 */
class EdificioBuilder {
    static EdificioBuilder builder
    String nombre = getRandomString(2, 50, false)
    DistanciaEdificio distancia = DistanciaEdificioBuilder.nuevo().crear()
    String datosPago = getRandomString(300, 1050, false)
    Boolean esAmpliable = getRandomBoolean()
    String observaciones = getRandomString(1, 150, false)

    private EdificioBuilder() {
    }

    def getParams() {
        return [
            nombre       : this.nombre,
            distancia    : this.distancia,
            datosPago    : this.datosPago,
            esAmpliable  : this.esAmpliable,
            observaciones: this.observaciones
        ]
    }

    public Edificio crear() {
        new Edificio(getParams())
    }

    public static EdificioBuilder nuevo() {
        builder = new EdificioBuilder()
        return builder
    }

    public EdificioBuilder con(Consumer<EdificioBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }

    public Edificio guardar() {
        return crear().save(failOnError: true)
    }
}
