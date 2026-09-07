package be.vdab.tcbackend.products;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
class ProductAlreadyHasThisMaterialException extends RuntimeException {
     ProductAlreadyHasThisMaterialException() {
        super("The product already has this material.");
    }
}
