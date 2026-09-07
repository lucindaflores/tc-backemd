package be.vdab.tcbackend.categories;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.NOT_FOUND)
public class CategoryNotFoundException extends RuntimeException {

     public CategoryNotFoundException() {
         super("The selected category was not found.");
    }

}
