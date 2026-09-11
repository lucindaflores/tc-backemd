package be.vdab.tcbackend.categories;

public class CategoryAlreadyExistsException extends RuntimeException {
    public CategoryAlreadyExistsException() {
        super("This category already exists.");
    }
}
