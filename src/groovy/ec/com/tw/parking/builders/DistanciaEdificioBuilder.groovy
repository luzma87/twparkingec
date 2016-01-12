package ec.com.tw.parking.builders

import ec.com.tw.parking.DistanciaEdificio

import java.util.function.Consumer

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 01/4/15 17:46.
 */
class DistanciaEdificioBuilder {
    static DistanciaEdificioBuilder builder
    String codigo = getRandomString(1)
    String descripcion = getRandomString(3, 10, false)

    private DistanciaEdificioBuilder() {
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

    public static DistanciaEdificioBuilder nuevo (){
        builder = new DistanciaEdificioBuilder()
        return builder
    }

    public DistanciaEdificioBuilder con(Consumer<DistanciaEdificioBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }

    public DistanciaEdificio guardar(){
        return crear().save(failOnError: true)
    }
}
