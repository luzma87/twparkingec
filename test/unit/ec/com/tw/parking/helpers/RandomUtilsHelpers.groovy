package ec.com.tw.parking.helpers

import com.mifmif.common.regex.Generex
import ec.com.tw.parking.Auto
import ec.com.tw.parking.Usuario
import org.apache.commons.lang.RandomStringUtils

/**
 * Created by lmunda on 12/23/15 14:49.
 */
class RandomUtilsHelpers {

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
        return getRandomMail(3, 90)
    }

    static getRandomMail(min, max) {
        return getRandomString(min, max, false) + "@test.com"
    }

    static getRandomBoolean() {
        def random = new Random()
        return random.nextBoolean()
    }

    static getRandomFromArray(array) {
        Random random = new Random()
        int pos = random.nextInt(array.size())
        return array[pos]
    }

    static generaUsuarioValido() {
        return new Usuario([nombre  : getRandomString(3, 50, false),
                            email   : getRandomMail(),
                            password: getRandomString(3, 512, true),
                            esAdmin : getRandomBoolean()])
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
}
