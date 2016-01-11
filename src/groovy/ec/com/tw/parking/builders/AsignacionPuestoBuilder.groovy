package ec.com.tw.parking.builders

import ec.com.tw.parking.AsignacionPuesto
import ec.com.tw.parking.Auto
import ec.com.tw.parking.Puesto

/**
 * Created by lmunda on 05/01/2016
 */
class AsignacionPuestoBuilder {
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

    public static AsignacionPuesto crearDefault() {
        return new AsignacionPuestoBuilder().crear()
    }

    public static List<AsignacionPuesto> crearLista(cantidad) {
        def lista = []
        cantidad.times{
           lista += new AsignacionPuestoBuilder().crear()
        }
        return lista
    }
}
