package be.vdab.tcbackend.users;

import be.vdab.tcbackend.order.Order;
import jakarta.persistence.*;
import org.aspectj.weaver.ast.Or;

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

    @Column(name = "password_hash")
    private String passwordHash;

    @Column(name = "first_name")
    private String firstName;

    @Column(name = "last_name")
    private String lastName;

    //private String role;
    @Enumerated(EnumType.STRING) private Role role;

    @Column(name = "created_at")
    private LocalDateTime createdAt;

    /* Thema 23: Bidirectionele Associatie met @OneToMany */
    // In the class products, under the variable campus in @JoinColumn, JPA finds how the association is expressed in the database.
    @OneToMany(mappedBy = "user")
    private Set<Address> addresses = new LinkedHashSet<>();

    @OneToMany(mappedBy = "user")
    private Set<Order> orders = new LinkedHashSet<>();

    @Version
    private long version;

    /* Constructors */
    public User(String email, String passwordHash, String firstName, String lastName) {
        this.email = email;
        this.passwordHash = passwordHash;
        this.firstName = firstName;
        this.lastName = lastName;
        this.role = Role.CUSTOMER;
        this.createdAt = LocalDateTime.now();
        //orders = new LinkedHashSet<>();
        //addresses = new LinkedHashSet<>();
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

    public String getPasswordHash() {
        return passwordHash;
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

    public Set<Address> getAddresses() {
        return Collections.unmodifiableSet(addresses);
    }

    public Set<Order> getOrders() {
        return Collections.unmodifiableSet(orders);
    }

    public long getVersion() {
        return version;
    }

    void add(Address address) {
        addresses.add(address);
    }

    void update(String email, String firstName, String lastName) {
        this.email = email;
        this.firstName = firstName;
        this.lastName = lastName;
    }

    void updatePassword(String passwordHash) {
        this.passwordHash = passwordHash;
    }

    void updateRole(Role role) {
        this.role = role;
    }

    void updateFirstName(String firstName) {
        this.firstName = firstName;
    }

    void updateLastName(String lastName) {
        this.lastName = lastName;
    }

}
