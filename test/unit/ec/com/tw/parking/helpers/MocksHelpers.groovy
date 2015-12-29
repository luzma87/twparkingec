package ec.com.tw.parking.helpers

/**
 * Created by lmunda on 12/29/15 15:30.
 */
class MocksHelpers {
    static mockObjeto(crudHelperServiceMock, expectedReturn) {
        crudHelperServiceMock.demand.obtenerObjeto { dominio, id -> return expectedReturn }
        return crudHelperServiceMock
    }

    static mockGuardarObjeto(crudHelperServiceMock, expectedReturn) {
        crudHelperServiceMock.demand.guardarObjeto { entidad, objeto, params ->
            objeto.properties = params
            objeto.save(flush: true)
            return expectedReturn
        }
    }

    static mockEliminarObjeto(crudHelperServiceMock, expectedReturn) {
        crudHelperServiceMock.demand.eliminarObjeto { entidad, objeto ->
            objeto.delete(flush: true)
            return expectedReturn
        }
    }
}
