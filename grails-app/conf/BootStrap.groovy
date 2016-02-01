import ec.com.tw.parking.AsignacionPuesto
import ec.com.tw.parking.DatosIniciales
import ec.com.tw.parking.Pago
import ec.com.tw.parking.enums.Tamanio
import grails.util.Environment

import ec.com.tw.parking.DistanciaEdificio
import ec.com.tw.parking.Edificio
import ec.com.tw.parking.Puesto
import ec.com.tw.parking.TipoPreferencia
import ec.com.tw.parking.TipoTransicion
import ec.com.tw.parking.Usuario

class BootStrap {

    def init = { servletContext ->

        if (Environment.current != Environment.TEST) {
            if (DistanciaEdificio.count() == 0) {
                DatosIniciales.crearDistanciasEdificio()
            }

            if (TipoPreferencia.count() == 0) {
                DatosIniciales.crearTiposPreferencia()
            }

            if (TipoTransicion.count() == 0) {
                DatosIniciales.crearTiposTransicion()
            }

            if (Usuario.count() == 0) {
                DatosIniciales.crearUsuariosYautos()
            }

            if (Edificio.count() == 0) {
                DatosIniciales.crearEdificiosYpuestos()
            }

            if (AsignacionPuesto.count() == 0) {
                DatosIniciales.crearAsignaciones()
            }

            if (Pago.count() == 0) {
                DatosIniciales.crearPagos()
            }
        }
    }
}
