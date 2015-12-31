package ec.com.tw.parking

import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import spock.lang.IgnoreRest
import spock.lang.Specification

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

/**
 * See the API for {@link grails.test.mixin.services.ServiceUnitTestMixin} for usage instructions
 */
@TestFor(CrudHelperService)
@Mock([Usuario, Auto, Edificio, MensajesBuilderTagLib, ValidationTagLib])
class CrudHelperServiceSpec extends Specification {
    void "Debe retornar un nuevo objeto del tipo especificado"() {
        when:
        def respuesta = service.obtenerObjeto(dominio, null)

        then:
        respuesta.class == dominio

        where:
        dominio << [Usuario, Auto, Edificio]
    }

    void "Debe retornar un nuevo objeto del tipo especificado con id"() {
        when:
        objetos.objeto.save()
        def respuesta = service.obtenerObjeto(objetos.dominio, 1)

        then:
        respuesta.class == objetos.dominio
        respuesta.properties == objetos.objeto.properties

        where:
        objetos << [
            [dominio: Usuario, objeto: new UsuarioBuilder().crear()],
            [dominio: Auto, objeto: new AutoBuilder().crear()],
            [dominio: Edificio, objeto: new EdificioBuilder().crear()],
        ]
    }

    void "Debe retornar un mensaje de exito cuando el objeto se guarda exitosamente"() {
        when:
        def objeto = objetos.builder.crear()
        def params = objetos.builder.getParams()
        def respuesta = service.guardarObjeto(objetos.entidad, objeto, params)

        then:
        respuesta == "SUCCESS*default.saved.message"

        where:
        objetos << [
            [entidad: 'usuario', builder: new UsuarioBuilder()],
            [entidad: 'auto', builder: new AutoBuilder()],
            [entidad: 'edificio', builder: new EdificioBuilder()],
        ]
    }

    void "Debe retornar un mensaje de error cuando no se pudo guardar el objeto"() {
        when:
        def objeto = objetos.builder.crear()
        def params = objetos.builder.getParams()
        params.find().value = getRandomString(550, 1550, true)
        def respuesta = service.guardarObjeto(objetos.entidad, objeto, params)

        then:
        respuesta.startsWith("ERROR*default.not.saved.message")

        where:
        objetos << [
            [entidad: 'usuario', builder: new UsuarioBuilder()],
            [entidad: 'auto', builder: new AutoBuilder()],
            [entidad: 'edificio', builder: new EdificioBuilder()],
        ]
    }

    void "Debe retornar un mensaje de exito cuando el objeto se elimina exitosamente"() {
        when:
        objetos.objeto.save()
        def respuesta = service.eliminarObjeto(objetos.entidad, objetos.objeto,)

        then:
        respuesta == "SUCCESS*default.deleted.message"

        where:
        objetos << [
            [entidad: 'usuario', objeto: new UsuarioBuilder().crear()],
            [entidad: 'auto', objeto: new AutoBuilder().crear()],
            [entidad: 'edificio', objeto: new EdificioBuilder().crear()],
        ]
    }
}
