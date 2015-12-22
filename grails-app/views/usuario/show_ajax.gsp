
<%@ page import="ec.com.tw.parking.Usuario" %>

<div class="modal-contenido">
    
    <g:if test="${usuarioInstance?.nombre}">
        <div class="row">
            <div class="col-sm-3 show-label">
                <g:message code="usuario.nombre.label" default="Nombre" />
            </div>
            
            <div class="col-sm-4">
                <g:fieldValue bean="${usuarioInstance}" field="nombre"/>
            </div>
            
        </div>
    </g:if>
    
    <g:if test="${usuarioInstance?.email}">
        <div class="row">
            <div class="col-sm-3 show-label">
                <g:message code="usuario.email.label" default="Email" />
            </div>
            
            <div class="col-sm-4">
                <g:fieldValue bean="${usuarioInstance}" field="email"/>
            </div>
            
        </div>
    </g:if>
    
    <g:if test="${usuarioInstance?.esAdmin}">
        <div class="row">
            <div class="col-sm-3 show-label">
                <g:message code="usuario.esAdmin.label" default="Es Admin" />
            </div>
            
            <div class="col-sm-4">
                <g:formatBoolean boolean="${usuarioInstance?.esAdmin}" true="Sí" false="No" />
            </div>
            
        </div>
    </g:if>
    
</div>