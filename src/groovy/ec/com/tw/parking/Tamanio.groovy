package ec.com.tw.parking

/**
 * Created by lmunda on 1/14/16 09:43.
 */
public enum Tamanio {
    PEQUENIO("P", 1),
    MEDIANO("M", 2),
    GRANDE("G", 3)

    String codigo
    Integer valor

    public Tamanio(codigo, valor) {
        this.codigo = codigo
        this.valor = valor
    }

    String getCodigo() {
        return codigo
    }

    Integer getValor() {
        return valor
    }

    def getTamaniosCompatibles() {
        switch (this) {
            case PEQUENIO:
                return [PEQUENIO, MEDIANO, GRANDE]
                break
            case MEDIANO:
                return [MEDIANO, GRANDE]
                break
            case GRANDE:
                return [GRANDE]
                break
        }
    }
}
