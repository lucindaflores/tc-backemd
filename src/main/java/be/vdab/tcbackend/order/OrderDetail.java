package be.vdab.tcbackend.order;

import be.vdab.tcbackend.products.Product;
import jakarta.persistence.*;

import java.math.BigDecimal;

@Entity
@Table(name = "order_details")
public class OrderDetail {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE,
            generator = "oderDetailGenerator") /*9.8 Sequence */
    @SequenceGenerator(name = "oderDetailGenerator",
            sequenceName = "orderitemid",
            allocationSize = 1) // increment by 1
    private long id;

    @Column(name = "product_name")
    private String productName; // product name snapshot

    private int quantity;

    @Column(name = "unit_price")
    private BigDecimal unitPrice;

    // fk_orderdetails_order
    // n:1. orderdetails:order
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    private Order order;

    // fk_orderdetails_product
    // n:1 orderdetails:product
    @ManyToOne(optional = false, fetch = FetchType.LAZY)
    @JoinColumn(name = "product_id")
    private Product product;

    /* Constructors */
    public OrderDetail(String productName, int quantity, BigDecimal unitPrice, Order order, Product product) {
        this.productName = productName;
        this.quantity = quantity;
        this.unitPrice = unitPrice;
        this.order = order;
        this.product = product;
    }

    protected OrderDetail() { }

    /* Getters */

    public long getId() {
        return id;
    }

    public String getProductName() {
        return productName;
    }

    public int getQuantity() {
        return quantity;
    }

    public BigDecimal getUnitPrice() {
        return unitPrice;
    }

    public Order getOrder() {
        return order;
    }

    public Product getProduct() {
        return product;
    }

}
