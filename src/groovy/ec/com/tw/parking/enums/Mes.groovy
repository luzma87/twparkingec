package ec.com.tw.parking.enums

/**
 * Created by lmunda on 2/1/16 09:59.
 */
public enum Mes {
    ENERO("enero", 1),
    FEBRERO("febrero", 2),
    MARZO("marzo", 3),
    ABRIL("abril", 4),
    MAYO("mayo", 5),
    JUNIO("junio", 6),
    JULIO("julio", 7),
    AGOSTO("agosto", 8),
    SEPTIEMBRE("septiembre", 9),
    OCTUBRE("octubre", 10),
    NOVIEMBRE("noviembre", 11),
    DICIEMBRE("diciembre", 12)

    String nombre
    Integer numero

    public Mes(nombre, numero) {
        this.nombre = nombre
        this.numero = numero
    }

    String getNombre() {
        return nombre
    }

    Integer getNumero() {
        return numero
    }
}
