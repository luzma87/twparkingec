import ec.com.tw.parking.DistanciaEdificio
import ec.com.tw.parking.TipoPreferencia
import ec.com.tw.parking.TipoTransicion
import ec.com.tw.parking.Usuario

class BootStrap {

    def init = { servletContext ->

        if (DistanciaEdificio.count() == 0) {
            listaDistanciasEdificioIniciales().each { datosDistancia ->
                def distanciaEdificio = new DistanciaEdificio(datosDistancia)
                if (!distanciaEdificio.save()) {
                    println "Error al crear distancia edificio: " + distanciaEdificio.errors
                }
            }
        }

        if (TipoPreferencia.count() == 0) {
            listaTiposPreferenciaIniciales().each { datosPreferencia ->
                def tipoPreferencia = new TipoPreferencia(datosPreferencia)
                if (!tipoPreferencia.save()) {
                    println "Error al crear tipo preferencia: " + tipoPreferencia.errors
                }
            }
        }

        if (TipoTransicion.count() == 0) {
            def tipoTransicion1 = new TipoTransicion([
                nombre          : "Lejos a Matriz",
                distanciaOrigen : DistanciaEdificio.findByCodigo("L"),
                distanciaDestino: DistanciaEdificio.findByCodigo("M"),
                prioridad       : 1
            ])
            def tipoTransicion2 = new TipoTransicion([
                nombre          : "Cerca a Lejos",
                distanciaOrigen : DistanciaEdificio.findByCodigo("C"),
                distanciaDestino: DistanciaEdificio.findByCodigo("L"),
                prioridad       : 2
            ])
            def tipoTransicion3 = new TipoTransicion([
                nombre          : "Matriz a Cerca",
                distanciaOrigen : DistanciaEdificio.findByCodigo("M"),
                distanciaDestino: DistanciaEdificio.findByCodigo("C"),
                prioridad       : 3
            ])
            if (!tipoTransicion1.save() || !tipoTransicion2.save() || !tipoTransicion3.save()) {
                println "Error al crear tipo transicion: "
                println "1: " + tipoTransicion1.errors
                println "2: " + tipoTransicion2.errors
                println "3: " + tipoTransicion3.errors
            }
        }

        if (Usuario.count() == 0) {
            listaUsuariosIniciales().each { datosUsuario ->
                def usuario = new Usuario(datosUsuario.usuario)
                if (!usuario.save()) {
                    println "Error al crear usuario: " + usuario.errors
                } else {
                    def auto = new Auto(datosUsuario.auto)
                    auto.usuario = usuario
                    if (!auto.save()) {
                        println "Error al crear auto: " + auto.errors
                    }
                }
            }
        }
    }
    def destroy = {
    }

    def listaUsuariosIniciales() {
        return [
            [
                usuario: [
                    nombre  : "Carlos Oquendo",
                    email   : "coquendo@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "0721351797"
                ],
                auto   : [marca  : "Renault",
                          modelo : "Logan",
                          placa  : "PBQ-8392",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Mauricio Murillo",
                    email   : "mmurillo@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1714579305"
                ],
                auto   : [marca  : "Chevrolet",
                          modelo : "Spark",
                          placa  : "PBO-7783",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Fausto de la Torre",
                    email   : "ftorre@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1710876275"
                ],
                auto   : [marca  : "Kia",
                          modelo : "Sportage",
                          placa  : "PBJ-5195",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Fausto Castañeda",
                    email   : "fcastane@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : true,
                    cedula  : "1718198938"
                ],
                auto   : [marca  : "Nissan",
                          modelo : "Pathfinder",
                          placa  : "PYM-347",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Paola Jimenez",
                    email   : "pjimenez@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "0703594747"
                ],
                auto   : [marca  : "Chevrolet",
                          modelo : "Grand Vitara",
                          placa  : "PBF-9129",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Gustavo Eguez",
                    email   : "geguez@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1714060439"
                ],
                auto   : [marca  : "Chevrolet",
                          modelo : "Grand Vitara 5P",
                          placa  : "PCO-5724",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Gaby Cortez",
                    email   : "gcortez@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1717360281"
                ],
                auto   : [marca  : "Volkswagen",
                          modelo : "Gol",
                          placa  : "PPA-1608",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Felipe Ureta",
                    email   : "fureta@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1707157093"
                ],
                auto   : [marca  : "Chevrolet",
                          modelo : "Vitara 3P",
                          placa  : "PCF-4689",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Iván Pazmiño",
                    email   : "ipazmino@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1234567898"
                ],
                auto   : [marca  : "Chevrolet",
                          modelo : "Vitara 5P",
                          placa  : "AAA-111",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Rodrigo Vallejo",
                    email   : "rvallejo@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1716368251"
                ],
                auto   : [marca  : "Chevrolet",
                          modelo : "Grand Vitara SZ",
                          placa  : "AAA-222",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "María Fernanda Escudero",
                    email   : "mescudero@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "0602772220"
                ],
                auto   : [marca  : "Chevrolet",
                          modelo : "Grand Vitara 5P",
                          placa  : "PBI-2312",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Viviana Perez",
                    email   : "vperez@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1720984630"
                ],
                auto   : [marca  : "Hyundai",
                          modelo : "Getz",
                          placa  : "POU-102",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Freddy Coronel",
                    email   : "fcoronel@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1234567812"
                ],
                auto   : [marca  : "Toyota",
                          modelo : "Fortuner",
                          placa  : "PBA-3793",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Luz Marina Unda",
                    email   : "lmunda@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1715068159"
                ],
                auto   : [marca  : "Chevrolet",
                          modelo : "Aveo",
                          placa  : "PCQ-8088",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Nelson Jumbo",
                    email   : "njumbo@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1718642174"
                ],
                auto   : [marca  : "Chevrolet",
                          modelo : "Esteem",
                          placa  : "PTE-0730",
                          tamanio: "P"]
            ],
            [
                usuario: [
                    nombre  : "Diego Alcocer",
                    email   : "dalcocer@thoughtworks.com",
                    password: "123".encodeAsSHA256(),
                    esAdmin : false,
                    cedula  : "1234567815"
                ],
                auto   : [marca  : "Ford",
                          modelo : "F150",
                          placa  : "PBN-4713",
                          tamanio: "P"]
            ]
        ]
    }

    def listaDistanciasEdificioIniciales() {
        return [
            [codigo: 'M', descripcion: 'Matriz'],
            [codigo: 'C', descripcion: 'Cerca'],
            [codigo: 'L', descripcion: 'Lejos']
        ]
    }

    def listaTiposPreferenciaIniciales() {
        return [
            [codigo: 'S', descripcion: 'Sale'],
            [codigo: 'N', descripcion: 'No sale']
        ]
    }
}
