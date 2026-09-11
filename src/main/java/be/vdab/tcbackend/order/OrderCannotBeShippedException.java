package be.vdab.tcbackend.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderCannotBeShippedException extends RuntimeException {
    public OrderCannotBeShippedException(long id, Status status) {
        super("This order has already been shipped, it has an status:" + status + ". Order id: " + id);
    }
}

