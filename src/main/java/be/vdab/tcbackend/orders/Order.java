package be.vdab.tcbackend.orders;

import be.vdab.tcbackend.users.Address;
import be.vdab.tcbackend.users.User;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.*;

@Entity
@Table(name="orders")
public class Order {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "orderGenerator") /*9.8 Sequence */
    @SequenceGenerator(name = "orderGenerator",
            sequenceName = "orderid",
            allocationSize = 1) // increment by 1
    private long id;

    @Column(name = "order_date")
    private LocalDateTime orderDate;

    @Enumerated(EnumType.STRING) private Status status;

    /* Relationships */
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @OneToMany(mappedBy = "order",
               cascade = CascadeType.PERSIST) // so order detail is persisted
    private List<OrderDetail> orderDetails = new ArrayList<>();


    /* Constructor(s) */
    public Order(User user, Address address) {
        this.orderDate = LocalDateTime.now();
        this.status = Status.PLACED;
        this.user = user;

    }

    protected Order() { }


    /* Getters */
    public long getId() {
        return id;
    }

    public LocalDateTime getOrderDate() {
        return orderDate;
    }

    public Status getStatus() {
        return status;
    }


    public User getUser() {
        return user;
    }

    public List<OrderDetail> getOrderDetails() {
        return orderDetails;
    }

    /* Functions */
    void add(OrderDetail detail) {
        orderDetails.add(detail);
    }

    /* These methods set the shipped field to the current date & sets the status to shipped */
     void ship() {
         if (status != Status.PLACED) {
             throw new OrderCannotBeShippedException(id, status);
         }
        this.status = Status.SHIPPED;
    }

     void complete() {
         if (status != Status.SHIPPED) {
             throw new OrderCannotBeCompletedException(id, status);
         }
        this.status = Status.COMPLETED;
    }

     void cancel() {
         if (status != Status.PLACED) {
             throw new OrderCannotBeCancelledException(id,status);
         }
        this.status = Status.CANCELLED;
    }

}
