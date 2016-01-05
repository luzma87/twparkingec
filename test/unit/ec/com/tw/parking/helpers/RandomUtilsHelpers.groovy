package ec.com.tw.parking.helpers

import com.mifmif.common.regex.Generex
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

    static getRandomInt(max) {
        return getRandomInt(0, max)
    }

    static getRandomInt(min, max) {
        Random random = new Random()
        return random.nextInt(max - min) + min
    }

    static getRandomDouble(max) {
        Random random = new Random()
        def entero = random.nextInt(max - 1)
        def decimal = random.nextInt(100) / 100
        return entero + decimal
    }
}
