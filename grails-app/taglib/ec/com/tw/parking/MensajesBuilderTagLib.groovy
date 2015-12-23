package ec.com.tw.parking

class MensajesBuilderTagLib {
    static defaultEncodeAs = [taglib: 'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "msgBuilder"

    def mensajeConCodigo = { attrs ->
        def tipo = attrs.tipo
        def codigo = attrs.codigo
        def id = attrs.id
        def entidad = message(code: attrs.entidad + '.label')
        def separador = attrs.separador ?: "*"
        out << tipo << separador << message(code: codigo, args: [entidad, id])
    }

    def error = { attrs ->
        def codigo = attrs.codigo
        def id = attrs.id
        def entidad = attrs.entidad
        out << mensajeConCodigo(tipo: "ERROR", codigo: codigo, id: id, entidad: entidad)
    }

    def exito = { attrs ->
        def codigo = attrs.codigo
        def id = attrs.id
        def entidad = attrs.entidad
        out << mensajeConCodigo(tipo: "SUCCESS", codigo: codigo, id: id, entidad: entidad)
    }

    def noEncontrado = { attrs ->
        def codigo = 'default.not.found.message'
        def id = attrs.id
        def entidad = attrs.entidad
        out << error(codigo: codigo, id: id, entidad: entidad)
    }

    def noGuardado = {attrs ->
        def codigo = 'default.not.saved.message'
        def id = attrs.id
        def entidad = attrs.entidad
        out << error(codigo: codigo, id: id, entidad: entidad)
    }

    def guardado = {attrs ->
        def codigo = 'default.saved.message'
        def id = attrs.id
        def entidad = attrs.entidad
        out << exito(codigo: codigo, id: id, entidad: entidad)
    }

    def noEliminado = {attrs ->
        def codigo = 'default.not.deleted.message'
        def id = attrs.id
        def entidad = attrs.entidad
        out << error(codigo: codigo, id: id, entidad: entidad)
    }

    def eliminado = {attrs ->
        def codigo = 'default.deleted.message'
        def id = attrs.id
        def entidad = attrs.entidad
        out << exito(codigo: codigo, id: id, entidad: entidad)
    }

}
