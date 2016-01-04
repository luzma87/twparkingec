<%=packageName ? "package ${packageName}" : ''%>

import <%=packageName%>.commons.Shield
<% classNameLower = domainClass.propertyName %>
class ${className}Controller extends Shield {

    def crudHelperService

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [${propertyName}List: ${className}.list(), ${propertyName}Count: ${className}.count()]
    }

    def form_ajax() {
        def ${propertyName} = crudHelperService.obtenerObjeto(${className}, params.id)
        return [${propertyName}: ${propertyName}]
    }

    def save_ajax() {
        def ${propertyName} = crudHelperService.obtenerObjeto(${className}, params.id)
        if(!${propertyName}) {
            render msgBuilder.renderNoEncontrado(entidad: '${classNameLower}')
            return
        }
        render crudHelperService.guardarObjeto(${propertyName}, params)
    }

    def delete_ajax() {
        def ${propertyName} = crudHelperService.obtenerObjeto(${className}, params.id)
        if(!${propertyName} || !${propertyName}.id) {
            render msgBuilder.renderNoEncontrado(entidad: '${classNameLower}')
            return
        }
        render crudHelperService.eliminarObjeto(${propertyName})
    }
}