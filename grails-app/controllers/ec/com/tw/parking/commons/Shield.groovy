package ec.com.tw.parking.commons

class Shield {
    def beforeInterceptor = [action: this.&auth, except: 'login']
    /**
     * Verifica si se ha iniciado una sesión
     * Verifica si el usuario actual tiene los permisos para ejecutar una acción
     */
    def auth() {

        if (!actionName.contains("ajax")) {
            session.an = actionName
            session.cn = controllerName
            session.pr = params
        }
        if (!session.usuario) {
            println "intenta acceder a " + controllerName + "/" + actionName + "  params: " + params + " pero no tiene sesion"
            redirect(controller: 'login', action: 'login')
            session.finalize()
            return false
        }

        if (isAllowed()) {
            return true
        } else {
            println "intenta acceder a " + controllerName + "/" + actionName + "  params: " + params + " pero no tiene permiso"
            response.sendError(403)
            return false
        }
    }

    boolean isAllowed() {
        if (request.method == "POST") {
            return true
        }
        if (session.esAdmin) {
            return true
        } else {
            def allowedUser = [
                inicio          : ["index"],
                asignacionPuesto: ["historial"],
                usuario         : ["personal"]
            ]
            if (allowedUser[controllerName] && allowedUser[controllerName].contains(actionName)) {
                return true
            }
        }
    }


}