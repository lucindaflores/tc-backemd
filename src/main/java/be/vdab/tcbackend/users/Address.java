package be.vdab.tcbackend.users;

import jakarta.persistence.*;

@Entity
@Table(name="addresses")
public class Address {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "addressGenerator") /*9.8 Sequence */
    @SequenceGenerator(name = "addressGenerator",
            sequenceName = "addressid",
            allocationSize = 1) // increments by 1
    private Long id;

    private String street;

    @Column(name = "house_number")
    private String houseNumber;

    private String bus;

    private String city;

    @Column(name = "postal_code")
    private String postalCode;

    private String country;

    /* Thema 21: @ManyToOne */
    // LAZY: So it does not load both tables when just one field of one table is needed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public Address(String street, String houseNumber, String bus, String city, String postalCode, String country, User user) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.bus = bus;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
        this.user = user;
    }

    protected Address() { }

    public Long getId() {
        return id;
    }

    public String getStreet() {
        return street;
    }

    public String getHouseNumber() {
        return houseNumber;
    }

    public String getBus() {
        return bus;
    }

    public String getCity() {
        return city;
    }

    public String getPostalCode() {
        return postalCode;
    }

    public String getCountry() {
        return country;
    }

    public User getUser() {
        return user;
    }

    void update(String street,
                String houseNumber,
                String bus,
                String city,
                String postalCode,
                String country) {
        this.street = street;
        this.houseNumber = houseNumber;
        this.bus = bus;
        this.city = city;
        this.postalCode = postalCode;
        this.country = country;
    }


}
