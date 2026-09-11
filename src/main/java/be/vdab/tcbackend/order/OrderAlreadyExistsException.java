package be.vdab.tcbackend.order;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

// 9 Toevoegen
@ResponseStatus(HttpStatus.CONFLICT)
public class OrderAlreadyExistsException extends RuntimeException {
    public OrderAlreadyExistsException(long id) {
        super("The order already exists. Order ID: " + id);
    }
}
