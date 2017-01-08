package ec.com.tw.parking

import org.springframework.web.servlet.support.RequestContextUtils as RCU

class ImportsTagLib {
    static defaultEncodeAs = [taglib: 'raw']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "imp"

    def favicon = {
        def html = "<link rel=\"apple-touch-icon\" sizes=\"57x57\" href=\"${resource(dir: 'images/favicons', file: 'apple-touch-icon-57x57.png')}\">\n" +
            "<link rel=\"apple-touch-icon\" sizes=\"60x60\" href=\"${resource(dir: 'images/favicons', file: 'apple-touch-icon-60x60.png')}\">\n" +
            "<link rel=\"apple-touch-icon\" sizes=\"72x72\" href=\"${resource(dir: 'images/favicons', file: 'apple-touch-icon-72x72.png')}\">\n" +
            "<link rel=\"apple-touch-icon\" sizes=\"76x76\" href=\"${resource(dir: 'images/favicons', file: 'apple-touch-icon-76x76.png')}\">\n" +
            "<link rel=\"apple-touch-icon\" sizes=\"114x114\" href=\"${resource(dir: 'images/favicons', file: 'apple-touch-icon-114x114.png')}\">\n" +
            "<link rel=\"apple-touch-icon\" sizes=\"120x120\" href=\"${resource(dir: 'images/favicons', file: 'apple-touch-icon-120x120.png')}\">\n" +
            "<link rel=\"apple-touch-icon\" sizes=\"144x144\" href=\"${resource(dir: 'images/favicons', file: 'apple-touch-icon-144x144.png')}\">\n" +
            "<link rel=\"apple-touch-icon\" sizes=\"152x152\" href=\"${resource(dir: 'images/favicons', file: 'apple-touch-icon-152x152.png')}\">\n" +
            "<link rel=\"apple-touch-icon\" sizes=\"180x180\" href=\"${resource(dir: 'images/favicons', file: 'apple-touch-icon-180x180.png')}\">\n" +
            "<link rel=\"icon\" type=\"image/png\" href=\"${resource(dir: 'images/favicons', file: 'favicon-32x32.png\" sizes=\"32x32')}\">\n" +
            "<link rel=\"icon\" type=\"image/png\" href=\"${resource(dir: 'images/favicons', file: 'favicon-194x194.png\" sizes=\"194x194')}\">\n" +
            "<link rel=\"icon\" type=\"image/png\" href=\"${resource(dir: 'images/favicons', file: 'favicon-96x96.png\" sizes=\"96x96')}\">\n" +
            "<link rel=\"icon\" type=\"image/png\" href=\"${resource(dir: 'images/favicons', file: 'android-chrome-192x192.png\" sizes=\"192x192')}\">\n" +
            "<link rel=\"icon\" type=\"image/png\" href=\"${resource(dir: 'images/favicons', file: 'favicon-16x16.png\" sizes=\"16x16')}\">\n" +
            "<link rel=\"manifest\" href=\"${resource(dir: 'images/favicons', file: 'manifest.json')}\">\n" +
            "<link rel=\"mask-icon\" href=\"${resource(dir: 'images/favicons', file: 'safari-pinned-tab.svg\" color=\"#5bbad5')}\">\n" +
            "<meta name=\"apple-mobile-web-app-title\" content=\"Tw Parking\">\n" +
            "<meta name=\"application-name\" content=\"Tw Parking\">\n" +
            "<meta name=\"msapplication-TileColor\" content=\"#da532c\">\n" +
            "<meta name=\"msapplication-TileImage\" content=\"/mstile-144x144.png\">\n" +
            "<meta name=\"theme-color\" content=\"#3f51b5\">" +
            "<link rel=\"shortcut icon\" href=\"${resource(dir: 'images/favicons', file: 'favicon.ico')}\" type=\"image/x-icon\">"
        out << html
    }

    /**
     * @attrs theme El tema para cambiar el look de default
     */
    def bootstrap = { attrs ->
        def theme = attrs.theme ? ("." + attrs.theme) : ""
        def html = "<link href=\"${resource(dir: 'bootstrap-3.3.6/css', file: 'bootstrap' + theme + '.min.css')}\" rel=\"stylesheet\">" +
            "<script src=\"https://ajax.googleapis.com/ajax/libs/jquery/1.11.3/jquery.min.js\"></script>" +
            "<script src=\"${resource(dir: 'bootstrap-3.3.6/js', file: 'bootstrap.min.js')}\"></script>"
        out << html
    }

