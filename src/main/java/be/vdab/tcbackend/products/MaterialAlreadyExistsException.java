package be.vdab.tcbackend.products;

public class MaterialAlreadyExistsException extends RuntimeException {
    public MaterialAlreadyExistsException() {
        super("This material already exists.");
    }
}
