package ec.com.tw.parking

import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.DistanciaEdificioBuilder
import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.PuestoBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.spock.IntegrationSpec

class GeneradorNotificacionesMasUsuariosQuePuestosIntegrationSpec extends IntegrationSpec {

    def autos = [], puestos = [], asignaciones = [], usuarios = []
    def edificioMatriz
    def generadorNotificacionesService

    def setup() {
        def distanciaMatriz = DistanciaEdificioBuilder.nuevo().con { d -> d.codigo = "M" }.guardar()
        edificioMatriz = EdificioBuilder.nuevo()
            .con { e -> e.distancia = distanciaMatriz }
            .con { e -> e.esAmpliable = true }.guardar()
        10.times { usuarios += UsuarioBuilder.nuevo().con { u -> u.estaActivo = true }.guardar() }
        10.times {
            autos += AutoBuilder.nuevo()
                .con { a -> a.esDefault = true }
                .con { a -> a.usuario = usuarios[it] }.guardar()
        }
        5.times { puestos += PuestoBuilder.nuevo().con { p -> p.edificio = edificioMatriz }.guardar() }
        5.times { puestos += PuestoBuilder.nuevo().con { p -> p.edificio.esAmpliable = false }.guardar() }
        10.times {
            asignaciones += AsignacionPuestoBuilder.nuevo()
                .con { a -> a.puesto = puestos[it] }
                .con { a -> a.auto = autos[it] }.guardar()
        }
    }

    def "Debe generar notificacion de alerta cuando existen mas usuarios que puestos y 1 edificio ampliable"() {
        setup:
        def notificacionEsperada = setupMasUsuariosQuePuestos(/<p>Faltan \d+ puestos: se necesitan \d+ y solamente existen \d+\. Si se asume que los puestos faltantes se ubican en \w+ \(\$\d+\.\d+\), la nueva cuota sería \$\d+\.\d+<\/p>/)

        when:
        def notificacion = generadorNotificacionesService.generarNotificacion()

        then:
        expectsMasUsuariosQuePuestos(notificacion, notificacionEsperada)
    }

    def "Debe generar notificacion de alerta cuando existen mas usuarios que puestos y 0 edificios ampliables"() {
        setup:
        def notificacionEsperada = setupMasUsuariosQuePuestos(/<p>Faltan \d+ puestos: se necesitan \d+ y solamente existen \d+\. No se encontraron edificios ampliables, no se pudo recalcular la cuota<\/p>/)
        edificioMatriz.esAmpliable = false
        edificioMatriz.save()

        when:
        def notificacion = generadorNotificacionesService.generarNotificacion()

        then:
        expectsMasUsuariosQuePuestos(notificacion, notificacionEsperada)
    }

    def "Debe generar notificacion de alerta cuando existen mas usuarios que puestos y varios edificios ampliables"() {
        setup:
        def notificacionEsperada = setupMasUsuariosQuePuestos(/<p>Faltan \d+ puestos: se necesitan \d+ y solamente existen \d+\. Se encontraron \d+ edificios ampliables, no se pudo recalcular la cuota<\/p>/)
        def edificios = Edificio.findAllByEsAmpliable(false, [max: 3])
        edificios.each { edificio ->
            edificio.esAmpliable = true
            edificio.save()
        }

        when:
        def notificacion = generadorNotificacionesService.generarNotificacion()

        then:
        expectsMasUsuariosQuePuestos(notificacion, notificacionEsperada)
    }

    private setupMasUsuariosQuePuestos(mensajeEsperado) {
        def notificacionEsperada = [
            destinatarios: usuarios.findAll { it.estaActivo && it.esAdmin },
            asunto       : "ALERTA: puestos faltantes",
            mensaje      : mensajeEsperado
        ]
        5.times {
            autos += AutoBuilder.nuevo()
                .con { a -> a.usuario.esAdmin = false }
                .con { a -> a.usuario.estaActivo = true }
                .guardar()
        }
        return notificacionEsperada
    }

    private expectsMasUsuariosQuePuestos(notificacion, notificacionEsperada) {
        return notificacion.destinatarios.size() == notificacionEsperada.destinatarios.size() &&
            notificacion.destinatarios.id.sort() == notificacionEsperada.destinatarios.id.sort() &&
            notificacion.asunto == notificacionEsperada.asunto &&
            notificacion.mensaje ==~ notificacionEsperada.mensaje &&
            AsignacionPuesto.count() == 10
    }

}
