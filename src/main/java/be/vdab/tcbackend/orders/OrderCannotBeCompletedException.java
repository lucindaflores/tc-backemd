package be.vdab.tcbackend.orders;

public class OrderCannotBeCompletedException extends RuntimeException {
    public OrderCannotBeCompletedException(long id, Status status) {
        super("This order cannot be completed, it has an status:" + status + ". Order id: " + id);
    }
}
