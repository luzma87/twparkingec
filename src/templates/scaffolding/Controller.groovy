<%=packageName ? "package ${packageName}" : ''%>

import org.springframework.dao.DataIntegrityViolationException
import <%=packageName%>.commons.Shield
<% props = domainClass.properties %>

class ${className}Controller extends Shield {

    static allowedMethods = [save_ajax: "POST", delete_ajax: "POST"]

    def index() {
        redirect(action: 'list')
    }

    def list() {
        return [${propertyName}List: ${className}.list(), ${propertyName}Count: ${className}.count()]
    }

    def show_ajax() {
        if(params.id) {
            def ${propertyName} = ${className}.get(params.id)
            if(!${propertyName}) {
                render mensajeError('default.not.found.message', params.id)
            }
            return [${propertyName}: ${propertyName}]
        } else {
            render mensajeError('default.not.found.message', params.id)
        }
    }

    def form_ajax() {
        def ${propertyName} = new ${className}()
        if(params.id) {
            ${propertyName} = ${className}.get(params.id)
            if(!${propertyName}) {
                render mensajeError('default.not.found.message', params.id)
                return
            }
        }
        ${propertyName}.properties = params
        return [${propertyName}: ${propertyName}]
    }

    def save_ajax() {
        def ${propertyName} = new ${className}()
        if(params.id) {
            ${propertyName} = ${className}.get(params.id)
            if(!${propertyName}) {
                render mensajeError('default.not.found.message', params.id)
                return
            }
        }
        ${propertyName}.properties = params
        if(!${propertyName}.save(flush: true)) {
            render mensajeError('default.not.saved.message', params.id) + renderErrors(bean: ${propertyName})
            return
        }
        render mensajeExito('default.saved.message', params.id)
    }

    def delete_ajax() {
        if(params.id) {
            def ${propertyName} = ${className}.get(params.id)
            if (!${propertyName}) {
                render mensajeError('default.not.found.message', params.id)
                return
            }
            try {
                ${propertyName}.delete(flush: true)
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
        return tipo + separador + message(code: codigo, args: [message(code: '${domainClass.propertyName}.label', default: '${className}'), id])
    }

    private String mensajeError(codigo, id) {
        mensajeConCodigo('ERROR', codigo, id)
    }

    private String mensajeExito(codigo, id) {
        mensajeConCodigo('SUCCESS', codigo, id)
    }
}