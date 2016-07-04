package metrotransit.exception

class InvalidRouteBusStopException extends Exception {

    InvalidRouteBusStopException(String busStop) {
        super("Invalid bus stop ${busStop}")
    }
}
