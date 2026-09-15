package be.vdab.tcbackend.products;

import be.vdab.tcbackend.categories.Category;
import be.vdab.tcbackend.orders.OrderDetail;
import be.vdab.tcbackend.origins.Origin;
import jakarta.persistence.*;

import java.math.BigDecimal;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.Set;

@Entity
@Table(name="products")
public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
                      generator = "productGenerator") /*9.8 Sequence */
    @SequenceGenerator(name = "productGenerator",
                    sequenceName = "productid",
    allocationSize = 1) // increment by 1
    private long id;

    private String code; // It's unique
    private String name;
    private String description;
    private BigDecimal price; // >0
    private int stock; // >=0

    @Column(name = "image_url")
    private String imageUrl;

    private boolean active;

    /* Thema 21: @ManyToOne */
    // LAZY: So it does not load both tables when just one field of one table is needed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "category_id")
    private Category category;

    /* Thema 21: @ManyToOne */
    // LAZY: So it does not load both tables when just one field of one table is needed
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "origin_id")
    private Origin origin;

    /* Thema 24: @ManyToMany */
    //Which Java entity contains the @JoinTable mapping and whose collection changes Hibernate uses to maintain that join table.
    // Owning side
    @ManyToMany
    @JoinTable(
            name = "product_materials",
            joinColumns = @JoinColumn(name = "product_id"),
            inverseJoinColumns = @JoinColumn(name = "material_id"))
    private Set<Material> materials = new LinkedHashSet<>();

    @OneToMany(mappedBy = "product")
    private Set<OrderDetail> orderDetails;


    /* Constructors */
    // Thema 9: Toevoegen
    // NOTE: No id in the constructor bc db created the id
    Product(String code,
            String name,
            String description,
            BigDecimal price,
            int stock,
            String imageUrl,
            boolean isActive,
            Category category,
            Origin origin) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.active = isActive;
        this.category = category;
        this.origin = origin;
        orderDetails = new LinkedHashSet<>();
       // materials = new LinkedHashSet<>();
    }

    // Thema 9: Toevoegen
    // A default protected constructor is needed so JPA can work
    protected Product() { }


    /* Getters */
    public long getId() {
        return id;
    }

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public String getDescription() {
        return description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public int getStock() {
        return stock;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public boolean active() {
        return active;
    }

    // Thema 21: @ManyToOne
    public Category getCategory() {
        return category;
    }

    // Thema 21: @ManyToOne
    public Origin getOrigin() {
        return origin;
    }

    // Thema 24: @ManyToMany
    public Set<Material> getMaterials() {
        return Collections.unmodifiableSet(materials);
    }


    /* @OneToMany */
    public Set<OrderDetail> getOrderDetails() {
       // return orderDetails;
        return Collections.unmodifiableSet(orderDetails);
    }

    public void setOrderDetails(Set<OrderDetail> orderDetails) {
        this.orderDetails = orderDetails;
    }

    /* Equals & Hashcode */
    public boolean equals(Object object) {
        return object instanceof Product product && code.equalsIgnoreCase(product.code);
    }

    @Override
    public int hashCode() {
        return code.toLowerCase().hashCode();
    }

    /* Functions */
    void add(Material material) {
        if (!materials.add(material)) {
            throw new ProductAlreadyHasThisMaterialException();
        }
    }

    /* Functions */
    public void decreaseStock(int value) {
        if (this.stock < value) {
            throw new NotEnoughProductsException(this.getId());
        }

        stock -= value;
    }

    public void increaseStock(int value) {
        this.stock += value;
    }

 /* This method updates all fields at once, instead of calling set by set */
    void update(String code,
                String name,
                String description,
                BigDecimal price,
                int stock,
                String imageUrl,
                boolean isActive,
                Category category,
                Origin origin) {
        this.code = code;
        this.name = name;
        this.description = description;
        this.price = price;
        this.stock = stock;
        this.imageUrl = imageUrl;
        this.active = isActive;
        this.category = category;
        this.origin = origin;
    }


    void replaceMaterials(Collection<Material> newMaterials) {
        materials.clear();
        materials.addAll(newMaterials);
    }


}
