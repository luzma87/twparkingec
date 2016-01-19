package ec.com.tw.parking

import ec.com.tw.parking.commons.Shield

class UsuarioController extends Shield {

    def crudHelperService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [usuarioInstanceList: Usuario.list(), usuarioInstanceCount: Usuario.count()]
    }

    def form_ajax() {
        def usuarioInstance = crudHelperService.obtenerObjeto(Usuario, params.id)
        return [usuarioInstance: usuarioInstance]
    }

    def save_ajax() {
        def usuarioInstance = crudHelperService.obtenerObjeto(Usuario, params.id)
        if (!usuarioInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'usuario')
            return
        }
        if (params.password) {
            params.password = params.password.toString().encodeAsSHA256()
        }
        render crudHelperService.guardarObjeto(usuarioInstance, params)
    }

    def delete_ajax() {
        def usuarioInstance = crudHelperService.obtenerObjeto(Usuario, params.id)
        if (!usuarioInstance || !usuarioInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'usuario')
            return
        }
        render crudHelperService.eliminarObjeto(usuarioInstance)
    }

    def password_ajax() {
        def usuarioInstance = crudHelperService.obtenerObjeto(Usuario, params.id)
        return [usuarioInstance: usuarioInstance]
    }

    def personal() {
        def usuario = Usuario.get(session.usuario.id)
        return [usuarioInstance: usuario]
    }

    def save() {
        def usuarioInstance = crudHelperService.obtenerObjeto(Usuario, params.id)
        if (!usuarioInstance) {
            render msgBuilder.renderNoEncontrado(entidad: 'usuario')
            return
        }
        if (params.password) {
            params.password = params.password.toString().encodeAsSHA256()
        }
        if (params.old_password && session.usuario.password != params.old_password.toString().trim().encodeAsSHA256()) {
            flash.tipo = "error"
            flash.message = "Su contraseña actual no concuerda"
            redirect(action: "personal")
            return
        }
        def result = crudHelperService.guardarObjeto(usuarioInstance, params)
        def parts = result.split("\\*")
        flash.tipo = parts[0]
        flash.message = parts[1]
        redirect(action: "personal")
    }

    def cambiarEstado() {
        def id = params.id
        def activar = params.activar == "true"

        def usuario = Usuario.get(id)
        usuario.estaActivo = activar
        if (usuario.save(flush: true)) {
            render "SUCCESS*Usuario actualizado exitosamente"
        } else {
            render "ERROR*Ha ocurrido un error al actualizar usuario"
        }
    }
}