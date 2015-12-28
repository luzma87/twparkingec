package ec.com.tw.parking

class Auto {

    String marca
    String modelo
    String placa
    String tamanio

    //TODO: prueba de integracion delete usuario => delete auto
    static belongsTo = [usuario: Usuario]

    static constraints = {
        marca nullable: false, blank: false, minSize: 2, maxSize: 20
        modelo nullable: false, blank: false, minSize: 2, maxSize: 25
        placa nullable: false, blank: false, minSize: 8, maxSize: 8
        tamanio nullable: false, blank: false, maxSize: 1, inList: ["P", "G"]
    }

    String toString() {
        return this.marca + " " + this.modelo
    }

}
