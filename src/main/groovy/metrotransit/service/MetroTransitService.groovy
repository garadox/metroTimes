package metrotransit.service


import groovy.json.JsonSlurper
import metrotransit.exception.InvalidRouteBusStopException
import metrotransit.exception.InvalidRouteDirectionException
import metrotransit.exception.InvalidRouteException
import metrotransit.integration.MetroTransit
import org.joda.time.*

class MetroTransitService {

    MetroTransit busApi

    MetroTransitService(MetroTransit busApi) {
        this.busApi = busApi
    }

    def findBusTimes(String busRoute ,String busStop, String routeDirection) {
        String routeId = findRouteId(busRoute)
        if (!routeId) {
            throw new InvalidRouteException(busRoute)
        }

        String routeDirectionId = findRouteDirectionId(routeId, routeDirection)
        if (!routeDirectionId) {
            throw new InvalidRouteDirectionException(routeDirection)
        }

        String busStopId = findRouteBusStop(routeId, routeDirectionId, busStop)
        if (!busStopId) {
            throw new InvalidRouteBusStopException(busStop)
        }

        def busTimes = fetchBusTimes(routeId, routeDirection, busStopId)
        busTimes
    }

    protected String findRouteId(String busRoute) {
        def routes = busApi.routeIds
        def route = routes.find { entry -> entry.name.toUpperCase().contains(busRoute.toUpperCase()) }
        route?.id
    }

    protected String findRouteDirectionId(String routeId, String routeDirection) {
        def directions = busApi.getRouteDirections(routeId)
        def direction = directions.find { entry -> entry.name.toUpperCase().contains(routeDirection.toUpperCase()) }
        direction?.id
    }

    protected String findRouteBusStop(String routeId, String routeDirectionId, String routeBusStop) {
        def busStops = busApi.getRouteBusStops(routeId, routeDirectionId)
        def busStop = busStops.find { entry -> entry.name.toUpperCase().contains(routeBusStop.toUpperCase()) }
        busStop?.id
    }

    protected fetchBusTimes(String routeId, String routeDirectionId, String routeBusStopId) {
        def busTimes = busApi.getBusTimes(routeId, routeDirectionId, routeBusStopId)
        busTimes.sort { a, b -> a <=> b }
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
