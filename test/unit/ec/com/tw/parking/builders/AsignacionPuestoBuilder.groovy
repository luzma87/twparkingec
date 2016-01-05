package ec.com.tw.parking.builders

import ec.com.tw.parking.AsignacionPuesto
import ec.com.tw.parking.Auto
import ec.com.tw.parking.Puesto

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomInt

/**
 * Created by lmunda on 05/01/2016
 */
class AsignacionPuestoBuilder {
    Auto auto = new AutoBuilder().crear()
    Puesto puesto = new PuestoBuilder().crear()
    Date fechaAsignacion = new Date()
    Date fechaLiberacion = new Date() + getRandomInt(10)

    public AsignacionPuestoBuilder() {
    }

    def getParams() {
        return [
            auto  : this.auto,
            puesto : this.puesto,
            fechaAsignacion  : this.fechaAsignacion,
            fechaLiberacion: this.fechaLiberacion
        ]
    }

    public AsignacionPuesto crear() {
        new AsignacionPuesto(getParams())
    }
}
