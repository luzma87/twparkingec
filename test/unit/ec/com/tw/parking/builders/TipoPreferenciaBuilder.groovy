package ec.com.tw.parking.builders

import ec.com.tw.parking.TipoPreferencia

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 01/4/15 17:46.
 */
class TipoPreferenciaBuilder {
    String codigo = getRandomString(1)
    String descripcion = getRandomString(3, 10, false)

    public TipoPreferenciaBuilder() {
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
}
