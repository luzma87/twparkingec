package ec.com.tw.parking

import org.springframework.dao.DataIntegrityViolationException
import ec.com.tw.parking.commons.Shield


class UsuarioController extends Shield {

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [usuarioInstanceList: Usuario.list(), usuarioInstanceCount: Usuario.count()]
    }

    def show_ajax() {
        if(params.id) {
            def usuarioInstance = Usuario.get(params.id)
            if(!usuarioInstance) {
                render mensajeError('default.not.found.message', params.id)
            }
            return [usuarioInstance: usuarioInstance]
        } else {
            render mensajeError('default.not.found.message', params.id)
        }
    }

    def form_ajax() {
        def usuarioInstance = new Usuario()
        if(params.id) {
            usuarioInstance = Usuario.get(params.id)
            if(!usuarioInstance) {
                render mensajeError('default.not.found.message', params.id)
                return
            }
        }
        usuarioInstance.properties = params
        return [usuarioInstance: usuarioInstance]
    }

    def save_ajax() {
        def usuarioInstance = new Usuario()
        if(params.id) {
            usuarioInstance = Usuario.get(params.id)
            if(!usuarioInstance) {
                render mensajeError('default.not.found.message', params.id)
                return
            }
        }
        usuarioInstance.properties = params
        if(!usuarioInstance.save(flush: true)) {
            render mensajeError('default.not.saved.message', params.id) + renderErrors(bean: usuarioInstance)
            return
        }
        render mensajeExito('default.saved.message', params.id)
    }

    def delete_ajax() {
        if(params.id) {
            def usuarioInstance = Usuario.get(params.id)
            if (!usuarioInstance) {
                render mensajeError('default.not.found.message', params.id)
                return
            }
            try {
                usuarioInstance.delete(flush: true)
                render mensajeExito('default.deleted.message', params.id)
            } catch (DataIntegrityViolationException e) {
                render mensajeError('default.delete.error.message', params.id)
            }
        } else {
            render mensajeError('default.not.found.message', params.id)
        }
    }

    private String mensajeConCodigo(tipo, codigo, id) {
        def separador = "*"
        return tipo + separador + message(code: codigo, args: [message(code: 'usuario.label', default: 'Usuario'), id])
    }

    private String mensajeError(codigo, id) {
        mensajeConCodigo('ERROR', codigo, id)
    }

    private String mensajeExito(codigo, id) {
        mensajeConCodigo('SUCCESS', codigo, id)
    }
}