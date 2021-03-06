package ec.com.tw.parking

import ec.com.tw.parking.enums.Mes
import ec.com.tw.parking.enums.Tamanio

/**
 * Created by lmunda on 1/21/16 09:31.
 */
class DatosIniciales {
    static crearDistanciasEdificio() {
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

    static crearTiposPreferencia() {
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

    static crearTiposTransicion() {
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

    static crearUsuariosYautos() {
        def usuarios = [
            [
                usuario: [
                    nombre     : "Carlos Oquendo",
                    email      : "coquendo@thoughtworks.com",
                    password   : "4acd83d500f90a0521e6191e38791012faf6a4aa84aa55a87c5a2b4d5fb92a5f",
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
                    password   : "c1616893dbf9fdd91e728fc931057393c0aeac0eba923e068aeae49abf62050d",
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
                    password   : "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
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
                    password   : "3b52c72b8af978151bd4922da047a134f86697890ed4634753419f8b66a52f4c",
                    esAdmin    : true,
                    estaActivo : false,
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
                    nombre     : "Paola Jiménez",
                    email      : "pjimenez@thoughtworks.com",
                    password   : "8d969eef6ecad3c29a3a629280e686cf0c3f5d5a86aff3ca12020c923adc6c92",
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
                    password   : "1d2b3bf2ab6a095d189ca3fc77f26ca749128260101d0d420a5d45ae4d2a6c03",
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
                    nombre     : "Gabriela Cortez",
                    email      : "gcortez@thoughtworks.com",
                    password   : "707bcb3450b65394e39b64043df76de760fc8c69d295176a1262b324dbc04d56",
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
                    password   : "8e2819f7b8a663b2b032db43fa646b83ca7dcf2517cbf6fe7605ca01177709b5",
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
                    password   : "475c287ef3b55df794ed88d1a9503de277f83a3ab26cfd0740aefa72769630bd",
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
                    password   : "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
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
                    password   : "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
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
                    password   : "644ea5db3dedb92479a2eb323d466f0365116cc5ffbe8d5aad5f4afad8cfe281",
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
                    password   : "97d7d401e943aa7ee174dc82e645ac04c64a0ce1e03d42790c2e8cca18b10fe3",
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
                    password   : "13f20255b0a8953b3bd8641d27621d5b80533698973fa58e7f7729273a425c49",
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
                    password   : "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
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
                    password   : "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
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
                    password   : "a665a45920422f9d417e4867efdc4fb8a04a1f3fff1fa07e998e86f7f7a27ae3",
                    esAdmin    : false,
                    cedula     : "1802847440",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Nissan",
                          modelo   : "XTrail",
                          placa    : "PCL-5696",
                          tamanio  : Tamanio.PEQUENIO,
                          esDefault: true]
            ],
            [
                usuario: [
                    nombre     : "Ramiro Castillo",
                    email      : "rcastill@thoughtworks.com",
                    password   : "c8bb399bd7aa8247238134a29a82fcb87598cd9454b82ac105e41a7d10ffe98e",
                    esAdmin    : false,
                    cedula     : "1717153744",
                    preferencia: TipoPreferencia.findByCodigo("S")
                ],
                auto   : [marca    : "Chevrolet",
                          modelo   : "Aveo",
                          placa    : "PCP-9892",
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

    static crearEdificiosYpuestos() {
        def edificios = [
            [
                edificio: [
                    nombre       : "Brescia",
                    distancia    : DistanciaEdificio.findByCodigo("M"),
                    esAmpliable  : false,
                    observaciones: "Retirar tarjeta de acceso y control para la puerta"
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
                    nombre       : "Eloy Alfaro",
                    distancia    : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable  : false,
                    observaciones: "Retirar tarjeta de acceso y control para la puerta"
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "6", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre       : "Praga 1",
                    distancia    : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable  : false,
                    observaciones: "Retirar tarjeta de acceso y control para la puerta"
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "9", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre       : "Praga 2",
                    distancia    : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable  : false,
                    observaciones: "Retirar tarjeta de acceso y control para la puerta"
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
                    nombre       : "Samoa",
                    distancia    : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable  : false,
                    observaciones: "Retirar tarjeta de acceso y control para la puerta"
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "50", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre       : "Ardres",
                    distancia    : DistanciaEdificio.findByCodigo("L"),
                    esAmpliable  : false,
                    observaciones: "Retirar tarjeta de acceso y control para la puerta"
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "57", precio: 0]
                ]
            ],
            [
                edificio: [
                    nombre       : "Plaza Real",
                    distancia    : DistanciaEdificio.findByCodigo("C"),
                    esAmpliable  : false,
                    observaciones: "Retirar tarjeta de acceso y control para la puerta"
                ],
                puestos : [
                    [tamanio: Tamanio.GRANDE, numero: "358", precio: 0]
                ]
            ]
        ]
        edificios.each { datosEdificio ->
            def edificio = new Edificio(datosEdificio.edificio)
            datosEdificio.puestos.each { datosPuesto ->
                datosPuesto.estaActivo = true
                edificio.addToPuestos(datosPuesto)
            }
            if (!edificio.save(flush: true)) {
                println "Error al crear edificio: " + edificio.errors
            }
        }
    }

    static crearAsignaciones() {
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
        asignaciones += [usuario: Usuario.findByEmail("fureta@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "38" }]
        asignaciones += [usuario: Usuario.findByEmail("geguez@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "25" }]
        asignaciones += [usuario: Usuario.findByEmail("gcortez@thoughtworks.com"),
                         puesto : edificioBrescia.puestos.find { it.numero == "27" }]
        asignaciones += [usuario: Usuario.findByEmail("pjimenez@thoughtworks.com"),
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

        asignaciones.each { asg ->
            def auto = asg.usuario.autos.first()
            def asignacion = new AsignacionPuesto()
            asignacion.fechaAsignacion = new Date()
            asignacion.auto = auto
            asignacion.puesto = asg.puesto
            if (!asignacion.save(flush: true)) {
                println "error al guardar asignacion de ${asg.usuario.toString()} a ${asg.puesto.toString()}"
            }
        }
    }

    static crearPagos() {
        def pagos = []
        pagos += [usuario: Usuario.findByEmail("coquendo@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("mmurillo@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '25-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("ftorre@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '29-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '25-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("dalcocer@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '29-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '28-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("fureta@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("geguez@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '25-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("gcortez@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '26-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("pjimenez@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '23-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '25-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("ipazmino@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("rvallejo@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '29-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '28-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("mescudero@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '21-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("vperez@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '28-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("fcoronel@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '27-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("lmunda@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '26-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("njumbo@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ],
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '25-01-2016'),
                          monto    : 12.38
                      ],
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("fcastane@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.ENERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '22-12-2015'),
                          monto    : 13.16
                      ]
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("rcastill@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '26-12-2015'),
                          monto    : 12.38
                      ]
                  ]
        ]
        pagos += [usuario: Usuario.findByEmail("iecheve@thoughtworks.com"),
                  pagos  : [
                      [
                          mes      : Mes.FEBRERO,
                          anio     : 2016,
                          fechaPago: new Date().parse('dd-MM-yyyy', '25-12-2015'),
                          monto    : 12.38
                      ]
                  ]
        ]

        pagos.each { datosPagos ->

            datosPagos.pagos.each { datosPago ->
                def pago = new Pago(datosPago)
                pago.usuario = datosPagos.usuario
                if (!pago.save(flush: true)) {
                    println "error al guardar pago de ${pago.usuario.toString()} de ${datosPago.mes}"
                }
            }
        }
    }
}
