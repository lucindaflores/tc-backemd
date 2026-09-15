package be.vdab.tcbackend.categories;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class CategoryIsInUseException extends RuntimeException {
    public CategoryIsInUseException() {
        super("This category is in use.");
    }
}
