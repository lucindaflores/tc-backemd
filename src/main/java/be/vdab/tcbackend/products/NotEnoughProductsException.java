package be.vdab.tcbackend.products;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class NotEnoughProductsException extends RuntimeException {
    public NotEnoughProductsException(long id) {
        super("There are not enough items to ship this order. Product id: " + id);
    }
}
