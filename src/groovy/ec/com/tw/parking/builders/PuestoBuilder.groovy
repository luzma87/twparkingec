package ec.com.tw.parking.builders

import ec.com.tw.parking.Edificio
import ec.com.tw.parking.Puesto

import java.util.function.Consumer

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomDouble
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomFromArray
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomInt

/**
 * Created by lmunda on 1/4/16 10:18.
 */
class PuestoBuilder {
    static PuestoBuilder builder
    String tamanio = getRandomFromArray(["P", "G"])
    String numero = getRandomInt(100)
    Edificio edificio = new EdificioBuilder().crear()
    Double precio = getRandomDouble(100)

    private PuestoBuilder() {
    }

    def getParams() {
        return [
            tamanio : this.tamanio,
            numero  : this.numero,
            edificio: this.edificio,
            precio  : this.precio
        ]
    }

    public Puesto crear() {
        new Puesto(getParams())
    }

    public List<Puesto> crearLista(int cantidad) {
        def lista = []
        cantidad.times {
            lista += new PuestoBuilder().crear()
        }
        return lista
    }

    public static PuestoBuilder nuevo() {
        builder = new PuestoBuilder()
        return builder
    }

    public PuestoBuilder con(Consumer<PuestoBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }

    public Puesto guardar() {
        def puesto = crear()
        puesto.edificio.distancia.save(failOnError: true)
        puesto.edificio.save(failOnError: true)
        return puesto.save(failOnError: true)
    }
}
