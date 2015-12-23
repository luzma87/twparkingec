<%=packageName%>
<% import grails.persistence.Event %>
<g:if test="\${${propertyName}}">
    <script type="text/javascript" src="\${resource(dir: 'js', file: 'ui.js')}"></script>
    <%  excludedProps = Event.allEvents.toList() << 'version' << 'dateCreated' << 'lastUpdated'
    persistentPropNames = domainClass.persistentProperties*.name
    boolean hasHibernate = pluginManager?.hasGrailsPlugin('hibernate') || pluginManager?.hasGrailsPlugin('hibernate4')
    if (hasHibernate) {
        def GrailsDomainBinder = getClass().classLoader.loadClass('org.codehaus.groovy.grails.orm.hibernate.cfg.GrailsDomainBinder')
        if (GrailsDomainBinder.newInstance().getMapping(domainClass)?.identity?.generator == 'assigned') {
            persistentPropNames << domainClass.identifier.name
        }
    }
    props = domainClass.properties.findAll { persistentPropNames.contains(it.name) && !excludedProps.contains(it.name) && (domainClass.constrainedProperties[it.name] ? domainClass.constrainedProperties[it.name].display : true) }
    uniques = []
    %>
    <div class="modal-contenido">
        <g:form class="form-horizontal" name="frm${domainClass.propertyName.capitalize()}" id="\${${propertyName}?.id}"
                role="form" action="save_ajax" method="POST">
            <%
                Collections.sort(props, comparator.constructors[0].newInstance([domainClass] as Object[]))
                for (p in props) {
                    if (p.embedded) {
                        def embeddedPropNames = p.component.persistentProperties*.name
                        def embeddedProps = p.component.properties.findAll { embeddedPropNames.contains(it.name) && !excludedProps.contains(it.name) }
                        Collections.sort(embeddedProps, comparator.constructors[0].newInstance([p.component] as Object[]))
            %><fieldset class="embedded"><legend><g:message code="${domainClass.propertyName}.${p.name}.label" /></legend><%
                for (ep in p.component.properties) {
                    renderFieldForProperty(ep, p.component, "${p.name}.")
                }
        %></fieldset><%
                } else {
                    renderFieldForProperty(p, domainClass)
                }
            }
            private renderFieldForProperty(p, owningClass, prefix = "") {
                boolean hasHibernate = pluginManager?.hasGrailsPlugin('hibernate') || pluginManager?.hasGrailsPlugin('hibernate4')
                boolean display = true
                boolean required = false
                boolean number = false
                boolean date = false
                boolean unique = false
                int size = 6
                if (hasHibernate) {
                    cp = owningClass.constrainedProperties[p.name]
                    display = (cp ? cp.display : true)
                    required = (cp ? !(cp.propertyType in [boolean, Boolean]) && !cp.nullable : false)
                    number = (cp ? Number.isAssignableFrom(cp.propertyType) || (cp.propertyType?.isPrimitive() && cp. propertyType != boolean) : false)
                    date = (cp ? (cp.propertyType == Date || cp.propertyType == java.sql.Date || cp.propertyType == java.sql.Time || cp.propertyType == Calendar) : false)
                    unique = p.name.contains('codigo') || p.name.contains('login') || p.name.contains('mail') || p.name.contains('email')
                    if(unique) {
                        uniques += p
                    }
                    if(number){
                        size = 2
                    } else if(date) {
                        size = 4
                    }
                    if(props.size() >= 10) {
                        size = size + 1
                    }
                }
                if (display) { %>
            <div class="row">
                <div class="col-md-12 form-group \${hasErrors(bean: ${propertyName}, field: '${prefix}${p.name}', 'error')} ${required ? 'required' : ''}">
                    <label for="${prefix}${p.name}" class="col-md-4 control-label">
                        <g:message code="${domainClass.propertyName}.${prefix}${p.name}.label" />
                        <% if (required) { %><span class="required-indicator">*</span><% } %>
                    </label>
                    <div class="col-md-8">
                        ${renderEditor(p)}
                    </div>
                </div>
            </div>
            <% }   } %>
        </g:form>
    </div>

    <script type="text/javascript">
        var validator = \$("#frm${domainClass.propertyName.capitalize()}").validate({
            errorClass     : "help-block",
            errorPlacement : function (error, element) {
                if (element.parent().hasClass("input-group")) {
                    error.insertAfter(element.parent());
                } else {
                    error.insertAfter(element);
                }
                element.parents(".grupo").addClass('has-error');
            },
            success        : function (label) {
                label.parents(".grupo").removeClass('has-error');
                label.remove();
            }
        });
        \$(".form-control").keydown(function (ev) {
            if (ev.keyCode == 13) {
                guardar${domainClass.propertyName.capitalize()}();
                return false;
            }
            return true;
        });
    </script>
</g:if>
<g:else>
    <msgBuilder:noEncontradoHtml entidad="${domainClass.propertyName}"/>
</g:else>