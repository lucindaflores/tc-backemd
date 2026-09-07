package be.vdab.tcbackend.categories;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="categories")
public class Category {

    @Id
    private Long id;
    //@GeneratedValue(strategy = GenerationType.IDENTITY) /* Thema 9: Toevoegen */

    private String name;

    /* Thema 9: Toevoegen */
    // NOTE: No id in the constructor
    Category( String name) {
        this.name = name;
    }

    // A default protected constructor is needed so JPA can work
    protected Category() { }

    /* Getters */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

}
