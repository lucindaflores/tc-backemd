package be.vdab.tcbackend.order;

import be.vdab.tcbackend.products.NotEnoughProductsException;
import be.vdab.tcbackend.products.ProductNotFoundException;
import be.vdab.tcbackend.products.ProductRepository;
import be.vdab.tcbackend.users.AddressNotFoundException;
import be.vdab.tcbackend.users.AddressRepository;
import be.vdab.tcbackend.users.UserNotFoundException;
import be.vdab.tcbackend.users.UserRepository;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;


@Service
@Transactional(readOnly=true)
class OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final OrderDetailRepository orderDetailRepository;
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    OrderService(OrderRepository orderRepository, UserRepository userRepository, OrderDetailRepository orderDetailRepository, ProductRepository productRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
        this.orderDetailRepository = orderDetailRepository;
        this.productRepository = productRepository;
        this.addressRepository = addressRepository;
    }

    /* Method that returns the count of orders in table Orders */
    long findCount() {
        return orderRepository.count();
    }

    /* Method that returns a list of all orders */
    List<Order> findAll() {
        return orderRepository.findAll();
    }

    /* Method that finds by id and returns an optional Product  */
    Optional<Order> findById(long id) {
        return  orderRepository.findById(id);
    }

    /* Create order*/
    /* Method that creates a new order and  changes the status of the order to 'ORDERED'  */
    /* Thema 9: Toevoegen */
    // UPDATE - Thema 21: @ManyToOne
    @Transactional
    long create(NewOrder newOrder) {
        try {
            // find the user in userRepo
            var user = userRepository.findById(newOrder.userId())
                    .orElseThrow(UserNotFoundException::new);


            var address = addressRepository.findByIdAndUserId(newOrder.addressId(), user.getId())
                    .orElseThrow(AddressNotFoundException::new);

            // Create an Order  obj
            var order = new Order(user, address);
            // An empty  orderDetails LinkedHasSet is created
            // User details need to be saved in a previous step

            // Validates that the order contains details
            if (newOrder.orderDetails().isEmpty()) {
                throw new OrderDetailEmptyException();
            }

            /* Order Detail */
            for (var orderLine : newOrder.orderDetails()) {
                var product = productRepository.findById(orderLine.productId())
                        .orElseThrow(ProductNotFoundException::new);

                // Check that there are enough items in stock
                if (product.getStock() < orderLine.quantity()) {
                    throw new NotEnoughProductsException(product.getId());
                }

                var detail = new OrderDetail(product.getName(),
                                             orderLine.quantity(),
                                             product.getPrice(), //snapshot unit price
                                             order,
                                             product);

                product.decreaseStock(orderLine.quantity());

               order.add(detail);
            }

            // Save in OrderRepo
            orderRepository.save(order);

            return order.getId();
        } catch (DataIntegrityViolationException _) {
            throw new OrderAlreadyPlacedException();
        }

    }


    /* Ship Order */
    /* Method that changes the status of the order to 'SHIPPED' and
      updates that decreases the stock for every product shipped */
    @Transactional
    void ship(long id) {
        var order = orderRepository.findByIdWithOrderDetailsAndProducts(id)
                .orElseThrow(OrderNotFoundException::new);

        order.ship();
    }

    /* Cancel order */
    /* Method that updates that decreases the stock for every product shipped */
    @Transactional
    void cancel(long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        order.cancel();
    }

    /* Complete order */
    @Transactional
    void complete(long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        order.complete();
    }


}
