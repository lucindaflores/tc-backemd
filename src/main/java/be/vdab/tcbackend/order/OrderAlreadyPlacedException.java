package be.vdab.tcbackend.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderAlreadyPlacedException extends RuntimeException {
    public OrderAlreadyPlacedException() {
        super("This order has already been placed.");
    }
}
