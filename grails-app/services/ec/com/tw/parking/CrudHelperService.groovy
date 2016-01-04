package ec.com.tw.parking

import grails.transaction.Transactional
import grails.util.GrailsNameUtils
import org.springframework.dao.DataIntegrityViolationException

@Transactional
class CrudHelperService {
    def grailsApplication

    def obtenerObjeto(dominio, id) {
        if (id) {
            return dominio.get(id)
        }
        return dominio.newInstance()
    }

    def guardarObjeto(objeto, params) {
        def entidad =  GrailsNameUtils.getPropertyName(Usuario)
        def msgBuilder = grailsApplication.mainContext.getBean('ec.com.tw.parking.MensajesBuilderTagLib')
        def g = grailsApplication.mainContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ValidationTagLib')

        objeto.properties = params
        if (objeto.save(flush: true)) {
            return msgBuilder.renderGuardado(entidad: entidad)
        }
        return msgBuilder.renderNoGuardado(entidad: entidad) + ": " + g.renderErrors(bean: objeto)
    }

    def eliminarObjeto(objeto) {
        def entidad =  GrailsNameUtils.getPropertyName(Usuario)
        def msgBuilder = grailsApplication.mainContext.getBean('ec.com.tw.parking.MensajesBuilderTagLib')
        try {
            objeto.delete(flush: true)
            return msgBuilder.renderEliminado(entidad: entidad)
        } catch (DataIntegrityViolationException e) {
            return msgBuilder.renderNoEliminado(entidad: entidad)
        }
    }
}
