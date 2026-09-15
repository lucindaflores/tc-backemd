package be.vdab.tcbackend.orders;

import be.vdab.tcbackend.products.NotEnoughProductsException;
import be.vdab.tcbackend.products.ProductNotFoundException;
import be.vdab.tcbackend.products.ProductRepository;
import be.vdab.tcbackend.users.*;
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
    private final ProductRepository productRepository;
    private final AddressRepository addressRepository;

    OrderService(OrderRepository orderRepository, UserRepository userRepository, ProductRepository productRepository, AddressRepository addressRepository) {
        this.orderRepository = orderRepository;
        this.userRepository = userRepository;
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

    List<Order> findByUserIdOrderByOrderDateDesc(long userId) {
        return orderRepository.findByUserIdOrderByOrderDateDesc(userId);
    }

    /* Create order*/
    /* Method that creates a new order and  changes the status of the order to 'ORDERED'  */
    @Transactional
    long create(NewOrder newOrder) {
        try {
            // find the user in userRepo
            var user = userRepository.findById(newOrder.userId())
                    .orElseThrow(UserNotFoundException::new);


            var address = addressRepository.findByIdAndUserId(newOrder.addressId(), user.getId())
                    .orElseThrow(AddressNotFoundException::new);

            var order = new Order(user, address);

            if (!order.getUser().isActive()) {
                throw new UserInactiveException();
            }

            // Validates that the order contains details
            if (newOrder.orderDetails().isEmpty()) {
                throw new OrderDetailEmptyException();
            }

            /* Order Detail */
            for (var detail : newOrder.orderDetails()) {
                var product = productRepository.findById(detail.productId())
                        .orElseThrow(ProductNotFoundException::new);

                // Check that there are enough items in stock
                if (product.getStock() < detail.quantity()) {
                    throw new NotEnoughProductsException(product.getId());
                }

                var orderDetail = new OrderDetail(product.getName(),
                                             detail.quantity(),
                                             product.getPrice(), //snapshot unit price
                                             order,
                                             product);

                product.decreaseStock(detail.quantity());

               order.add(orderDetail);
            }

            orderRepository.save(order);

            return order.getId();
        } catch (DataIntegrityViolationException _) {
            throw new OrderAlreadyPlacedException();
        }

    }


    /* Ship Order */
    @Transactional
    void ship(long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        order.ship();
    }

    /* Cancel order */
    @Transactional
    void cancel(long id) {
        var order = orderRepository.findById(id)
                .orElseThrow(OrderNotFoundException::new);

        for (var detail : order.getOrderDetails()) {
            detail.getProduct().increaseStock(detail.getQuantity());
        }

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
