package be.vdab.tcbackend.origins;

import be.vdab.tcbackend.products.Product;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="origins")
public class Origin {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "originGenerator")
    @SequenceGenerator(name = "originGenerator",
            sequenceName = "originid",
            allocationSize = 1)
    private long id;

    private String name;

    @OneToMany(mappedBy = "origin")
    @OrderBy("name")
    private Set<Product> products = new LinkedHashSet<>();

    /* Constructors*/
    Origin(String name) {
        this.name = name;
    }

    protected Origin() { }

    /* Getters */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /* Setters */
    void updateName(String name) {
        this.name = name;
    }

    public Set<Product> getProducts() {
        return Collections.unmodifiableSet(products);
    }

}
