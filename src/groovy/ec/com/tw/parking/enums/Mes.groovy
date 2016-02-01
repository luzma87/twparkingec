package ec.com.tw.parking.enums

/**
 * Created by lmunda on 2/1/16 09:59.
 */
public enum Mes {
    ENERO(1),
    FEBRERO(2),
    MARZO(3),
    ABRIL(4),
    MAYO(5),
    JUNIO(6),
    JULIO(7),
    AGOSTO(8),
    SEPTIEMBRE(9),
    OCTUBRE(10),
    NOVIEMBRE(11),
    DICIEMBRE(12)

    Integer numero

    public Mes(numero) {
        this.numero = numero
    }

    Integer getNumero() {
        return numero
    }

    static Mes obtenerPorNumero(numero) {
        values().find { it.numero == numero.toInteger() }
    }
}
