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

//    /* Shipping details  */
//    @Column(name = "shipping_street")
//    private String shippingStreet;
//
//    @Column(name = "shipping_house_number")
//    private String shippingHouseNumber;
//
//    @Column(name = "shipping_bus")
//    private String shippingBus;
//
//    @Column(name = "shipping_city")
//    private String shippingCity;
//
//    @Column(name = "shipping_postal_code")
//    private String shippingPostalCode;
//
//    @Column(name = "shipping_country")
//    private String shippingCountry;

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

//        this.shippingStreet = address.getStreet();
//        this.shippingHouseNumber = address.getHouseNumber();
//        this.shippingBus = address.getBus();
//        this.shippingCity = address.getCity();
//        this.shippingPostalCode = address.getPostalCode();
//        this.shippingCountry = address.getCountry();
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

    /* Equals & Hash code */
    // TODO: Do i need equals & hashcode for orderdetail
    /* Equals, Hashcode */
    /* Thema 23: Bidirectionele Associatie met @OneToMany */
    // Based on email because it is unique and it does not change
//    @Override
//    public boolean equals(Object object) {
//        if (!(object instanceof Order order)) return false;
//        return id == order.id && version == order.version;
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hash(id, version);
//    }

}
