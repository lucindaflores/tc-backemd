package be.vdab.tcbackend.order;

public class OrderCannotBeChangedException extends RuntimeException {
    public OrderCannotBeChangedException(long id) {
        super("This order has a final status cannot be changed." +  ". Order id: " + id);
    }
}
