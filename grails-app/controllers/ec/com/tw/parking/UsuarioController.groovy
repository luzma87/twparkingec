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

    def form_ajax() {
        def usuarioInstance = getUsuario(params.id)
        return [usuarioInstance: usuarioInstance]
    }

    def save_ajax() {
        def usuario = getUsuario(params.id)
        if (!usuario) {
            render msgBuilder.renderNoEncontrado(entidad: 'usuario', id: params.id)
            return
        }
        render guardarUsuario(usuario, params)
    }

    def delete_ajax() {
        def usuario = getUsuario(params.id)
        if (!usuario || !usuario.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'usuario', id: params.id)
            return
        }
        render eliminarUsuario(usuario)
    }

    def getUsuario(id) {
        if (id) {
            return Usuario.get(id)
        }
        return new Usuario()
    }

    def guardarUsuario(usuario, params) {
        usuario.properties = params
        if (usuario.save(flush: true)) {
            return msgBuilder.renderGuardado(entidad: 'usuario', id: params.id)
        }
        return msgBuilder.renderNoGuardado(entidad: 'usuario', id: params.id)
    }

    def eliminarUsuario(usuario) {
        try {
            usuario.delete(flush: true)
            return msgBuilder.renderEliminado(entidad: 'usuario', id: params.id)
        } catch (DataIntegrityViolationException e) {
            return msgBuilder.renderNoEliminado(entidad: 'usuario', id: params.id)
        }
    }
}