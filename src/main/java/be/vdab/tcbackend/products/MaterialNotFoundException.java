package be.vdab.tcbackend.products;

public class MaterialNotFoundException extends RuntimeException {
    public MaterialNotFoundException() {
        super("The material was not found.");
    }
}
