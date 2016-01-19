package ec.com.tw.parking

class LoginController {

    def login() {

    }

    def iniciarSesion() {
        if (!params.usuario || !params.password) {
            redirect(action: "login")
            return
        }
        def password = params.password.toString().encodeAsSHA256()
        def email = params.usuario.toString().trim().toLowerCase() + "@thoughtworks.com"
        def usuarios = Usuario.withCriteria {
            eq("email", email)
            eq("password", password)
//            eq("estaActivo", true)
        }
        if (usuarios.size() == 1) {
            def usuario = usuarios.first()
            session.usuario = usuario
            session.esAdmin = usuario.esAdmin
            def controlador = "inicio", accion = "index", parametros = []
            if (session.cn && session.an) {
                controlador = session.cn
                accion = session.an
                parametros = session.pr
            }
            redirect(controller: controlador, action: accion, params: parametros)
        } else {
            flash.message = "No se encontró el usuario solicitado"
            flash.tipo = "error"
            redirect(action: "login")
        }
    }

    def logout() {
        session.usuario = null
        session.esAdmin = null
        session.an = null
        session.cn = null
        session.pr = null
        session.invalidate()
        redirect(controller: 'login', action: 'login')
    }
}
