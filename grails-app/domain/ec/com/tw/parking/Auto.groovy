package ec.com.tw.parking

class Auto {

//    Usuario usuario
    String marca
//    String modelo
//    String placa
//    String tamanio

    static constraints = {
        marca nullable: false, blank: false, minSize: 2, maxSize: 20
    }
}
