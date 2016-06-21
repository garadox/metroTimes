# metroTimes

Sample script using the MetroTransit NexTrip API. Usage:

`groovy busTimes.groovy <route> <stop> <direction>`, where `route` is a string containing part of a bus route, `stop` is a string containing part of a bus stop name and `direction` is the direction the bus is heading (one of `north`, `south`, `west` or `east`).

The script has some basic error checking, and will let you know if it can't find the route given, or the direction is not supported on the given route, or the stop is not found on the given route.

### Dependencies

You will need `groovy` installed (tested with version 2.4.7), and Java 1.8 is recommended. 

### Instructions

1. Clone the repository, using the links provided on this page
2. Install the dependencies above
3. Run the script, with the example data above.
