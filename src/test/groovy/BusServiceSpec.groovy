import metrotransit.BusService
import metrotransit.InvalidRouteException
import spock.lang.Specification

class BusServiceSpec extends Specification {

    static BusService service

    def setupSpec() {
        service = new BusService()
    }

    def "When i give an invalid route I get an exception"() {
        given:
        String busRoute = "not a route"
        String busStop = "not a bus route"
        String routeDirection = "not a direction"

        when:
        service.findBusTimes(busRoute, busStop, routeDirection)

        then:
            InvalidRouteException ex = thrown()
            ex.message == "Unable to find route named ${busRoute}".toString()
    }
}
