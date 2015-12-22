/**
 * Created by lmunda on 12/22/15.
 */

$(function () {
    //hace q todos los elementos con un atributo title tengan el title bonito de qtip2
    $('[title!=""]').qtip({
        style    : {
            classes : 'qtip-tipsy'
        },
        position : {
            my : "bottom center",
            at : "top center"
        }
    });

    //hace q los inputs q tienen maxlenght muestren la cantidad de caracteres utilizados/caracterres premitidos
    $('[maxlength]').maxlength({
        alwaysShow        : true,
        warningClass      : "label label-success",
        limitReachedClass : "label label-danger",
        placement         : 'top'
    });

    //para los dialogs, setea los defaults
    bootbox.setDefaults({
        locale      : "es",
        closeButton : false,
        show        : true
    });
});