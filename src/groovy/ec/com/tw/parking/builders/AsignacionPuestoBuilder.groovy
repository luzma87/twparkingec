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
    Auto auto = new AutoBuilder().crear()
    Puesto puesto = new PuestoBuilder().crear()
    Date fechaAsignacion = new Date()
    Date fechaLiberacion = null

    public AsignacionPuestoBuilder() {
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

    public List<AsignacionPuesto> crearLista(cantidad) {
        def lista = []
        cantidad.times {
            lista += new AsignacionPuestoBuilder().crear()
        }
        return lista
    }

    public AsignacionPuesto guardar() {
        return crear().save(failOnError: true)
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
