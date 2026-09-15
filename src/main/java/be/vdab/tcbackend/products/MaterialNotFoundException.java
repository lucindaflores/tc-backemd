package be.vdab.tcbackend.products;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException() {
        super("The material was not found.");
    }
}
