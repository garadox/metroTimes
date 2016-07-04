import metrotransit.BusApi
import metrotransit.BusService
import metrotransit.InvalidRouteException
import spock.lang.Specification

class BusServiceSpec extends Specification {

    BusService service
    BusApi mockBusApi

    def setup() {
        mockBusApi = Stub(BusApi)
        service = new BusService(mockBusApi)
    }

    def "When i give an invalid route I get an exception"() {
        setup:
        mockBusApi.getRouteIds() >> [[name: "test route", id: "123"]]

        String busRoute = "not a route"
        String busStop = "not a bus route"
        String routeDirection = "not a direction"

        when:
        service.findBusTimes(busRoute, busStop, routeDirection)

        then:
            InvalidRouteException ex = thrown()
            ex.message == "Unable to find route named ${busRoute}".toString()
    }

    def "When I give an invalid bus direction I get an exception"() {
        given:
        String busRoute = "not a route"
        String busStop = "not a bus route"
        String routeDirection = "not a direction"


    }
}
