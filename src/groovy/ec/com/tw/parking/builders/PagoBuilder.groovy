package ec.com.tw.parking.builders

import ec.com.tw.parking.Pago
import ec.com.tw.parking.Usuario
import ec.com.tw.parking.enums.Mes

import java.util.function.Consumer

import static ec.com.tw.parking.RandomUtilsHelpers.*

/**
 * Created by lmunda on 1/4/16 10:18.
 */
class PagoBuilder {
    static PagoBuilder builder
    Usuario usuario = UsuarioBuilder.nuevo().crear()
    Date fechaPago = new Date()
    String mes = getRandomFromArray(Mes.values())
    Double monto = getRandomDouble(1, 100)

    private PagoBuilder() {
    }

    def getParams() {
        return [
            usuario  : this.usuario,
            fechaPago: this.fechaPago,
            mes      : this.mes,
            monto    : this.monto
        ]
    }

    public Pago crear() {
        new Pago(getParams())
    }

    public static List<Pago> lista(int cantidad) {
        def lista = []
        cantidad.times {
            lista += nuevo().crear()
        }
        return lista
    }

    public static PagoBuilder nuevo() {
        builder = new PagoBuilder()
        return builder
    }

    public PagoBuilder con(Consumer<PagoBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }

    public Pago guardar() {
        def pago = crear()
        pago.usuario.preferencia.save(failOnError: true)
        pago.usuario.save(failOnError: true)
        return pago.save(failOnError: true)
    }
}
