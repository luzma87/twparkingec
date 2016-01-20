package ec.com.tw.parking


import ec.com.tw.parking.builders.AsignacionPuestoBuilder
import grails.test.mixin.*
import spock.lang.*

@TestFor(AsignacionPuestoController)
@Mock([AsignacionPuesto, MensajesBuilderTagLib, Auto, Puesto])
class AsignacionPuestoControllerSpec extends Specification {

    CrudHelperService crudHelperServiceMock

    def setup() {
        crudHelperServiceMock = Mock(CrudHelperService)
        controller.crudHelperService = crudHelperServiceMock
    }

    void "Debe obtener la lista de asignacionPuestos, su numero, autos sin asignacion y puestos sin asignacion"() {
        setup:
        asignacionPuestoInstance.save()

        expect:
        controller.list() == [puestosSinAsignacion         : Puesto.obtenerSinAsignacion(),
                              autosSinAsignacion           : Auto.obtenerSinAsignacion(),
                              asignacionPuestoInstanceList : [asignacionPuestoInstance],
                              asignacionPuestoInstanceCount: 1]

        where:
        asignacionPuestoInstance = AsignacionPuestoBuilder.nuevo().crear()
    }

    void "Debe devolver una instancia de asignacionPuesto"() {
        when:
        def asignacionPuestoInstanceReturned = controller.form_ajax().asignacionPuestoInstance

        then:
        1 * crudHelperServiceMock.obtenerObjeto(AsignacionPuesto, _) >> asignacionPuestoInstance
        asignacionPuestoInstanceReturned.properties == asignacionPuestoInstance.properties

        where:
        asignacionPuestoInstance << [new AsignacionPuesto(), AsignacionPuestoBuilder.nuevo().crear()]
    }

    void "Debe guardar un asignacionPuesto valido"() {
        setup:
        def asignacionPuestoInstance = new AsignacionPuesto()
        def expectedMessage = "SUCCESS*default.saved.message"

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(AsignacionPuesto, _) >> asignacionPuestoInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as AsignacionPuesto, _) >> expectedMessage
    }

    void "Debe mostrar error al intentar actualizar un asignacionPuesto no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"
        def asignacionPuestoInstance = null

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(AsignacionPuesto, _) >> asignacionPuestoInstance
        0 * crudHelperServiceMock.guardarObjeto(_ as AsignacionPuesto, _)
        response.text == expectedMessage
    }

    void "Debe mostrar error al actualizar un asignacionPuesto con datos invalidos"() {
        setup:
        def expectedMessage = "ERROR*default.not.saved.message"
        def asignacionPuestoInstance = AsignacionPuestoBuilder.nuevo().crear()

        when:
        request.method = "POST"
        controller.save_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(AsignacionPuesto, _) >> asignacionPuestoInstance
        1 * crudHelperServiceMock.guardarObjeto(_ as AsignacionPuesto, _) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe eliminar un asignacionPuesto valido"() {
        setup:
        def expectedMessage = "SUCCESS*default.deleted.message"
        def asignacionPuestoInstance = AsignacionPuestoBuilder.nuevo().crear()
        def random = new Random()
        asignacionPuestoInstance.id = random.nextInt()

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(AsignacionPuesto, _) >> asignacionPuestoInstance
        1 * crudHelperServiceMock.eliminarObjeto(_ as AsignacionPuesto) >> expectedMessage
        response.text == expectedMessage
    }

    void "Debe mostrar error al intentar eliminar un asignacionPuesto no encontrado"() {
        setup:
        def expectedMessage = "ERROR*default.not.found.message"

        when:
        request.method = "POST"
        controller.delete_ajax()

        then:
        1 * crudHelperServiceMock.obtenerObjeto(AsignacionPuesto, _) >> asignacionPuestoInstance
        0 * crudHelperServiceMock.eliminarObjeto(_ as AsignacionPuesto)
        response.text == expectedMessage

        where:
        asignacionPuestoInstance << [null, new AsignacionPuesto()]
    }
}
