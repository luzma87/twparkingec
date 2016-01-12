package ec.com.tw.parking.builders

import ec.com.tw.parking.TipoPreferencia

import java.util.function.Consumer

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 01/4/15 17:46.
 */
class TipoPreferenciaBuilder {
    static TipoPreferenciaBuilder builder
    String codigo = getRandomString(1)
    String descripcion = getRandomString(3, 10, false)

    private TipoPreferenciaBuilder() {
    }

    def getParams() {
        return [
            codigo     : this.codigo,
            descripcion: this.descripcion
        ]
    }

    public TipoPreferencia crear() {
        new TipoPreferencia(getParams())
    }

    public static TipoPreferenciaBuilder nuevo() {
        builder = new TipoPreferenciaBuilder()
        return builder
    }

    public TipoPreferenciaBuilder con(Consumer<TipoPreferenciaBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }

    public TipoPreferencia guardar() {
        return crear().save(failOnError: true)
    }

}
