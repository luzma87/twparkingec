package ec.com.tw.parking

import grails.plugin.mail.MailService
import grails.test.mixin.TestFor
import spock.lang.Specification

import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomMail
import static ec.com.tw.parking.helpers.RandomUtilsHelpers.getRandomString

@TestFor(EnviarMailService)
class EnviarMailServiceSpec extends Specification {

    String asuntoValido
    String mensajeValido
    MailService mailServiceMock
    String[] destinatariosValidos

    def setup() {
        asuntoValido = getRandomString(100)
        mensajeValido = getRandomString(1000)
        mailServiceMock = Mock(MailService)
        service.mailService = mailServiceMock
        destinatariosValidos = [getRandomMail(), getRandomMail(), getRandomMail()]
    }

    void "Debe enviar correo con un arreglo de destinatarios no nulo y no vacio"() {
        when:
        service.enviarMail(destinatariosValidos, asuntoValido, mensajeValido)

        then:
        1 * mailServiceMock.sendMail(_) >> null
    }

    void "Debe lanzar una excepcion si el arreglo de destinatarios es vacio"() {
        given:
        String[] destinatarios = []

        when:
        service.enviarMail(destinatarios, asuntoValido, mensajeValido)

        then:
        0 * mailServiceMock.sendMail(_) >> null
        def e = thrown(EnviarMailException)
        e.message == "ERROR: no se puede enviar un mail sin destinatarios"
    }

    void "Debe lanzar una excepcion si el arreglo de destinatarios es nulo"() {
        given:
        String[] destinatarios = null

        when:
        service.enviarMail(destinatarios, asuntoValido, mensajeValido)

        then:
        0 * mailServiceMock.sendMail(_) >> null
        def e = thrown(EnviarMailException)
        e.message == "ERROR: no se puede enviar un mail sin destinatarios"
    }

    void "Debe lanzar una excepcion si el asunto es nulo o vacio"() {
        when:
        service.enviarMail(destinatariosValidos, asunto, mensajeValido)

        then:
        0 * mailServiceMock.sendMail(_) >> null
        def e = thrown(EnviarMailException)
        e.message == "ERROR: no se puede enviar un mail sin asunto"

        where:
        asunto << [null, ""]
    }

    void "Debe lanzar una excepcion si el mensaje es nulo o vacio"() {
        when:
        service.enviarMail(destinatariosValidos, asuntoValido, mensaje)

        then:
        0 * mailServiceMock.sendMail(_) >> null
        def e = thrown(EnviarMailException)
        e.message == "ERROR: no se puede enviar un mail sin asunto"

        where:
        mensaje << [null, ""]
    }
}
