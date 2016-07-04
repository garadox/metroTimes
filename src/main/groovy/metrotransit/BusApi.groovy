package metrotransit

import groovy.json.JsonSlurper

class BusApi {

    def getRouteIds() {
        def rawResponse = "http://svc.metrotransit.org/NexTrip/Routes".toURL().
                getText(requestProperties: [Accept: 'application/json'])
        def json = new JsonSlurper().parseText(rawResponse)
        json.collect { entry -> results << [id: entry.Route, name: entry.Description] }
    }
}
