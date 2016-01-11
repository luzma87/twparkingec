package ec.com.tw.parking.builders

import ec.com.tw.parking.Auto
import ec.com.tw.parking.Usuario

import java.util.function.Consumer

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomBoolean
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomFromArray
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 12/29/15 16:43.
 */
class AutoBuilder {
    static AutoBuilder builder
    String marca = getRandomString(2, 20, false)
    String modelo = getRandomString(2, 20, false)
    String placa = getRandomString(8)
    String tamanio = getRandomFromArray(["P", "G"])
    Boolean esDefault = getRandomBoolean()

    Usuario usuario = new UsuarioBuilder().crear()

    public AutoBuilder() {
    }

    def getParams() {
        return [
            marca    : this.marca,
            modelo   : this.modelo,
            placa    : this.placa,
            tamanio  : this.tamanio,
            usuario  : this.usuario,
            esDefault: this.esDefault
        ]
    }

    public Auto crear() {
        new Auto(getParams())
    }

    public List<Auto> crearLista(cantidad) {
        def lista = []
        cantidad.times {
            lista += new AutoBuilder().crear()
        }
        return lista
    }

    public static AutoBuilder nuevo(){
        builder = new AutoBuilder()
        return builder
    }

    public AutoBuilder con (Consumer<AutoBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }

    public Auto guardar() {
        Auto auto = crear()
        auto.usuario.preferencia.save(failOnError: true)
        auto.usuario.save(failOnError: true)
        return auto.save(failOnError: true)
    }
}
