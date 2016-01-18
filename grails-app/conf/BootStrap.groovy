import ec.com.tw.parking.AsignacionPuesto
import ec.com.tw.parking.Tamanio
import grails.util.Environment

import ec.com.tw.parking.DistanciaEdificio
import ec.com.tw.parking.Edificio
import ec.com.tw.parking.Puesto
import ec.com.tw.parking.TipoPreferencia
import ec.com.tw.parking.TipoTransicion
import ec.com.tw.parking.Usuario

class BootStrap {

    def usuariosObj, edificiosObj;

    def init = { servletContext ->

        if (Environment.current != Environment.TEST) {
            if (DistanciaEdificio.count() == 0) {
                crearDistanciasEdificioIniciales()
            }

            if (TipoPreferencia.count() == 0) {
                crearTiposPreferenciaIniciales()
            }

            if (TipoTransicion.count() == 0) {
                crearTiposTransicionIniciales()
            }

            if (Usuario.count() == 0) {
                crearUsuariosIniciales()
            }

            if (Edificio.count() == 0) {
                crearEdificiosIniciales()
            }

            if (AsignacionPuesto.count() == 0) {
                crearAsignacionesIniciales()
            }
        }
    }
    def destroy = {
    }

    def crearUsuariosIniciales() {
        def usuarios = [
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.MEDIANO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.GRANDE,
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Luz Marina Unda",
                    email      : "lmunda@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : true,
                    cedula     : "1715068159",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Aveo",
                          placa    : "PCQ-8088",
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.PEQUENIO,
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
                          tamanio  : Tamanio.GRANDE,
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Elena Echeverría",
                    email      : "iecheve@thoughtworks.com",
                    password   : "123".encodeAsSHA256(),
                    esAdmin    : false,
                    estaActivo : false,
                    cedula     : "1802847440",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Nissan",
                          modelo   : "XTrail",
                          placa    : "PCL-5696",
                          tamanio  : Tamanio.PEQUENIO,
                          esDefault: true]
            ]
        ]
        usuarios.each { datosUsuario ->
            def usuario = new Usuario(datosUsuario.usuario)
            usuario.addToAutos(datosUsuario.auto)
            if (!usuario.save(flush: true)) {
                println "Error al crear usuario: " + usuario.errors
            }
        }
    }

    def crearDistanciasEdificioIniciales() {
        def distancias = [
            [codigo: 'M', descripcion: 'Matriz'],
            [codigo: 'C', descripcion: 'Cerca'],
            [codigo: 'L', descripcion: 'Lejos']
        ]
        distancias.each { datosDistancia ->
            def distanciaEdificio = new DistanciaEdificio(datosDistancia)
            if (!distanciaEdificio.save(flush: true)) {
                println "Error al crear distancia edificio: " + distanciaEdificio.errors
            }
        }
    }

    def crearTiposPreferenciaIniciales() {
        def preferencias = [
            [codigo: 'S', descripcion: 'Sale'],
            [codigo: 'N', descripcion: 'No sale']
        ]
        preferencias.each { datosPreferencia ->
            def preferencia = new TipoPreferencia(datosPreferencia)
            if (!preferencia.save(flush: true)) {
                println "Error al crear tipo preferencia: " + preferencia.errors
            }
        }
    }

    def crearTiposTransicionIniciales() {
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
        if (!tipoTransicion1.save(flush: true) || !tipoTransicion2.save(flush: true) || !tipoTransicion3.save(flush: true)) {
            println "Error al crear tipo transicion: "
            println "1: " + tipoTransicion1.errors
            println "2: " + tipoTransicion2.errors
            println "3: " + tipoTransicion3.errors
        }
    }

    def crearEdificiosIniciales() {
        def edificios = [
            [
                edificio: [
                    nombre     : "Brescia",
                    distancia  : DistanciaEdificio.findByCodigo("M"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: Tamanio.PEQUENIO, numero: "26", precio: 0],
                    [tamanio: Tamanio.PEQUENIO, numero: "37", precio: 0],
                    [tamanio: Tamanio.PEQUENIO, numero: "44", precio: 0],
                    [tamanio: Tamanio.PEQUENIO, numero: "38", precio: 0],
                    [tamanio: Tamanio.PEQUENIO, numero: "25", precio: 0],
                    [tamanio: Tamanio.PEQUENIO, numero: "27", precio: 0],
                    [tamanio: Tamanio.MEDIANO, numero: "18", precio: 0],
                    [tamanio: Tamanio.PEQUENIO, numero: "39", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre     : "Eloy Alfaro",
                    distancia  : DistanciaEdificio.findByCodigo("L"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "6", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre     : "Praga 1",
                    distancia  : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "9", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre     : "Praga 2",
                    distancia  : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "22", precio: 0]
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
                    [tamanio: Tamanio.GRANDE, numero: "1", precio: 70],
                    [tamanio: Tamanio.GRANDE, numero: "2", precio: 70],
                    [tamanio: Tamanio.GRANDE, numero: "3", precio: 70]
                ]
            ],
            [
                edificio: [
                    nombre     : "Samoa",
                    distancia  : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "50", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre     : "Ardres",
                    distancia  : DistanciaEdificio.findByCodigo("L"),
                    esAmpliable: false
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "57", precio: 0]
                ]
            ]
        ]
        edificios.each { datosEdificio ->
            def edificio = new Edificio(datosEdificio.edificio)
            datosEdificio.puestos.each { datosPuesto ->
                edificio.addToPuestos(datosPuesto)
            }
            if (!edificio.save(flush: true)) {
                println "Error al crear edificio: " + edificio.errors
            }
        }
    }

    def crearAsignacionesIniciales() {
        def edificioBrescia = Edificio.findByNombre("Brescia")
        def edificioEloyAlfaro = Edificio.findByNombre("Eloy Alfaro")
        def edificioPraga1 = Edificio.findByNombre("Praga 1")
        def edificioPraga2 = Edificio.findByNombre("Praga 2")
        def edificioSamoa = Edificio.findByNombre("Samoa")
        def edificioArdres = Edificio.findByNombre("Ardres")
        def edificioLeParc = Edificio.findByNombre("Le Parc")

        def asignaciones = []
        asignaciones += [usuario: Usuario.findByEmail("coquendo@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "26" }]
        asignaciones += [usuario: Usuario.findByEmail("mmurillo@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "37" }]
        asignaciones += [usuario: Usuario.findByEmail("ftorre@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "44" }]
        asignaciones += [usuario: Usuario.findByEmail("dalcocer@thoughtworks.com"),
                         puesto : edificioEloyAlfaro.puestos.find { it.numero == "6" }]
        asignaciones += [usuario: Usuario.findByEmail("pjimenez@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "38" }]
        asignaciones += [usuario: Usuario.findByEmail("geguez@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "25" }]
        asignaciones += [usuario: Usuario.findByEmail("gcortez@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "27" }]
        asignaciones += [usuario: Usuario.findByEmail("fureta@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "18" }]
        asignaciones += [usuario: Usuario.findByEmail("ipazmino@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "39" }]
        asignaciones += [usuario: Usuario.findByEmail("rvallejo@thoughtworks.com"),
                         puesto : edificioPraga1.puestos.find { it.numero == "9" }]
        asignaciones += [usuario: Usuario.findByEmail("mescudero@thoughtworks.com"),
                         puesto : edificioLeParc.puestos.find { it.numero == "1" }]
        asignaciones += [usuario: Usuario.findByEmail("vperez@thoughtworks.com"),
                         puesto : edificioSamoa.puestos.find { it.numero == "50" }]
        asignaciones += [usuario: Usuario.findByEmail("fcoronel@thoughtworks.com"),
                         puesto : edificioArdres.puestos.find { it.numero == "57" }]
        asignaciones += [usuario: Usuario.findByEmail("lmunda@thoughtworks.com"),
                         puesto : edificioLeParc.puestos.find { it.numero == "2" }]
        asignaciones += [usuario: Usuario.findByEmail("njumbo@thoughtworks.com"),
                         puesto : edificioPraga2.puestos.find { it.numero == "22" }]
        asignaciones += [usuario: Usuario.findByEmail("fcastane@thoughtworks.com"),
                         puesto : edificioLeParc.puestos.find { it.numero == "3" }]

        asignaciones.each { asignacion ->
            crearAsignacion(asignacion.usuario, asignacion.puesto)
        }
    }

    def crearAsignacion(Usuario usuario, Puesto puesto) {
        def auto = usuario.autos.first()
        def asignacion = new AsignacionPuesto()
        asignacion.fechaAsignacion = new Date()
        asignacion.auto = auto
        asignacion.puesto = puesto
        if (!asignacion.save(flush: true)) {
            println "error al guardar asignacion de ${usuario.toString()} a ${puesto.toString()}"
        }
    }
}
