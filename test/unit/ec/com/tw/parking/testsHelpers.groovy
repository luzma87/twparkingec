package ec.com.tw.parking

import org.apache.commons.lang.RandomStringUtils

/**
 * Created by lmunda on 12/23/15 14:49.
 */
class TestsHelpers {

    static def getRandomString(int min, int max, boolean allChars) {
        def random = new Random()
        def length = random.nextInt(max - min) + min
        if (allChars) {
            return RandomStringUtils.random(length)
        }
        return RandomStringUtils.randomAlphabetic(length)
    }

    static def getRandomString(int numeroCaracteres) {
        return RandomStringUtils.randomAlphabetic(numeroCaracteres)
    }

    static def getRandomMail() {
        return getRandomString(3, 90, false) + "@test.com"
    }

    static def getRandomAdmin() {
        def random = new Random()
        return random.nextBoolean()
    }

    static def getValidUsuario() {
        return new Usuario([nombre  : getRandomString(3, 50, false),
                            email   : getRandomMail(),
                            password: getRandomString(3, 512, true),
                            esAdmin : getRandomAdmin()])
    }

    static def mockObjeto(crudHelperServiceMock, expectedReturn) {
        crudHelperServiceMock.demand.obtenerObjeto { dominio, id -> return expectedReturn }
        return crudHelperServiceMock
    }

    static def mockGuardarObjeto(crudHelperServiceMock, expectedReturn) {
        crudHelperServiceMock.demand.guardarObjeto { entidad, objeto, params ->
            objeto.properties = params
            objeto.save(flush: true)
            return expectedReturn
        }
    }

    static def mockEliminarObjeto(crudHelperServiceMock, expectedReturn) {
        crudHelperServiceMock.demand.eliminarObjeto { entidad, objeto ->
            objeto.delete(flush: true)
            return expectedReturn
        }
    }
}
