package metrotransit.integration

import groovy.json.JsonSlurper

class MetroTransit {

    def getRouteIds() {
        def rawResponse = "http://svc.metrotransit.org/NexTrip/Routes".toURL().
                getText(requestProperties: [Accept: 'application/json'])
        def json = new JsonSlurper().parseText(rawResponse)
        json.collect { entry -> [id: entry.Route, name: entry.Description] }
    }

    def getRouteDirections(String routeId) {
        def rawResponse = "http://svc.metrotransit.org/NexTrip/Directions/${routeId}".toURL().
                getText(requestProperties: [Accept: 'application/json'])
        def json = new JsonSlurper().parseText(rawResponse)
        json.collect { entry -> [id: entry.Value, name: entry.Text] }
    }

    def getRouteBusStops(String routeId, String directionId) {

    }
}
