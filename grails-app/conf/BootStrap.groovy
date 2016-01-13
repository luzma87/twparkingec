import grails.util.Environment

import ec.com.tw.parking.DistanciaEdificio
import ec.com.tw.parking.Edificio
import ec.com.tw.parking.Puesto
import ec.com.tw.parking.TipoPreferencia
import ec.com.tw.parking.TipoTransicion
import ec.com.tw.parking.Usuario
import ec.com.tw.parking.Auto

class BootStrap {

    def init = { servletContext ->

        if (Environment.current != Environment.TEST) {
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
                    def preferencia = new TipoPreferencia(datosPreferencia)
                    if (!preferencia.save()) {
                        println "Error al crear tipo preferencia: " + preferencia.errors
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

            if (Edificio.count() == 0) {
                listaEdificiosIniciales().each { datosEdificio ->
                    def edificio = new Edificio(datosEdificio.edificio)
                    if (!edificio.save()) {
                        println "Error al crear edificio: " + edificio.errors
                    } else {
                        datosEdificio.puestos.each { datosPuesto ->
                            def puesto = new Puesto(datosPuesto)
                            puesto.edificio = edificio
                            if (!puesto.save()) {
                                println "Error al crear puesto: " + puesto.errors
                            }
                        }
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
                    nombre     : "Carlos Oquendo",
                    email      : "coquendo@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "0721351797",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Renault",
                          modelo   : "Logan",
                          placa    : "PBQ-8392",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Mauricio Murillo",
                    email      : "mmurillo@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1714579305",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Spark",
                          placa    : "PBO-7783",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Fausto de la Torre",
                    email      : "ftorre@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1710876275",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Kia",
                          modelo   : "Sportage",
                          placa    : "PBJ-5195",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Fausto Castañeda",
                    email      : "fcastane@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : true,
                    cedula     : "1718198938",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Nissan",
                          modelo   : "Pathfinder",
                          placa    : "PYM-347",
                          tamanio  : "M",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Paola Jimenez",
                    email      : "pjimenez@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "0703594747",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Grand Vitara",
                          placa    : "PBF-9129",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Gustavo Eguez",
                    email      : "geguez@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1714060439",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Grand Vitara 5P",
                          placa    : "PCO-5724",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Gaby Cortez",
                    email      : "gcortez@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1717360281",
                    preferencia: TipoPreferencia.findByCodigo("N")
                ],
                auto   : [marca    : "Volkswagen",
                          modelo   : "Gol",
                          placa    : "PPA-1608",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Felipe Ureta",
                    email      : "fureta@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1707157093",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Vitara 3P",
                          placa    : "PCF-4689",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Iván Pazmiño",
                    email      : "ipazmino@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1713411278",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Vitara 5P",
                          placa    : "AAA-111",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Rodrigo Vallejo",
                    email      : "rvallejo@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1716368251",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Grand Vitara SZ",
                          placa    : "AAA-222",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "María Fernanda Escudero",
                    email      : "mescudero@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "0602772220",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Grand Vitara 5P",
                          placa    : "PBI-2312",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Viviana Perez",
                    email      : "vperez@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1720984630",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Hyundai",
                          modelo   : "Getz",
                          placa    : "POU-102",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Freddy Coronel",
                    email      : "fcoronel@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1712498847",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Toyota",
                          modelo   : "Fortuner",
                          placa    : "PBA-3793",
                          tamanio  : "G",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Luz Marina Unda",
                    email      : "lmunda@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1715068159",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Aveo",
                          placa    : "PCQ-8088",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Nelson Jumbo",
                    email      : "njumbo@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1718642174",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Esteem",
                          placa    : "PTE-0730",
                          tamanio  : "P",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Diego Alcocer",
                    email      : "dalcocer@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1715629778",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Ford",
                          modelo   : "F150",
                          placa    : "PBN-4713",
                          tamanio  : "G",
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Elena Echeverría",
                    email      : "iecheve@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    cedula     : "1802847440",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Nissan",
                          modelo   : "XTrail",
                          placa    : "PCL-5696",
                          tamanio  : "P",
                          esDefault: true]
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

    def listaEdificiosIniciales() {
        return [
            [
                edificio: [
                    nombre     : "Brescia",
                    distancia  : DistanciaEdificio.findByCodigo("M"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: "P", numero: "26", precio: 0],
                    [tamanio: "P", numero: "37", precio: 0],
                    [tamanio: "P", numero: "44", precio: 0],
                    [tamanio: "P", numero: "38", precio: 0],
                    [tamanio: "P", numero: "25", precio: 0],
                    [tamanio: "P", numero: "27", precio: 0],
                    [tamanio: "P", numero: "18", precio: 0],
                    [tamanio: "P", numero: "39", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre     : "Eloy Alfaro",
                    distancia  : DistanciaEdificio.findByCodigo("L"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: "G", numero: "6", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre     : "Praga 1",
                    distancia  : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: "G", numero: "9", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre     : "Praga 2",
                    distancia  : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: "G", numero: "22", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre     : "Le Parc",
                    distancia  : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable: true,
                    datosPago  : "Hotel Le Parc S.A.<br/>" +
                        "RUC: 1792086817001<br/>" +
                        "Dirección: República del Salvador 34-349 e Irlanda<br/>" +
                        "E-mail: tsanchez@leparc.com.ec<br/>" +
                        "Cuenta corriente Pacífico: 05265673"
                ],
                puestos : [
                    [tamanio: "G", numero: "s/n", precio: 70],
                    [tamanio: "G", numero: "s/n", precio: 70],
                    [tamanio: "G", numero: "s/n", precio: 70]
                ]
            ],
            [
                edificio: [
                    nombre     : "Samoa",
                    distancia  : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: "G", numero: "50", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre     : "Ardres",
                    distancia  : DistanciaEdificio.findByCodigo("L"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: "G", numero: "57", precio: 0]
                ]
            ]
        ]
    }
}
