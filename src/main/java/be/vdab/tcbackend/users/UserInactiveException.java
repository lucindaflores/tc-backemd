package be.vdab.tcbackend.users;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(HttpStatus.CONFLICT)
public class UserInactiveException extends RuntimeException {
    public UserInactiveException() {
        super("The selected user is not active");
    }
}
