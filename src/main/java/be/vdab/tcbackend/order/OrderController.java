package be.vdab.tcbackend.order;

import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("orders")
class OrderController {

    private final OrderService orderService;

    OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    /* DTOs */
    // DTO order general info no details
    private record OrderWithoutDetails(
            long id,
            LocalDateTime orderDate,
            Status status,
            long customerId,
            String customerFirstName,
            String customerLastName) {
        public OrderWithoutDetails(Order order) {
            this(order.getId(), 
                 order.getOrderDate(),
                 order.getStatus(),
                 order.getUser().getId(),
                 order.getUser().getFirstName(),
                 order.getUser().getLastName());
        }
    }

    // DTO to show an order information with final total and product details including price by ordered units
    private record OrderWithFullDetails(
            long id,
            LocalDateTime orderDate,
            Status status,
            BigDecimal total,
            long customerId,
            String customerFirstName,
            String customerLastName,
            List<OrderedDetails>orderDetails){
        public OrderWithFullDetails(Order order) {
            this(order.getId(),
                 order.getOrderDate(),
                 order.getStatus(),

                 // Calculation Final total of the order
                 // order.getTotal(),
                 order.getOrderDetails()
                         .stream()
                         .map(orderDetail -> BigDecimal.valueOf(orderDetail.getQuantity()).multiply(orderDetail.getUnitPrice()))
                         .reduce(BigDecimal.ZERO, BigDecimal::add),

                 order.getUser().getId(),
                 order.getUser().getFirstName(),
                 order.getUser().getLastName(),

                 // Order details list
                 order.getOrderDetails()
                         .stream()
                         .map(OrderedDetails::new)
                         .toList()
            );
        }  
    }

    // DTO: List of product details to be used by DTO OrderWithFullDetails
    private record OrderedDetails(String productName, int quantity, BigDecimal unitPrice, BigDecimal value) {
        public OrderedDetails(OrderDetail orderDetails) {
            this(orderDetails.getProductName(),
                 orderDetails.getQuantity(),
                 orderDetails.getUnitPrice(),
                 // Calculation by product: quantity * unit price
                 BigDecimal.valueOf(orderDetails.getQuantity()).multiply(orderDetails.getUnitPrice())
            );
        }
    }


    /* REQUESTS */
    /* GET request that returns the count of orders  */
    // GET http://localhost:8080/orders/count
    @GetMapping("count")
    long findCount() { return orderService.findCount(); }

    /* GET request that returns all the product names */
    // GET http://localhost:8080/orders/all
    @GetMapping("all")
    List<OrderWithoutDetails> findAll() {
        return orderService.findAll()
                .stream()
                .map(order -> new OrderWithoutDetails(order))
              //  .map(product -> new ProductController.ProductName(product))
                .toList();
    }
    //List means: I already have a collection of results.
    // Stream: I want to process a sequence of objects.

    // GET requests finds by id returns one order */
    // GET http://localhost:8080/orders/{{id}}
    @GetMapping("{id}")
    Optional<OrderWithoutDetails> findById(@PathVariable long id) {
        return Optional.of(orderService.findById(id)
                .map(order -> new OrderWithoutDetails(order))
                .orElseThrow(OrderNotFoundException::new));
    }

    // GET requests finds by id returns one order */
    // GET http://localhost:8080/orders/{{id}}
    @GetMapping("{id}/fulldetails")
    List<OrderWithFullDetails> findByIdWithDetails(@PathVariable long id) {
        return orderService.findById(id)
                .map(order -> new OrderWithFullDetails(order))
                .stream().toList();
    }

    // GET request that find an order by its userId
    //

    /* POST request to create a new Order with detail & status=PLACED */
    // POST http://localhost:8080/orders/
    @PostMapping
    long create(@RequestBody @Valid NewOrder newOrder) {
        return orderService.create(newOrder);
    }

    /* PUT request to update the status of an order to 'SHIPPED' by id */
    // PUT http://localhost:8080/orders/{{id}}/ship
    @PutMapping("{id}/ship")
    void ship(@PathVariable long id) {
        orderService.ship(id);
    }

    /* PUT request to update the status of an order to 'CANCELLED' by id */
    // PUT http://localhost:8080/orders/{{id}}/cancel
    @PutMapping("{id}/cancel")
    void cancel(@PathVariable long id) {
        orderService.cancel(id);
    }

    /* PUT request to update the status of an order to 'COMPLETED' by id */
    // PUT http://localhost:8080/orders/{{id}}/completed
    @PutMapping("{id}/complete")
    void complete(@PathVariable long id) {
        orderService.complete(id);
    }



}
