package metrotransit

class InvalidRouteException extends Exception {

    InvalidRouteException(String message) {
        super("Unable to find route named ${message}")
    }
}