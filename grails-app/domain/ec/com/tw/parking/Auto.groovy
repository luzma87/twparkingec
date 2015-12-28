package ec.com.tw.parking

class Auto {

    Usuario usuario
    String marca
    String modelo
    String placa
    String tamanio

    static constraints = {
        usuario nullable: false
        marca nullable: false, blank: false, minSize: 2, maxSize: 20
        modelo nullable: false, blank: false, minSize: 2, maxSize: 25
        placa nullable: false, blank: false, minSize: 8, maxSize: 8
        tamanio nullable: false, blank: false, maxSize: 1, inList: ["P", "G"]
    }
}
