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

        def items = [
            inicio: [
                controller: "inicio",
                action    : "index",
                label     : message(code: 'navbar.home'),
                icon      : "fa-home"
            ],
            admin : [
                label: message(code: 'navbar.admin'),
                icon : "fa-cog",
                items: [
                    usuarios: [
                        controller: "usuario",
                        action    : "index",
                        label     : message(code: 'navbar.users'),
                        icon      : "fa-users"
                    ]
                ]
            ]
        ]

        items.each { k, item ->
            strItems += renderNavbarItem(item)
        }

        def paramsEs = session.pr ? session.pr.clone() : [:]
        def paramsEn = paramsEs.clone()
        paramsEs.lang = "es"
        paramsEn.lang = "en"
        def linkI18nEs = createLink(controller: session.cn, action: session.an, params: paramsEs)
        def linkI18nEn = createLink(controller: session.cn, action: session.an, params: paramsEn)

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
        html += "                       <a href=\"#\" class=\"dropdown-toggle\" data-toggle=\"dropdown\">Usuario <b class=\"caret\"></b></a>"
        html += "                       <ul class=\"dropdown-menu\">"
        html += "                           <li><a href=\"${createLink(controller: 'persona', action: 'personal')}\"><i class=\"fa fa-cogs\"></i> ${message(code: 'navbar.user.config')}</a></li>"
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

    def footer = { attrs, body ->
        def content = "Place sticky footer content here."
        if (attrs.content) {
            content = attrs.content
        } else if (body()) {
            content = body()
        }

        def html = "<footer class=\"footer\">\n" +
            "        <div class=\"container\">\n" +
            "            <p class=\"text-muted\">$content</p>\n" +
            "        </div>\n" +
            "    </footer>"
        out << raw(html)
    }

    private String renderNavbarItem(item) {
        def str = "", clase = ""
        if (session.cn == item.controller && session.an == item.action) {
            clase = "active"
        }
        if (item.items) {
            clase += " dropdown"
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
