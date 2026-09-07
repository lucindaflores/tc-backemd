package be.vdab.tcbackend.products;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/* Thema 15: Lock */
@ResponseStatus(HttpStatus.CONFLICT)
public class ProductVersionConflictException extends RuntimeException {
    public ProductVersionConflictException() {
        super("Another user is currently updating this product.");
    }
}

