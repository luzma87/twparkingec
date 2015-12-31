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
        render crudHelperService.guardarObjeto('usuario', usuarioInstance, params)
    }

    def delete_ajax() {
        def usuarioInstance = crudHelperService.obtenerObjeto(Usuario, params.id)
        if (!usuarioInstance || !usuarioInstance.id) {
            render msgBuilder.renderNoEncontrado(entidad: 'usuario')
            return
        }
        render crudHelperService.eliminarObjeto('usuario', usuarioInstance)
    }

    def password_ajax() {
        def usuarioInstance = crudHelperService.obtenerObjeto(Usuario, params.id)
        return [usuarioInstance: usuarioInstance]
    }
}