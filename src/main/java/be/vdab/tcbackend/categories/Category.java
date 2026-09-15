package be.vdab.tcbackend.categories;

import jakarta.persistence.*;

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


    /* Constructor(s) */
    Category(String name) {
        this.name = name;
    }

    protected Category() { }


    /* Getters */
    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public void updateName(String name) {
        this.name = name;
    }



}
