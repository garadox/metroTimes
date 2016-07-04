package metrotransit.exception

class InvalidRouteDirectionException extends Exception {

    InvalidRouteDirectionException(String routeDirection) {
        super("Invalid route direction ${routeDirection}")
    }
}
