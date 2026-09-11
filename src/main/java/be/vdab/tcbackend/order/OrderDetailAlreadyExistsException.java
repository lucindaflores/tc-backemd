package be.vdab.tcbackend.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OrderDetailAlreadyExistsException extends RuntimeException {
    public OrderDetailAlreadyExistsException() {
        super("This order detail already exists.");
    }
}
