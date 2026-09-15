package be.vdab.tcbackend.origins;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OriginIsInUseException extends RuntimeException {
    public OriginIsInUseException() {
        super("This origin is currently assigned to a product.");
    }
}
