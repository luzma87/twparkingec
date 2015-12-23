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

    static def getRandomNombre() {
        return getRandomString(3, 50, false)
    }

    static def getRandomNombreInvalido() {
        return getRandomString(51, 150, false)
    }

    static def getRandomMail() {
        return getRandomString(3, 90, false) + "@test.com"
    }

    static def getRandomPass() {
        return getRandomString(3, 512, true)
    }

    static def getRandomAdmin() {
        def random = new Random()
        return random.nextBoolean()
    }

    static def getValidUsuario() {
        return new Usuario([nombre  : getRandomNombre(),
                            email   : getRandomMail(),
                            password: getRandomPass(),
                            esAdmin : getRandomAdmin()])
    }
}
