package be.vdab.tcbackend.products;

import jakarta.persistence.*;

import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Objects;
import java.util.Set;

@Entity
@Table(name="materials")
class Material {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                   generator = "materialGenerator") /*9.8 Sequence */
    @SequenceGenerator(name = "materialGenerator",
            sequenceName = "materialid",
     allocationSize = 1) // increment by 1 private long id;
    private long id;

    String name;

    @Column(name = "name_spanish")
    String nameSpanish;
    String technique;

    /* Thema 24: @ManyToMany */
    // MappedBy: Represents the other side of the association in the Task class.
    // JPA will find there how the association has been worked out in the database: the variable docenten stands for: @JoinTable(…)
    @ManyToMany(mappedBy = "materials")
    private Set<Product> products = new LinkedHashSet<>();

    /* Constructors */
    // Thema 9: Toevoegen
    // NOTE: No id in the constructor bc db created the id
    public Material(String name, String nameSpanish, String technique) {
        this.name = name;
        this.nameSpanish = nameSpanish;
        this.technique = technique;
    }

    // Thema 9: Toevoegen
    // A default protected constructor is needed so JPA can work
    protected Material() { }

    /* Getters */

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getNameSpanish() {
        return nameSpanish;
    }

    public String getTechnique() {
        return technique;
    }

    Set<Product> getProducts() {
        return Collections.unmodifiableSet(products);
    }

    /* Functions */
    /* Thema 24: @ManyToMany */
    void add(Product product) {
        if (!products.add(product)) {
            throw new ProductAlreadyHasThisMaterialException();
        }
    }

    void update(String name, String nameInSpanish, String technique) {
        this.name = name;
        this.nameSpanish = nameInSpanish;
        this.technique = technique;
    }
    /* Equals & Hashcode */
    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Material material)) return false;
        return Objects.equals(name, material.name);
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(name);
    }

}
