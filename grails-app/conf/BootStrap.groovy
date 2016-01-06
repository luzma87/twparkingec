import ec.com.tw.parking.DistanciaEdificio
import ec.com.tw.parking.TipoPreferencia
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

        if (Usuario.count() == 0) {
            listaUsuariosIniciales().each { datosUsuario ->
                def usuario = new Usuario(datosUsuario)
                if (!usuario.save()) {
                    println "Error al crear usuario: " + usuario.errors
                }
            }
        }
    }
    def destroy = {
    }

    def listaUsuariosIniciales() {
        return [
            [
                nombre  : "Carlos Oquendo",
                email   : "coquendo@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "0721351797"
            ],
            [
                nombre  : "Mauricio Murillo",
                email   : "mmurillo@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1714579305"
            ],
            [
                nombre  : "Fausto de la Torre",
                email   : "ftorre@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1710876275"
            ],
            [
                nombre  : "Fausto Castañeda",
                email   : "fcastane@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : true,
                cedula  : "1718198938"
            ],
            [
                nombre  : "Paola Jimenez",
                email   : "pjimenez@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "0703594747"
            ],
            [
                nombre  : "Gustavo Eguez",
                email   : "geguez@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1714060439"
            ],
            [
                nombre  : "Gaby Cortez",
                email   : "gcortez@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1717360281"
            ],
            [
                nombre  : "Felipe Ureta",
                email   : "fureta@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1707157093"
            ],
            [
                nombre  : "Iván Pazmiño",
                email   : "ipazmino@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1234567898"
            ],
            [
                nombre  : "Rodrigo Vallejo",
                email   : "rvallejo@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1716368251"
            ],
            [
                nombre  : "María Fernanda Escudero",
                email   : "mescudero@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "0602772220"
            ],
            [
                nombre  : "Viviana Perez",
                email   : "vperez@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1720984630"
            ],
            [
                nombre  : "Freddy Coronel",
                email   : "fcoronel@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1234567812"
            ],
            [
                nombre  : "Luz Marina Unda",
                email   : "lmunda@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1715068159"
            ],
            [
                nombre  : "Nelson Jumbo",
                email   : "njumbo@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1718642174"
            ],
            [
                nombre  : "Diego Alcocer",
                email   : "dalcocer@thoughtworks.com",
                password: "123".encodeAsSHA256(),
                esAdmin : false,
                cedula  : "1234567815"
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
