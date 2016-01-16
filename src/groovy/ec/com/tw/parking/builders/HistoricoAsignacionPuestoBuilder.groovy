package ec.com.tw.parking.builders

import ec.com.tw.parking.AsignacionPuesto
import ec.com.tw.parking.Auto
import ec.com.tw.parking.HistoricoAsignacionPuesto
import ec.com.tw.parking.Puesto

import java.util.function.Consumer

/**
 * Created by lmunda on 05/01/2016
 */
class HistoricoAsignacionPuestoBuilder {
    static HistoricoAsignacionPuestoBuilder builder
    Auto auto = AutoBuilder.nuevo().crear()
    Puesto puesto = PuestoBuilder.nuevo().crear()
    Date fechaAsignacion = new Date()
    Date fechaLiberacion = null

    private HistoricoAsignacionPuestoBuilder() {
    }

    def getParams() {
        return [
            auto           : this.auto,
            puesto         : this.puesto,
            fechaAsignacion: this.fechaAsignacion,
            fechaLiberacion: this.fechaLiberacion
        ]
    }

    public HistoricoAsignacionPuesto crear() {
        new HistoricoAsignacionPuesto(getParams())
    }

    public static List<HistoricoAsignacionPuesto> lista(cantidad) {
        def lista = []
        cantidad.times {
            lista += nuevo().crear()
        }
        return lista
    }

    public HistoricoAsignacionPuesto guardar() {
        def historico = crear()
        historico.auto.usuario.preferencia.save(failOnError: true)
        historico.auto.usuario.save(failOnError: true)
        historico.auto.save(failOnError: true)
        historico.puesto.edificio.distancia.save(failOnError: true)
        historico.puesto.edificio.save(failOnError: true)
        historico.puesto.save(failOnError: true)
        return historico.save(failOnError: true)
    }

    public static HistoricoAsignacionPuestoBuilder nuevo() {
        builder = new HistoricoAsignacionPuestoBuilder()
        return builder
    }

    public HistoricoAsignacionPuestoBuilder con(Consumer<HistoricoAsignacionPuestoBuilder> consumer) {
        consumer.accept(builder)
        return builder
    }
}
