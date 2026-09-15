package be.vdab.tcbackend.origins;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class OriginAlreadyExistsException extends RuntimeException {
    public OriginAlreadyExistsException() {
        super("This origin already exists.");
    }
}
