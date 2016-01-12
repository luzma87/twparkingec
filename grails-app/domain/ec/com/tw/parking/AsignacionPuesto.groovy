package ec.com.tw.parking

class AsignacionPuesto {

    Auto auto
    Puesto puesto
    Date fechaAsignacion
    Date fechaLiberacion

    static mapping = {
        table 'asignacion_puesto'
        cache usage: 'read-write', include: 'lazy'
        version false
        id generator: 'identity'
        sort auto: "asc"
        columns {
            id column: 'id'
            auto column: 'auto_id'
            puesto column: 'puesto_id'
            fechaAsignacion column: 'fecha_asignacion'
            fechaLiberacion column: 'fecha_liberacion'
        }
    }

    static constraints = {
        auto nullable: false
        puesto nullable: false
        fechaAsignacion nullable: false
        fechaLiberacion nullable: true
    }

    static List<AsignacionPuesto> obtenerOcupadosPorPreferenciaYedificio(TipoPreferencia preferencia, Edificio edificio) {
        return AsignacionPuesto.withCriteria {
            puesto {
                eq("edificio", edificio)
            }
            auto {
                usuario {
                    eq("preferencia", preferencia)
                }
            }
            isNull("fechaLiberacion")
        }
    }

    static List<AsignacionPuesto> obtenerPorDistancia(DistanciaEdificio distancia) {
        return AsignacionPuesto.withCriteria {
            puesto {
                edificio {
                    eq("distancia", distancia)
                }
            }
        }
    }


    String toString() {
        def persona = this.auto.usuario.nombre
        def placa = this.auto.placa
        def edificio = this.puesto.edificio.nombre
        def numero = this.puesto.numero
        return persona + " (" + placa + ") → " + edificio + " " + numero
    }
}
