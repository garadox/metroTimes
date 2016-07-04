import metrotransit.exception.InvalidRouteBusStopException
import metrotransit.exception.InvalidRouteDirectionException
import metrotransit.integration.MetroTransit
import metrotransit.service.MetroTransitService
import metrotransit.exception.InvalidRouteException
import spock.lang.Specification

class BusServiceSpec extends Specification {

    MetroTransitService service
    MetroTransit mockBusApi

    def setup() {
        mockBusApi = Stub(MetroTransit)
        service = new MetroTransitService(mockBusApi)
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
        setup:
        mockBusApi.getRouteIds() >> [[name: "test route", id: "123"]]
        mockBusApi.getRouteDirections(_) >> [[name: "test direction", id: "NORTH"]]

        String busRoute = "route"
        String busStop = "not a bus route"
        String routeDirection = "not a direction"

        when:
        service.findBusTimes(busRoute, busStop, routeDirection)

        then:
            InvalidRouteDirectionException ex = thrown()
            ex.message == "Invalid route direction ${routeDirection}".toString()
    }

    def "When I give an invalid bus stop I get an exception"() {
        setup:
        mockBusApi.getRouteIds() >> [[name: "test route", id: "123"]]
        mockBusApi.getRouteDirections(_) >> [[name: "test direction", id: "NORTH"]]
        mockBusApi.getRouteBusStops(_, _) >> [[name: "test bus stop", id: "987"]]

        String busRoute = "route"
        String busStop = "not a bus route"
        String routeDirection = "direction"

        when:
        service.findBusTimes(busRoute, busStop, routeDirection)

        then:
        InvalidRouteBusStopException ex = thrown()
        ex.message == "Invalid bus stop ${busStop}".toString()
    }
}
