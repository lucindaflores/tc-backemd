package be.vdab.tcbackend.products;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class ProductAlreadyExistsException extends RuntimeException {

    public ProductAlreadyExistsException( ) {
        super("The product already exists.");
    }
}