    def importFonts = {
        def html = "<link rel=\"stylesheet\" href=\"${resource(dir: 'fonts/font-awesome-4.5.0/css', file: 'font-awesome.min.css')}\">"
        html += "<link rel=\"stylesheet\" href=\"${resource(dir: 'fonts/font-mfizz-2.2', file: 'font-mfizz.css')}\">"
        out << html
    }

    def importCss = {
        def html = ""
        html += css(href: resource(dir: 'js/bootstrap-datetimepicker/build/css', file: 'bootstrap-datetimepicker.min.css'))
        html += css(href: resource(dir: 'js/jquery.qtip.custom', file: 'jquery.qtip.min.css'))
        html += css(href: resource(dir: 'js/pnotify/dist', file: 'pnotify.css'))
        html += css(href: resource(dir: 'js/pnotify/dist', file: 'pnotify.brighttheme.css'))
        html += css(href: resource(dir: 'js/bootstrap-datetimepicker/build/css', file: 'bootstrap-datetimepicker.min.css'))
        html += css(href: resource(dir: 'js/bootstrap-switch-master/dist/css/bootstrap3', file: 'bootstrap-switch.min.css'))

        html += css(href: resource(dir: 'css', file: 'text.css'))
        html += css(href: resource(dir: 'css', file: 'tablas.css'))
        out << html
    }

    def importJs = {
        def html = js(src: resource(dir: 'js/moment', file: 'moment.js'))
        html += js(src: resource(dir: 'js/bootbox', file: 'bootbox.js'))
        html += js(src: resource(dir: 'js/jquery-validation-1.14.0/dist/', file: 'jquery.validate.min.js'))

        def locale = RCU.getLocale(request)
        if (locale != 'en') {
            html += js(src: resource(dir: 'js/jquery-validation-1.14.0/dist/localization', file: 'messages_' + locale + '.min.js'))
        }
        html += js(src: resource(dir: 'js/bootstrap-datetimepicker/build/js', file: 'bootstrap-datetimepicker.min.js'))
        html += js(src: resource(dir: 'js/jquery.qtip.custom/', file: 'jquery.qtip.min.js'))
        html += js(src: resource(dir: 'js/jquery.qtip.custom/', file: 'jquery.qtip.min.js'))
        html += js(src: resource(dir: 'js/bootstrap-maxlength-1.7.0/src', file: 'bootstrap-maxlength.js'))
        html += js(src: resource(dir: 'js/pnotify/dist/', file: 'pnotify.js'))
        html += js(src: resource(dir: 'js/pnotify/dist/', file: 'pnotify.callbacks.js'))
        html += js(src: resource(dir: 'js', file: 'moment-with-locales.js'))
        html += js(src: resource(dir: 'js/bootstrap-datetimepicker/build/js', file: 'bootstrap-datetimepicker.min.js'))
        html += js(src: resource(dir: 'js/bootstrap-switch-master/dist/js', file: 'bootstrap-switch.min.js'))

        html += js(src: resource(dir: 'js', file: 'ui.js'))
        html += js(src: resource(dir: 'js', file: 'funciones.js'))
        out << html
    }

    /**
     * @attr href El url del css a importar
     * @attr scr El url del css a importar
     * @attr dir El directorio del css a importar
     * @attr file El nombre del archivo del css a importar
     */
    def css = { attrs ->
        def file = attrs.href
        if (!file) {
            file = attrs.src
        }

        if (attrs.dir && attrs.file) {
            file = resource(attrs)
        }

        if (file) {
            def html = "<link href=\"${file}\" rel=\"stylesheet\">"
            out << html
        }
    }

    /**
     * @attr href El url del js a importar
     * @attr scr El url del js a importar
     * @attr dir El directorio del js a importar
     * @attr file El nombre del archivo del js a importar
     */
    def js = { attrs ->
        def file = attrs.href
        if (!file) {
            file = attrs.src
        }

        if (attrs.dir && attrs.file) {
            file = resource(attrs)
        }

        if (file) {
            def html = "<script src=\"${file}\"></script>"
            out << html
        }
    }

}
