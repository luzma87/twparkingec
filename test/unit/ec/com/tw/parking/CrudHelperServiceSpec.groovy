package ec.com.tw.parking

import ec.com.tw.parking.builders.AutoBuilder
import ec.com.tw.parking.builders.EdificioBuilder
import ec.com.tw.parking.builders.UsuarioBuilder
import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib
import org.springframework.dao.DataIntegrityViolationException
import spock.lang.Specification

import static RandomUtilsHelpers.getRandomString

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
        def objeto = builder.crear()
        def params = builder.getParams()
        def respuesta = service.guardarObjeto(objeto, params)

        then:
        respuesta == "SUCCESS*default.saved.message"

        where:
        builder << [
            new UsuarioBuilder(),
            new AutoBuilder(),
            new EdificioBuilder(),
        ]
    }

    void "Debe retornar un mensaje de error cuando no se pudo guardar el objeto"() {
        when:
        def objeto = builder.crear()
        def params = builder.getParams()
        params.find().value = getRandomString(550, 1550, true)
        def respuesta = service.guardarObjeto(objeto, params)

        then:
        respuesta.startsWith("ERROR*default.not.saved.message")

        where:
        builder << [
            new UsuarioBuilder(),
            new AutoBuilder(),
            new EdificioBuilder(),
        ]
    }

    void "Debe retornar un mensaje de exito cuando el objeto se elimina exitosamente"() {
        when:
        objeto.save()
        def respuesta = service.eliminarObjeto(objeto)

        then:
        respuesta == "SUCCESS*default.deleted.message"

        where:
        objeto << [
            new UsuarioBuilder().crear(),
            new AutoBuilder().crear(),
            new EdificioBuilder().crear(),
        ]
    }

    void "Debe retornar un mensaje de error cuando no puede eliminar el objeto"() {
        when:
        mockDomain(Usuario, [new UsuarioBuilder().getParams()])
        Usuario.metaClass.delete = { Map params ->
            throw new DataIntegrityViolationException("ERROR")
        }
        def mocked = Usuario.get(1)
        def respuesta = service.eliminarObjeto(mocked)

        then:
        respuesta == "ERROR*default.not.deleted.message"
    }
}
