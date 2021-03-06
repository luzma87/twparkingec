package ec.com.tw.parking

class MenuTagLib {
    static defaultEncodeAs = [taglib: 'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static namespace = "mn"

    /**
     * @attr title El title a mostrar en la barra
     */
    def navbar = { attrs ->

        def strItems = ""
        if (!attrs.title) {
            attrs.title = "TW Parking EC"
        }

        def items = getMenuItems()

        items.each { k, item ->
            strItems += renderNavbarItem(item)
        }

        def paramsEs = session.pr ? session.pr.clone() : [:]
        def paramsEn = paramsEs.clone()
        paramsEs.lang = "es"
        paramsEn.lang = "en"
        def linkI18nEs = createLink(controller: session.cn, action: session.an, params: paramsEs)
        def linkI18nEn = createLink(controller: session.cn, action: session.an, params: paramsEn)

        def usuario = session.usuario.toString()

        def html = "<!-- Fixed navbar -->"
        html += "    <nav class=\"navbar navbar-default navbar-fixed-top\">"
        html += "        <div class=\"container\">"
        html += "            <div class=\"navbar-header\">"
        html += "                <button type=\"button\" class=\"navbar-toggle collapsed\" data-toggle=\"collapse\" data-target=\"#navbar\" aria-expanded=\"false\" aria-controls=\"navbar\">"
        html += "                    <span class=\"sr-only\">Toggle navigation</span>"
        html += "                    <span class=\"icon-bar\"></span>"
        html += "                    <span class=\"icon-bar\"></span>"
        html += "                    <span class=\"icon-bar\"></span>"
        html += "                </button>"
        html += "                <a class=\"navbar-brand\" href=\"#\">${attrs.title}</a>"
        html += "            </div>"

        html += "            <div id=\"navbar\" class=\"collapse navbar-collapse\">"
        html += "                <ul class=\"nav navbar-nav\">"
        html += strItems
        html += "                </ul>"

        html += "                 <ul class=\"nav navbar-nav navbar-right\">"
        html += "                   <li class=\"dropdown\">"
        html += "                       <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">${message(code: 'navbar.language')} <b class=\"caret\"></b></a>"
        html += "                       <ul class=\"dropdown-menu\">"
        html += "                           <li><a href=\"$linkI18nEs\"><i class=\"fa fa-language\"></i> ${message(code: 'navbar.language.spanish')}</a></li>"
        html += "                           <li><a href=\"$linkI18nEn\"><i class=\"fa fa-language\"></i> ${message(code: 'navbar.language.english')}</a></li>"
        html += "                       </ul>"
        html += "                   </li>"
        html += "                   <li class=\"dropdown\">"
        html += "                       <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">$usuario <b class=\"caret\"></b></a>"
        html += "                       <ul class=\"dropdown-menu\">"
        html += "                           <li><a href=\"${createLink(controller: 'usuario', action: 'personal')}\"><i class=\"fa fa-cogs\"></i> ${message(code: 'navbar.user.config')}</a></li>"
        html += "                           <li class=\"divider\"></li>"
        html += "                           <li><a href=\"${createLink(controller: 'login', action: 'logout')}\"><i class=\"fa fa-power-off\"></i> ${message(code: 'navbar.user.logout')}</a></li>"
        html += "                       </ul>"
        html += "                   </li>"
        html += "                </ul>"

        html += "            </div><!--/.nav-collapse -->"
        html += "        </div>"
        html += "    </nav>"

        out << raw(html)
    }

    private getMenuItems() {
        if (session.esAdmin) {
            return getAdminMenuItems()
        }
        return getUsuariosMenuItems()
    }

    private getAdminMenuItems() {
        return [
            inicio   : [
                controller: "inicio",
                action    : "index",
                label     : message(code: 'navbar.home'),
                icon      : "fa-home"
            ],
            admin    : [
                label: message(code: 'navbar.admin'),
                icon : "fa-cog",
                items: [
                    usuarios    : [
                        controller: "usuario",
                        action    : "list",
                        label     : message(code: 'navbar.users'),
                        icon      : "fa-user"
                    ],
                    edificios   : [
                        controller: "edificio",
                        action    : "list",
                        label     : message(code: 'navbar.edificios'),
                        icon      : "fa-building"
                    ],
                    asignaciones: [
                        controller: "asignacionPuesto",
                        action    : "list",
                        label     : message(code: 'navbar.asignaciones'),
                        icon      : "fa-cubes",
                        separator : true
                    ],
                    pagos: [
                        controller: "pago",
                        action    : "list",
                        label     : message(code: 'navbar.pagos'),
                        icon      : "fa-money",
                    ],
                    parametros  : [
                        controller: "parametros",
                        action    : "index",
                        label     : message(code: 'navbar.parametros'),
                        icon      : "fa-cog",
                        separator : true
                    ]
                ]
            ],
            asignar  : [
                controller: "asignacionPuesto",
                action    : "index",
                label     : message(code: 'navbar.asignar'),
                icon      : "fa-bullseye"
            ],
            historial: [
                controller: "asignacionPuesto",
                action    : "historial",
                label     : message(code: 'navbar.historial'),
                icon      : "fa-calendar"
            ],
            pagos: [
                controller: "pago",
                action    : "index",
                label     : message(code: 'navbar.pagos'),
                icon      : "fa-money"
            ]
        ]
    }

    private getUsuariosMenuItems() {
        return [
            inicio   : [
                controller: "inicio",
                action    : "index",
                label     : message(code: 'navbar.home'),
                icon      : "fa-home"
            ],
            historial: [
                controller: "asignacionPuesto",
                action    : "historial",
                label     : message(code: 'navbar.historial'),
                icon      : "fa-calendar"
            ],
            pagos: [
                controller: "pago",
                action    : "index",
                label     : message(code: 'navbar.pagos'),
                icon      : "fa-money"
            ]
        ]
    }


    private String renderNavbarItem(item) {
        def str = "", clase = ""
        if (session.cn == item.controller && session.an == item.action) {
            clase = "active"
        }
        if (item.items) {
            clase += " dropdown"
            item.items.each { t, it ->
                if (session.cn == it.controller && session.an == it.action) {
                    clase = "active"
                }
            }
        }

        if (item.separator == true) {
            str += "<li role=\"separator\" class=\"divider\"></li>"
        }

        str += "<li class='" + clase + "'>"
        if (item.items) {
            str += "<a href='#' class='dropdown-toggle' data-toggle='dropdown'>"
            if (item.icon) {
                str += "<i class='fa ${item.icon}'></i>"
                str += " "
            }
            str += item.label
            str += "<b class=\"caret\"></b></a>"
            str += '<ul class="dropdown-menu" role="menu" aria-labelledby="dLabel">'
            item.items.each { t, i ->
                str += renderNavbarItem(i)
            }
            str += "</ul>"
        } else {
            str += "<a href='" + createLink(controller: item.controller, action: item.action, params: item.params) + "'>"
            if (item.icon) {
                str += "<i class='fa ${item.icon}'></i>"
                str += " "
            }
            str += item.label
            str += "</a>"
        }
        str += "</li>"

        return str
    }

}
