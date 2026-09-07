package be.vdab.tcbackend.origins;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class OriginNotFoundException extends RuntimeException {
    public OriginNotFoundException() {
        super("The selected origin was not found");
    }
}
