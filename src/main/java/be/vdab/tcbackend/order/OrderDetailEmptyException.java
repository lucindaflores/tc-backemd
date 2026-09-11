package be.vdab.tcbackend.order;

public class OrderDetailEmptyException extends RuntimeException {
    public OrderDetailEmptyException() {
        super("The order does not contains products.");
    }
}
