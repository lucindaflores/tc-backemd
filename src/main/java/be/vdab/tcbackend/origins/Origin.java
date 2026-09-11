package be.vdab.tcbackend.origins;

import be.vdab.tcbackend.products.Product;
import jakarta.persistence.*;

import java.util.Set;

@Entity
@Table(name="origins")
public class Origin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "originGenerator") /*9.8 Sequence */
    @SequenceGenerator(name = "originGenerator",
            sequenceName = "originid",
            allocationSize = 1) // increment by 1 private long id
    private long id;

    private String name;

    /* Thema 23: Bidirectionele Associatie met @OneToMany */
    // In the class products, under the variable campus in @JoinColumn, JPA finds how the association is expressed in the database.
    @OneToMany(mappedBy = "origin")
    @OrderBy("name")
    private Set<Product> products;// = new LinkedHashSet<>();

    /* Constructors*/
    Origin(String name) {
        this.name = name;
       // products = new LinkedHashSet<>();
    }

    // A default protected constructor is needed so JPA can work
    protected Origin() { }

    /* Getters */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /* Setters */
    public void setName(String name) {
        this.name = name;
    }

    //    public Set<Product> getProducts() {
//        return Collections.unmodifiableSet(products);
//    }

    /* Functions */
    /* Thema 23: Bidirectionele Associatie met @OneToMany */
//    @Override
//    public boolean equals(Object o) {
//        if (!(o instanceof Origin origin)) return false;
//        return Objects.equals(name, origin.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return Objects.hashCode(name);
//    }
}
