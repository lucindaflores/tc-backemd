package be.vdab.tcbackend.origins;

public class OriginAlreadyExistsException extends RuntimeException {
    public OriginAlreadyExistsException() {
        super("This origin already exists.");
    }
}
