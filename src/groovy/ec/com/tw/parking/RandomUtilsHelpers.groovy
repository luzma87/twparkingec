package ec.com.tw.parking

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

    static Integer getRandomInt(max) {
        return getRandomInt(0, max)
    }

    static Integer getRandomInt(min, max) {
        Random random = new Random()
        return random.nextInt(max - min) + min
    }

    static Double getRandomDouble(max) {
        return getRandomDouble(0, max)
    }

    static Double getRandomDouble(min, max) {
        def entero = getRandomInt(min, max - 1)
        def decimal = getRandomInt(0, 100) / 100
        return entero + decimal
    }
}
