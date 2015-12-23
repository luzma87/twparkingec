package ec.com.tw.parking

import grails.test.mixin.Mock
import grails.test.mixin.TestFor
import spock.lang.Specification

/**
 * See the API for {@link grails.test.mixin.domain.DomainClassUnitTestMixin} for usage instructions
 */
@TestFor(Auto)
@Mock([Usuario])
class AutoSpec extends Specification {

    def auto

    def setup() {
        auto = new Auto([marca: TestsHelpers.getRandomString(2, 20, false)])
    }

    void "Deben los datos ser correctos"() {
        when: 'Los datos son correctos'

        then: 'la validacion debe pasar'
        auto.validate()
        !auto.hasErrors()
    }

    void "No debe ser la marca nula"() {
        when: 'la marca es nula'
        auto.marca = null

        then: 'la validacion debe fallar'
        !auto.validate()
        auto.hasErrors()
        auto.errors['marca']?.code == 'nullable'
    }

    void "No debe ser la marca blanca"() {
        when: 'la marca es blanca'
        auto.marca = ''

        then: 'la validacion debe fallar'
        !auto.validate()
        auto.hasErrors()
        auto.errors['marca']?.code == 'blank'
    }

    void "No debe tener la marca menos de 2 caracteres"() {
        when:
        auto.marca = TestsHelpers.getRandomString(1)

        then:
        !auto.validate()
        auto.hasErrors()
        auto.errors['marca']?.code == 'minSize.notmet'
    }

    void "No debe tener la marca mas de 20 caracteres"() {
        when:
        auto.marca = TestsHelpers.getRandomString(21, 40, false)

        then:
        !auto.validate()
        auto.hasErrors()
        auto.errors['marca']?.code == 'maxSize.exceeded'
    }
}
