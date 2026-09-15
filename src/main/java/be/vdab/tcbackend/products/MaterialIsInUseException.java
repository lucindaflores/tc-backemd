package be.vdab.tcbackend.products;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class MaterialIsInUseException extends RuntimeException {
    public MaterialIsInUseException() {
        super("This material is in use by a product.");
    }
}
