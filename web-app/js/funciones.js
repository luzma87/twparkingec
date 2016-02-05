/**
 * Created by lmunda on 12/22/15.
 */

/**
 * muestra notificaciones flotantes
 * @param msg: el mensaje a mostrar
 * @param type: tipo de mensaje: 'error' o 'success'
 * @param title: el titulo del mensaje (opcional)
 * @param hide: si se oculta solo o no (opcional)
 */
function log(msg, type, title, hide) {
    type = type.toLowerCase();
    //if (hide === undefined) {
    //    hide = type != "error";
    //}
    //if (!title) {
    //    title = type == 'error' ? "Ha ocurrido un error" : "Transacción exitosa";
    //}
    var icon = type == 'error' ? "fa fa-warning fa-2x" : "fa fa-check fa-2x";
    if (msg === undefined) {
        msg = "";
    }
    new PNotify({
        title   : title,
        icon    : icon,
        buttons : {
            closer_hover  : false,
            sticker_hover : false
        },
        styling : 'fontawesome',
        text    : msg,
        type    : type
    });
}

function openLoader(msg, title) {
    msg = $.trim(msg);
    title = $.trim(title);
    var $msg = $("<div/>");
    $msg.addClass("text-center");
    if (msg !== undefined && msg != "") {
        $msg.append("<p>" + msg + "</p>");
    }
    if (title === undefined || title == "") {
        title = false;
    }

    $msg.append('<i class="fa fa-5x icon-debian fa-spin"></i>'); //fa-circle-o-notch

    bootbox.dialog({
        id          : 'dlgLoader',
        title       : title,
        message     : $msg,
        closeButton : false,
        class       : "modal-sm"
    });
    $("#dlgLoader").css({zIndex : 1061});
    $(".modal-backdrop").css({zIndex : 1060});
}
function closeLoader() {
    $("#dlgLoader").modal('hide');
    $(".modal-backdrop").css({zIndex : 1040});
}