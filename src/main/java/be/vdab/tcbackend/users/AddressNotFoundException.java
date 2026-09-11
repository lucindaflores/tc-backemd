package be.vdab.tcbackend.users;

public class AddressNotFoundException extends RuntimeException {
    public AddressNotFoundException() {
        super("The address was not found.");
    }
}
