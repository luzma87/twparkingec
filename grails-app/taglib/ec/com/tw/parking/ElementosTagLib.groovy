package ec.com.tw.parking

class ElementosTagLib {
    static defaultEncodeAs = [taglib: 'html']
    //static encodeAsForTags = [tagName: [taglib:'html'], otherTagName: [taglib:'none']]

    static encodeAsForTags = [flashMessage: [taglib: 'raw']]

    static namespace = "elm"

    /**
     * Crea un div para mensajes
     * @attr contenido El contenido del mensaje. También puede ser el body del tag
     * @attr icono El ícono deseado para el mensaje
     * @attr clase La clase para el mensaje
     */
    def flashMessage = { attrs, body ->
        def contenido = body()

        def closable =  true
        if(attrs.closable == false || attrs.closable == "no") {
            closable = false
        }

        if (!contenido) {
            if (attrs.contenido) {
                contenido = attrs.contenido
            }
        }

        if (contenido) {
            def finHtml = "</p></div>"

            def icono = ""
            def clase = attrs.clase ?: ""

            if (attrs.icon) {
                icono = attrs.icon
            } else {
                switch (attrs.tipo?.toLowerCase()) {
                    case "error":
                        icono = "fa fa-times"
                        clase += "alert-danger"
                        break;
                    case "success":
                        icono = "fa fa-check"
                        clase += "alert-success"
                        break;
                    case "notfound":
                        icono = "icon-ghost"
                        clase += "alert-info"
                        break;
                    case "warning":
                        icono = "fa fa-warning"
                        clase += "alert-warning"
                        break;
                    case "info":
                        icono = "fa fa-info-circle"
                        clase += "alert-info"
                        break;
                    case "bug":
                        icono = "fa fa-bug"
                        clase += "alert-warning"
                        break;
                    default:
                        clase += "alert-info"
                }
            }
            def html = "<div class=\"alert alert-dismissable ${clase}\">"
            if (closable) {
                html += "<button type=\"button\" class=\"close\" data-dismiss=\"alert\" aria-hidden=\"true\">&times;</button>"
            }
            html += "<p style='margin-bottom:15px;'>"
            html += "<i class=\"${icono} fa-2x pull-left text-shadow\"></i> "
            out << html << contenido << finHtml
        } else {
            out << ""
        }
    }

