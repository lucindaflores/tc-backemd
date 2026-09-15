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
                   generator = "materialGenerator")
    @SequenceGenerator(name = "materialGenerator",
            sequenceName = "materialid",
     allocationSize = 1)
    private long id;

    String name;

    @Column(name = "name_spanish")
    String nameSpanish;
    String technique;

    @ManyToMany(mappedBy = "materials")
    private Set<Product> products = new LinkedHashSet<>();

    /* Constructors */
    public Material(String name, String nameSpanish, String technique) {
        this.name = name;
        this.nameSpanish = nameSpanish;
        this.technique = technique;
    }

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
    /*  @ManyToMany */
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
