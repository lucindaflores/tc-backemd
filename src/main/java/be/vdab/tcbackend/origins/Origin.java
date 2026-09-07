package be.vdab.tcbackend.origins;

import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.Table;

@Entity
@Table(name="origins")
public class Origin {

    @Id
    //@GeneratedValue(strategy = GenerationType.IDENTITY) /* Thema 9: Toevoegen */
    private long id;

    private String name;

    /* Constructors*/
    Origin(String name) {
        this.name = name;
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

}
