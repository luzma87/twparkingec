package ec.com.tw.parking.builders

import ec.com.tw.parking.TipoPreferencia
import ec.com.tw.parking.Usuario

import java.util.function.Consumer

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomBoolean
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomMail
import static ec.com.tw.parking.RandomUtilsHelpers.getRandomString

/**
 * Created by lmunda on 12/29/15 15:39.
 */
class UsuarioBuilder {
    static UsuarioBuilder builder
    String nombre = getRandomString(3, 50, false)
    String email = getRandomMail()
    String password = getRandomString(3, 512, false)
    Boolean esAdmin = getRandomBoolean()
    String cedula = getRandomString(10)
    Boolean estaActivo = getRandomBoolean()
    TipoPreferencia preferencia = TipoPreferenciaBuilder.nuevo().crear()

    private UsuarioBuilder() {
    }

    def getParams() {
        return [
            nombre     : this.nombre,
            email      : this.email,
            password   : this.password,
            esAdmin    : this.esAdmin,
            cedula     : this.cedula,
            estaActivo : this.estaActivo,
            preferencia: this.preferencia
        ]
    }

    def Usuario crear() {
        new Usuario(getParams())
    }

    static List<Usuario> lista(cantidad) {
        def lista = []
        cantidad.times {
            lista += nuevo().crear()
        }
        return lista
    }

    public static UsuarioBuilder nuevo() {
        builder = new UsuarioBuilder()
        return builder
    }

    public UsuarioBuilder con(Consumer<UsuarioBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }

    public Usuario guardar() {
        def usuarioBuilder = crear()
        usuarioBuilder.preferencia.save(failOnError: true)
        return usuarioBuilder.save(failOnError: true)
    }
}
