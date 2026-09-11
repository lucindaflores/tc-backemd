package be.vdab.tcbackend.categories;

import be.vdab.tcbackend.products.Product;
import jakarta.persistence.*;

import java.util.Collections;
import java.util.Set;

@Entity
@Table(name="categories")
public class Category {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "categoryGenerator") /*9.8 Sequence */
    @SequenceGenerator(name = "categoryGenerator",
            sequenceName = "categoryid",
            allocationSize = 1) // increment by 1
    private long id;

    private String name;


//    @OneToMany(mappedBy = "category")
//    @OrderBy("name")
//    private Set<Product> products; //= new LinkedHashSet<>();

    /* Constructor(s) */
    Category(String name) {
        this.name = name;
        // products = new LinkedHashSet<>();
    }

    protected Category() { }


    /* Getters */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    /* oneToMany */
//    public Set<Product> getProducts() {
//        return Collections.unmodifiableSet(products);
//    }

    public void setName(String name) {
        this.name = name;
    }

    /* Equals, Hashcode */
    /* Thema 23: Bidirectionele Associatie met @OneToMany */
    // SET cannot contain duplicates, so java needs to know if these 2 objs are the same
    // Based on name because it's unique and it does not change
//    public boolean equals(Object object) {
//        return object instanceof Category category && name.equalsIgnoreCase(category.name);
//    }
//
//    @Override
//    public int hashCode() {
//        return name.toLowerCase().hashCode();
//    }
//

}
