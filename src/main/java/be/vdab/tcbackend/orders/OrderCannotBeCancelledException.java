package be.vdab.tcbackend.orders;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderCannotBeCancelledException extends RuntimeException {
    public OrderCannotBeCancelledException(long id, Status status) {
        super("This order cannot be shipped, it has an status:" + status + ". Order id: " + id);
    }
}
