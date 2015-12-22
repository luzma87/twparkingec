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
        if (params.id) {
            def usuarioInstance = Usuario.get(params.id)
            if (!usuarioInstance) {
                render msgBuilder.noEncontrado(entidad: 'usuario', id: params.id)
            }
            return [usuarioInstance: usuarioInstance]
        } else {
            render msgBuilder.noEncontrado(entidad: 'usuario', id: params.id)
        }
    }

    def form_ajax() {
        def usuarioInstance = new Usuario()
        if (params.id) {
            usuarioInstance = Usuario.get(params.id)
            if (!usuarioInstance) {
                render msgBuilder.noEncontrado(entidad: 'usuario', id: params.id)
                return
            }
        }
        usuarioInstance.properties = params
        return [usuarioInstance: usuarioInstance]
    }

    def save_ajax() {
        def usuarioInstance = new Usuario()
        if (params.id) {
            usuarioInstance = Usuario.get(params.id)
            if (!usuarioInstance) {
                render msgBuilder.noEncontrado(entidad: 'usuario', id: params.id)
                return
            }
        }
        usuarioInstance.properties = params
        if (!usuarioInstance.save(flush: true)) {
            render msgBuilder.noGuardado(entidad: 'usuario', id: params.id)
            return
        }
        render msgBuilder.guardado(entidad: 'usuario', id: params.id)
    }

    def delete_ajax() {
        if (params.id) {
            def usuarioInstance = Usuario.get(params.id)
            if (!usuarioInstance) {
                render msgBuilder.noEncontrado(entidad: 'usuario', id: params.id)
                return
            }
            try {
                usuarioInstance.delete(flush: true)
                render msgBuilder.eliminado(entidad: 'usuario', id: params.id)
            } catch (DataIntegrityViolationException e) {
                render msgBuilder.noEliminado(entidad: 'usuario', id: params.id)
            }
        } else {
            render msgBuilder.noEncontrado(entidad: 'usuario', id: params.id)
        }
    }
}