package ec.com.tw.parking

class AsignacionPuesto {

    Auto auto
    Puesto puesto
    Date fechaAsignacion = new Date()
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

    static List<AsignacionPuesto> obtenerOcupadosPorDistanciaYpreferenciaSale(DistanciaEdificio distancia) {
        def preferenciaSale = TipoPreferencia.findByCodigo("S")
        return AsignacionPuesto.withCriteria {
            puesto {
                edificio {
                    eq("distancia", distancia)
                }
            }
            auto {
                usuario {
                    eq("preferencia", preferenciaSale)
                }
            }
            isNull("fechaLiberacion")
        }
    }

    static int contarOcupadosPorPrioridad(int prioridad) {
        def distanciaOrigen = TipoTransicion.findByPrioridad(prioridad).distanciaOrigen
        return obtenerOcupadosPorDistanciaYpreferenciaSale(distanciaOrigen).count { it.fechaLiberacion == null }
    }

    static int contarLibresPorPrioridad(int prioridad) {
        def distanciaOrigen = TipoTransicion.findByPrioridad(prioridad).distanciaOrigen
        return obtenerOcupadosPorDistanciaYpreferenciaSale(distanciaOrigen).count {
            it.fechaLiberacion != null && it.fechaLiberacion < new Date()
        }
    }

    String toString() {
        def persona = this.auto.usuario.nombre
        def placa = this.auto.placa
        def edificio = this.puesto.edificio.nombre
        def numero = this.puesto.numero
        return persona + " (" + placa + ") → " + edificio + " #" + numero
    }

    def liberar() {
        if (this.fechaLiberacion == null) {
            this.fechaLiberacion = new Date()
            if (!this.save()) {
                println this.errors
            }
        }
    }
}
