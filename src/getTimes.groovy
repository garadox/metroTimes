@Grab( 'joda-time:joda-time:2.3' )

import org.joda.time.*
import metrotransit.BusService


//Script to return the time until the next bus, given a route, stop and direction
def cli = new CliBuilder(usage:'getTimes.groovy <route> <stop> <direction>')
options = cli.parse(args)

if (options.arguments().isEmpty() || options.arguments().size() != 3) {
    cli.usage()
    return
}

String busRoute = options.arguments()[0]
String busStop = options.arguments()[1]
String routeDirection = options.arguments()[2]

def routeId = BusService.findRouteId(busRoute)
if (!routeId) {
    println "Unable to find route '${busRoute}'"
    return
}
def directionId = BusService.findRouteDirectionIdByRouteId(routeId, routeDirection)
if (!directionId) {
    println "Unable to find direction '${routeDirection}' on route '${busRoute}'"
    return
}
def stopId = BusService.findStopIdByRouteIdAndDirectionId(routeId, directionId, busStop)
if (!stopId) {
    println "Unable to find stop '${busStop}' on route '${busRoute}'"
    return
}

def busTimes = BusService.findBusTimesByRouteIdAndDirectionIdAndStopId(routeId, directionId, stopId)
if (busTimes.size()) {
    def nextBusTime = busTimes.first()
    def minutes = Minutes.minutesBetween(DateTime.now(), nextBusTime).getMinutes()
    println "${minutes} Minute${(minutes > 1 || minutes == 0) ? 's' : ''}"
}
