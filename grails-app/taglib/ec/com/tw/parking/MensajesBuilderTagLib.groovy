package ec.com.tw.parking

class MensajesBuilderTagLib {
    static defaultEncodeAs = [taglib: 'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "msgBuilder"

    def mensajeConCodigo = { attrs ->
        def codigo = attrs.codigo
        def id = attrs.id
        def entidad = message(code: attrs.entidad + '.label')
        def prefijo = attrs.prefijio ?: ""
        out << prefijo << message(code: codigo, args: [entidad, id])
    }

    def noEncontradoHtml = { attrs ->
        def codigo = 'default.not.found.message'
        def entidad = attrs.entidad
        def msg = mensajeConCodigo(codigo: codigo, entidad: entidad)
        out << elm.flashMessage(tipo: "notFound", closable: false, contenido: msg)
    }

    def renderError = { attrs ->
        def codigo = attrs.codigo
        def id = attrs.id
        def entidad = attrs.entidad
        out << mensajeConCodigo(prefijio: "ERROR*", codigo: codigo, id: id, entidad: entidad)
    }

    def renderExito = { attrs ->
        def codigo = attrs.codigo
        def id = attrs.id
        def entidad = attrs.entidad
        out << mensajeConCodigo(prefijo: "SUCCESS*", codigo: codigo, id: id, entidad: entidad)
    }

    def renderNoEncontrado = { attrs ->
        def codigo = 'default.not.found.message'
        def entidad = attrs.entidad
        out << renderError(codigo: codigo, entidad: entidad)
    }

    def renderNoGuardado = { attrs ->
        def codigo = 'default.not.saved.message'
        def id = attrs.id
        def entidad = attrs.entidad
        out << renderError(codigo: codigo, id: id, entidad: entidad)
    }

    def renderGuardado = { attrs ->
        def codigo = 'default.saved.message'
        def id = attrs.id
        def entidad = attrs.entidad
        out << renderExito(codigo: codigo, id: id, entidad: entidad)
    }

    def renderNoEliminado = { attrs ->
        def codigo = 'default.not.deleted.message'
        def id = attrs.id
        def entidad = attrs.entidad
        out << renderError(codigo: codigo, id: id, entidad: entidad)
    }

    def renderEliminado = { attrs ->
        def codigo = 'default.deleted.message'
        def id = attrs.id
        def entidad = attrs.entidad
        out << renderExito(codigo: codigo, id: id, entidad: entidad)
    }

}
