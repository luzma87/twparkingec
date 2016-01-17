package ec.com.tw.parking

import org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib

class EnviarMailService {

    def mailService

    final ERROR_MAIL_DESTINATARIO = "ERROR: no se puede enviar un mail sin destinatarios"
    final ERROR_MAIL_ASUNTO = "ERROR: no se puede enviar un mail sin asunto"
    final ERROR_MAIL_MENSAJE = "ERROR: no se puede enviar un mail sin asunto"

    def g = new ApplicationTagLib()

    def enviarMail(destinatarios, asunto, mensaje) {
        if (!destinatarios || destinatarios.size() == 0) {
            throw new EnviarMailException(ERROR_MAIL_DESTINATARIO)
        }
        if (!asunto || asunto == "") {
            throw new EnviarMailException(ERROR_MAIL_ASUNTO)
        }
        if (!mensaje || mensaje == "") {
            throw new EnviarMailException(ERROR_MAIL_MENSAJE)
        }
        mailService.sendMail {
            to destinatarios
            subject asunto
            html g.render(template: "/mailTemplates/mailTemplate", model: [mensaje: mensaje])
        }
    }
}
