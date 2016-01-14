package ec.com.tw.parking.builders

import ec.com.tw.parking.AsignacionPuesto
import ec.com.tw.parking.Auto
import ec.com.tw.parking.Puesto

import java.util.function.Consumer

/**
 * Created by lmunda on 05/01/2016
 */
class AsignacionPuestoBuilder {
    static AsignacionPuestoBuilder builder
    Auto auto = AutoBuilder.nuevo().crear()
    Puesto puesto = PuestoBuilder.nuevo().crear()
    Date fechaAsignacion = new Date()
    Date fechaLiberacion = null

    private AsignacionPuestoBuilder() {
    }

    def getParams() {
        return [
            auto           : this.auto,
            puesto         : this.puesto,
            fechaAsignacion: this.fechaAsignacion,
            fechaLiberacion: this.fechaLiberacion
        ]
    }

    public AsignacionPuesto crear() {
        new AsignacionPuesto(getParams())
    }

    public static List<AsignacionPuesto> lista(cantidad) {
        def lista = []
        cantidad.times {
            lista += nuevo().crear()
        }
        return lista
    }

    public AsignacionPuesto guardar() {
        def asignacion = crear()
        asignacion.auto.usuario.preferencia.save(failOnError: true)
        asignacion.auto.usuario.save(failOnError: true)
        asignacion.auto.save(failOnError: true)
        asignacion.puesto.edificio.distancia.save(failOnError: true)
        asignacion.puesto.edificio.save(failOnError: true)
        asignacion.puesto.save(failOnError: true)
        return asignacion.save(failOnError: true)
    }

    public static AsignacionPuestoBuilder nuevo() {
        builder = new AsignacionPuestoBuilder()
        return builder
    }

    public AsignacionPuestoBuilder con(Consumer<AsignacionPuestoBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }
}
