package be.vdab.tcbackend.users;

import be.vdab.tcbackend.orders.Order;
import jakarta.persistence.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="users")
public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "userGenerator") /*9.8 Sequence */
    @SequenceGenerator(name = "userGenerator",
            sequenceName = "userid",
            allocationSize = 1) // increment by 1

    private long id;

    private String email;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    @Enumerated(EnumType.STRING) private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    private boolean active;

    /* Thema 23: Bidirectionele Associatie met @OneToMany */
    // In the class products, under the variable campus in @JoinColumn, JPA finds how the association is expressed in the database.
    @OneToMany(mappedBy = "user")
    private Set<Address> addresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new LinkedHashSet<>();

    /* Constructors */
    public User(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
        this.active = true;
        this.role = Role.CUSTOMER;
        this.createdAt = LocalDateTime.now();
    }

    protected User() {
    }

    /* Getters */
    public long getId() {
        return id;
    }

    public String getEmail() {
        return email;
    }

    public String getFirstName() {
        return firstName;
    }

    public String getLastName() {
        return lastName;
    }

    public Role getRole() {
        return role;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public boolean isActive() {
        return active;
    }

    public Set<Address> getAddresses() {
        return Collections.unmodifiableSet(addresses);
    }

    public Set<Order> getOrders() {
        return Collections.unmodifiableSet(orders);
    }

    void update(String firstName, String lastName) {
        this.firstName = firstName;
        this.lastName = lastName;
    }

    void updateRole(Role role) {
        this.role = role;
    }


    void deactivate() {
        this.active = false;
    }

}
