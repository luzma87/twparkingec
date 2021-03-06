package ec.com.tw.parking

import ec.com.tw.parking.builders.*
import ec.com.tw.parking.enums.Tamanio
import grails.test.spock.IntegrationSpec

import static ec.com.tw.parking.RandomUtilsHelpers.getRandomInt

class GeneradorNotificacionesConUsuariosNoSalenIntegrationSpec extends IntegrationSpec {

    def autos = [], puestos = [], asignaciones = [], usuarios = []
    def edificioMatriz, edificioCerca, edificioLejos
    def generadorNotificacionesService

    def setup() {
        def tipoPreferenciaSale = TipoPreferenciaBuilder.nuevo().con { p -> p.codigo = "S" }.guardar()
        def tipoPreferenciaNoSale = TipoPreferenciaBuilder.nuevo().con { p -> p.codigo = "N" }.guardar()
        def distanciaMatriz = DistanciaEdificioBuilder.nuevo().con { d -> d.codigo = "M" }.guardar()
        def distanciaCerca = DistanciaEdificioBuilder.nuevo().con { d -> d.codigo = "C" }.guardar()
        def distanciaLejos = DistanciaEdificioBuilder.nuevo().con { d -> d.codigo = "L" }.guardar()

        TipoTransicionBuilder.nuevo()
            .con { t -> t.distanciaOrigen = distanciaLejos }
            .con { t -> t.distanciaDestino = distanciaMatriz }
            .con { t -> t.prioridad = 1 }.guardar()
        TipoTransicionBuilder.nuevo()
            .con { t -> t.distanciaOrigen = distanciaCerca }
            .con { t -> t.distanciaDestino = distanciaLejos }
            .con { t -> t.prioridad = 2 }.guardar()
        TipoTransicionBuilder.nuevo()
            .con { t -> t.distanciaOrigen = distanciaMatriz }
            .con { t -> t.distanciaDestino = distanciaCerca }
            .con { t -> t.prioridad = 3 }.guardar()

        edificioMatriz = EdificioBuilder.nuevo()
            .con { e -> e.distancia = distanciaMatriz }
            .con { e -> e.esAmpliable = true }.guardar()
        edificioCerca = EdificioBuilder.nuevo()
            .con { e -> e.distancia = distanciaCerca }
            .con { e -> e.esAmpliable = false }.guardar()
        edificioLejos = EdificioBuilder.nuevo()
            .con { e -> e.distancia = distanciaLejos }
            .con { e -> e.esAmpliable = false }.guardar()
        2.times {
            usuarios += UsuarioBuilder.nuevo()
                .con { u -> u.estaActivo = true }
                .con { u -> u.preferencia = tipoPreferenciaNoSale }
                .guardar()
        }
        13.times {
            usuarios += UsuarioBuilder.nuevo()
                .con { u -> u.estaActivo = true }
                .con { u -> u.preferencia = tipoPreferenciaSale }
                .guardar()
        }
        7.times {
            autos += AutoBuilder.nuevo()
                .con { a -> a.esDefault = true }
                .con { a -> a.tamanio = Tamanio.PEQUENIO }
                .con { a -> a.usuario = usuarios[it] }.guardar()
        }
        5.times {
            autos += AutoBuilder.nuevo()
                .con { a -> a.esDefault = true }
                .con { a -> a.tamanio = Tamanio.MEDIANO }
                .con { a -> a.usuario = usuarios[it + 7] }.guardar()
        }
        3.times {
            autos += AutoBuilder.nuevo()
                .con { a -> a.esDefault = true }
                .con { a -> a.tamanio = Tamanio.GRANDE }
                .con { a -> a.usuario = usuarios[it + 12] }.guardar()
        }
        8.times {
            puestos += PuestoBuilder.nuevo().con { p -> p.edificio = edificioMatriz }
                .con { p -> p.tamanio = Tamanio.PEQUENIO }
                .con { p -> p.estaActivo = true }.guardar()
        }
        3.times {
            puestos += PuestoBuilder.nuevo().con { p -> p.edificio = edificioCerca }
                .con { p -> p.tamanio = Tamanio.MEDIANO }
                .con { p -> p.estaActivo = true }.guardar()
        }
        2.times {
            puestos += PuestoBuilder.nuevo().con { p -> p.edificio = edificioCerca }
                .con { p -> p.tamanio = Tamanio.GRANDE }
                .con { p -> p.estaActivo = true }.guardar()
        }
        puestos += PuestoBuilder.nuevo().con { p -> p.edificio = edificioLejos }
            .con { p -> p.tamanio = Tamanio.MEDIANO }
            .con { p -> p.estaActivo = true }.guardar()
        puestos += PuestoBuilder.nuevo().con { p -> p.edificio = edificioLejos }
            .con { p -> p.tamanio = Tamanio.GRANDE }
            .con { p -> p.estaActivo = true }.guardar()
        2.times {
            asignaciones += AsignacionPuestoBuilder.nuevo()
                .con { a -> a.fechaAsignacion = new Date() - getRandomInt(5, 10) }
                .con { a -> a.puesto = puestos[it] }
                .con { a -> a.auto = autos[it] }.guardar()
        }
        13.times {
            asignaciones += AsignacionPuestoBuilder.nuevo()
                .con { a -> a.fechaAsignacion = new Date() - getRandomInt(5, 10) }
                .con { a -> a.puesto = puestos[it + 2] }
                .con { a -> a.auto = autos[it + 2] }.guardar()
        }
    }

    def "Debe generar notificacion cuando hay igual de usuarios q de puestos, con usuarios con preferencia no sale"() {
        setup:
        def notificacionEsperada = setupIgualUsuariosQuePuestos("<p>Se han asignado los nuevos puestos de parqueo para este mes.")

        when:
        def notificacion = generadorNotificacionesService.generarNotificacion()

        then:
        expectsIgualUsuariosQuePuestos(notificacion, notificacionEsperada)
    }

    private setupIgualUsuariosQuePuestos(mensajeEsperado) {
        def notificacionEsperada = [
            destinatarios: usuarios,
            asunto       : "Nueva organización de parqueaderos",
            mensaje      : mensajeEsperado
        ]
        return notificacionEsperada
    }

    private expectsIgualUsuariosQuePuestos(notificacion, notificacionEsperada) {
        return notificacion.destinatarios.size() == notificacionEsperada.destinatarios.size() &&
            notificacion.destinatarios.id.sort() == notificacionEsperada.destinatarios.id.sort() &&
            notificacion.asunto == notificacionEsperada.asunto &&
            notificacion.mensaje.startsWith(notificacionEsperada.mensaje) &&
            AsignacionPuesto.count() == 21 &&
            AsignacionPuesto.withCriteria {
                isNull("fechaLiberacion")
                projections {
                    countDistinct("puesto")
                }
            }.first() == 15
    }
}
