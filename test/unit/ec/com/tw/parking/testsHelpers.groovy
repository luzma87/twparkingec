package ec.com.tw.parking

import com.mifmif.common.regex.Generex
import org.apache.commons.lang.RandomStringUtils

/**
 * Created by lmunda on 12/23/15 14:49.
 */
class TestsHelpers {

    static getRandomString(int min, int max, boolean allChars) {
        if (min == max) {
            return RandomStringUtils.randomAlphabetic(min)
        }
        def random = new Random()
        def length = random.nextInt(max - min) + min
        if (allChars) {
            return RandomStringUtils.random(length)
        }
        return RandomStringUtils.randomAlphabetic(length)
    }

    static getRandomString(int numeroCaracteres) {
        return RandomStringUtils.randomAlphabetic(numeroCaracteres)
    }

    static getRandomString(String regex, int min, int max) {
        def generex = new Generex(regex)
        return generex.random(min, max)
    }

    static getRandomMail() {
        return getRandomString(3, 90, false) + "@test.com"
    }

    static getRandomAdmin() {
        def random = new Random()
        return random.nextBoolean()
    }

    static generaUsuarioValido() {
        return new Usuario([nombre  : getRandomString(3, 50, false),
                            email   : getRandomMail(),
                            password: getRandomString(3, 512, true),
                            esAdmin : getRandomAdmin()])
    }

    static Auto generaAutoValido() {
        def tamanios = ["P", "G"]
        Random random = new Random()
        int pos = random.nextInt(2)
        return new Auto([
            usuario: generaUsuarioValido(),
            marca  : getRandomString(2, 20, false),
            modelo : getRandomString(2, 20, false),
            placa  : getRandomString(8),
            tamanio: tamanios[pos]
        ])
    }

    static Auto generaAutoConCampo(campo, valor) {
        def auto = generaAutoValido()
        auto[campo] = valor
        return auto
    }

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
