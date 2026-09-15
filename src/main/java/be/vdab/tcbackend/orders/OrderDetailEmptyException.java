package be.vdab.tcbackend.orders;

public class OrderDetailEmptyException extends RuntimeException {
    public OrderDetailEmptyException() {
        super("The order does not contains products.");
    }
}
