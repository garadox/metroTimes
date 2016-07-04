package metrotransit


import groovy.json.JsonSlurper
import org.joda.time.*

class BusService {

    def findBusTimes(String busRoute ,String busStop, String routeDirection) {
        String routeId = getRouteId(busRoute)
        if (!routeId) {
            throw new InvalidRouteException(busRoute)
        }
    }

    protected String getRouteId(String busRoute) {
        def rawResponse = "http://svc.metrotransit.org/NexTrip/Routes".toURL().
                getText(requestProperties: [Accept: 'application/json'])
        def json = new JsonSlurper().parseText(rawResponse)
        def routeData = json.find { entry -> entry.Description.toUpperCase().contains(busRoute.toUpperCase()) }
        routeData?.Route
    }

    static String findRouteId(String busRoute) {
        def rawResponse = "http://svc.metrotransit.org/NexTrip/Routes".toURL().
                getText(requestProperties: [Accept: 'application/json'])
        def json = new JsonSlurper().parseText(rawResponse)
        def routeData = json.find { entry -> entry.Description.toUpperCase().contains(busRoute.toUpperCase()) }
        routeData?.Route
    }

    static String findRouteDirectionIdByRouteId(String routeId, String routeDirection) {
        def rawResponse = "http://svc.metrotransit.org/NexTrip/Directions/${routeId}".toURL().
                getText(requestProperties: [Accept: 'application/json'])
        def json = new JsonSlurper().parseText(rawResponse)
        def directionId = json.find { entry -> entry.Text.contains(routeDirection.toUpperCase()) }
        directionId?.Value
    }

    static String findStopIdByRouteIdAndDirectionId(String routeId, String directionId, String busStop) {
        def rawResponse = "http://svc.metrotransit.org/NexTrip/Stops/${routeId}/${directionId}".toURL().
                getText(requestProperties: [Accept: 'application/json'])
        def json = new JsonSlurper().parseText(rawResponse)
        def stopData = json.find { entry -> entry.Text.toUpperCase().contains(busStop.toUpperCase()) }
        stopData?.Value
    }

    static def findBusTimesByRouteIdAndDirectionIdAndStopId(String routeId, String directionId, String stopId) {
        def rawResponse = "http://svc.metrotransit.org/NexTrip/${routeId}/${directionId}/${stopId}".toURL().
                getText(requestProperties: [Accept: 'application/json'])
        def json = new JsonSlurper().parseText(rawResponse)
        List<DateTime> busTimes = []
        json.each { entry ->
            def matcher = entry.DepartureTime =~ /\/Date\((\d+)([\-\+]\d+)\)\//
            if (matcher) {
                Long instant = matcher[0][1].toLong()
                int timezoneOffset = (matcher[0][2].toInteger() / 100).toInteger().intValue()
                DateTime departureTime = new DateTime(instant, DateTimeZone.forOffsetHours(timezoneOffset))
                busTimes << departureTime
            }
        }
        busTimes.sort { it }.toArray()
    }
}
