package metrotransit.exception

class InvalidRouteException extends Exception {

    InvalidRouteException(String busRoute) {
        super("Unable to find route named ${busRoute}")
    }
}