    /**
     * crea un datepicker
     *  attrs:
     *      class           clase
     *      name            name
     *      id              id (opcional, si no existe usa el mismo name)
     *      value           value (groovy Date o String)
     *      format          format para el Date (groovy)
     *      minDate         groovy Date o String. fecha mínima para el datepicker. cualquier cosa anterior se deshabilita
     *      maxDate         groovy Date o String. fecha máxima para el datepicker. cualquier cosa posterior se deshabilita
     *      orientation     String. Default: “auto”
     *                               A space-separated string consisting of one or two of “left” or “right”, “top” or “bottom”, and “auto” (may be omitted);
     *                                      for example, “top left”, “bottom” (horizontal orientation will default to “auto”), “right” (vertical orientation will default to “auto”),
     *                                      “auto top”. Allows for fixed placement of the picker popup.
     *                               “orientation” refers to the location of the picker popup’s “anchor”; you can also think of it as the location of the trigger element (input, component, etc)
     *                               relative to the picker.
     *                               “auto” triggers “smart orientation” of the picker.
     *                                  Horizontal orientation will default to “left” and left offset will be tweaked to keep the picker inside the browser viewport;
     *                                  vertical orientation will simply choose “top” or “bottom”, whichever will show more of the picker in the viewport.
     *      autoclose       boolean. default: true cierra automaticamente el datepicker cuando se selecciona una fecha
     *      todayHighlight  boolean. default: true marca la fecha actual
     *      beforeShowDay   funcion. funcion que se ejecuta antes de mostrar el día. se puede utilizar para deshabilitar una fecha en particular
     *                          ej:
     *                               beforeShowDay: function (date){*                                   if (date.getMonth() == (new Date()).getMonth())
     *                                       switch (date.getDate()){*                                           case 4:
     *                                               return {*                                                   tooltip: 'Example tooltip',
     *                                                   classes: 'active'
     *};
     *                                           case 8:
     *                                               return false;
     *                                           case 12:
     *                                               return "green";
     *}*}*                                }
     *      onChangeDate    funcion. funcion q se ejecuta al cambiar una fecha. se manda solo el nombre, sin parentesis, como parametro recibe el datepicker y el objeto
     *                          ej: onChangeDate="miFuncion"
     *                          function miFuncion($elm, e) {*
     *                              console.log($elm); //el objeto jquery del datepicker, el textfield
     *                              console.log(e); //el objeto que pasa el plugin
     *}*      daysOfWeekDisabled  lista de números para deshabilitar ciertos días: 0:domingo, 1:lunes, 2:martes, 3:miercoles, 4:jueves, 5:viernes, 6:sabado
     *      img             imagen del calendario. clase de glyphicons o font awsome
     **/
    def datepicker = { attrs ->
        def name = attrs.name
        def nameInput = name + "_input"
        def nameHiddenDay = name + "_day"
        def nameHiddenMonth = name + "_month"
        def nameHiddenYear = name + "_year"

        def nameHiddenHour = name + "_hour"
        def nameHiddenMin = name + "_minute"

        def id = nameInput
        if (attrs.id) {
            id = attrs.id
        }
        def readonly = attrs.readonly ?: true
        def value = attrs.value

        def clase = attrs["class"] ?: ""
        def claseGrupo = ""
        if (clase.contains("input-sm")) {
            claseGrupo = "input-group-sm"
        }

        def showDate = attrs.showDate ?: true
        def showTime = attrs.showTime ?: false

        def defaultFormat = "dd-MM-YYYY"
        if (showTime) {
            defaultFormat += " HH:mm"
        }

        def format = attrs.format ?: defaultFormat
        def formatJS = attrs.formatJS ?: format.replaceAll("d", "D")

        def startDate = attrs.minDate ?: false
        def endDate = attrs.maxDate ?: false

        def showMin = attrs.showMin ?: true

        def minStep = attrs.minStep ?: 1

        def orientation = attrs.orientation ?: "top auto"

        def todayHighlight = attrs.todayHighlight ?: true

        def beforeShowDay = attrs.beforeShowDay ?: false
        def onChangeDate = attrs.onChangeDate ?: false
        def onHide = attrs.onHide ?: false

        def daysOfWeekDisabled = attrs.daysOfWeekDisabled ?: false

        def img = attrs.img ?: "fa fa-calendar"

        def style = attrs.style ?: ""
        def placeholder = attrs.placeholder ?: ""

        def clear = attrs.clear && (attrs.clear == "true" || attrs.clear == true ||
            attrs.clear == "1" || attrs.clear == 1 ||
            attrs.clear == "Y" || attrs.clear == "S" ||
            attrs.clear == "y" || attrs.clear == "s")

        if (value instanceof Date) {
            value = value.format(format)
        }
        if (!value) {
            value = ""
        }

        def valueDay = "", valueMonth = "", valueYear = "", valueHour = "", valueMin = ""
        if (value != "") {
            if (showTime) {
                def parts = value.split(" ")
                def fecha, hora
                fecha = parts[0]
                if (parts.size() == 2) {
                    hora = parts[1]
                } else {
                    hora = new Date().format("HH:mm")
                }
                parts = fecha.split("-")
                if (parts.size() > 1) {
                    valueDay = parts[0]
                    valueMonth = parts[1]
                    valueYear = parts[2]
                }
                parts = hora.split(":")
                valueHour = parts[0]
                valueMin = parts[1]
            } else {
                def parts = value.split("-")
                valueDay = parts[0]
                valueMonth = parts[1]
                valueYear = parts[2]
            }
        }

        def br = "\n"

        def textfield = "<input type='text' name='${nameInput}' id='${id}' " + (readonly ? "readonly=''" : "") + " value='${value}'" +
            " class='${clase}' data-date-format='${formatJS}' placeholder='${placeholder}' style='${style}'/>"

        def hiddenDay = "<input type='hidden' name='${nameHiddenDay}' id='${nameHiddenDay}' value='${valueDay}'/>"
        def hiddenMonth = "<input type='hidden' name='${nameHiddenMonth}' id='${nameHiddenMonth}' value='${valueMonth}'/>"
        def hiddenYear = "<input type='hidden' name='${nameHiddenYear}' id='${nameHiddenYear}' value='${valueYear}'/>"

        def hiddenHour = "<input type='hidden' name='${nameHiddenHour}' id='${nameHiddenHour}' value='${valueHour}'/>"
        def hiddenMin = "<input type='hidden' name='${nameHiddenMin}' id='${nameHiddenMin}' value='${valueMin}'/>"

        def hidden = "<input type='hidden' name='${name}' id='${name}' value='date.struct'/>"

        def div = ""
        div += "<div class='input-group ${claseGrupo}'>" + br
        if (clear) {
            div += "<span class=\"input-group-btn\">"
            div += "<a href='#' class='btn btn-default btn-clear'><i class='fa fa-times'></i></a>"
            div += "</span>"
        }
        div += textfield + br

        div += hiddenDay + br
        div += hiddenMonth + br
        div += hiddenYear + br
        if (showTime) {
            div += hiddenHour + br
            div += hiddenMin + br
        }
        div += hidden + br

        div += "<span class=\"input-group-addon\"><i class=\"${img}\"></i></span>" + br
        div += "</div>" + br

        def js = "<script type=\"text/javascript\">" + br
        js += '$(".btn-clear").click(function() {' + br +
            '$(this).parent().parent().children("input").val("");' + br +
            '});' + br
        js += '$("#' + id + '").datetimepicker({' + br
        if (startDate) {
            if (startDate instanceof Date) {
                startDate = "moment(${startDate.format('dd/MM/yyyy')})"
            }
            js += "minDate: '${startDate}'," + br
        }
        if (endDate) {
            if (endDate instanceof Date) {
                endDate = "moment(${endDate.format('dd/MM/yyyy')})"
            }
            js += "maxDate: '${endDate}'," + br
        }
        js += 'pickDate: ' + showDate + ',' + br
        js += 'pickTime: ' + showTime + ',' + br
        js += 'useMinutes: ' + showMin + ',' + br
        js += 'useSeconds: false,' + br
        js += 'minuteStepping: ' + minStep + ',' + br
        js += 'sideBySide: true,' + br
        if (daysOfWeekDisabled) {
            js += "daysOfWeekDisabled: '${daysOfWeekDisabled}'," + br
        }
        if (beforeShowDay) {
            js += "beforeShowDay: function() { ${beforeShowDay}() }," + br
            js += "beforeShowDay: ${beforeShowDay}," + br
        }
        js += 'language: "es",' + br
        js += 'icons: {' + br
        js += 'time: "fa fa-clock-o",' + br
        js += 'date: "fa fa-calendar",' + br
        js += 'up: "fa fa-arrow-up",' + br
        js += 'down: "fa fa-arrow-down"' + br
        js += '},' + br
//        js += "format: '${formatJS}'," + br
        js += "orientation: '${orientation}'," + br
        js += "showToday: ${todayHighlight}" + br
        js += "}).on('dp.change', function(e) {" + br
//        js += 'console.log(e.date.date(),e.date.month(),e.date.year(), e.date.hour(), e.date.minute());'
        js += "var fecha = e.date;" + br
        js += "if(fecha) {" + br
        js += '$("#' + nameHiddenDay + '").val(fecha.date());' + br
        js += '$("#' + nameHiddenMonth + '").val(fecha.month() + 1);' + br
        js += '$("#' + nameHiddenYear + '").val(fecha.year());' + br
        if (showTime) {
            js += '$("#' + nameHiddenHour + '").val(fecha.hour());' + br
            js += '$("#' + nameHiddenMin + '").val(fecha.minute());' + br
        }
        js += '$(e.currentTarget).parents(".grupo").removeClass("has-error").find("label.help-block").hide();' + br
        js += "}" + br
        if (onChangeDate) {
            js += onChangeDate + "(\$(this), e);" + br
        }
        js += "})" + br
        js += ".on('dp.hide', function(e) {" + br
//        js += 'console.log(e.date.date(),e.date.month(),e.date.year(), e.date.hour(), e.date.minute());'
        if (onHide) {
            js += onHide + "(\$(this), e);" + br
        }
        js += "});" + br
        js += "</script>" + br

        out << div
        out << js
    }
}
