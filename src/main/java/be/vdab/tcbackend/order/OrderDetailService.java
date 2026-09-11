package be.vdab.tcbackend.order;

import be.vdab.tcbackend.products.Product;

import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
@Transactional(readOnly=true)
class OrderDetailService {

    private final OrderDetailRepository orderDetailRepository;


    OrderDetailService(OrderDetailRepository orderDetailRepository) {
        this.orderDetailRepository = orderDetailRepository;
    }

    Optional<OrderDetail> findById(long id) {
        return orderDetailRepository.findById(id);
    }

    /* Method that returns a list of all orders */
    List<OrderDetail> findAll() {
        return orderDetailRepository.findAll();
    }

    /* Method that returns the count of orderdetails by Order Id in table Orders */
    long findCountByOrderId(long orderId) {
        return orderDetailRepository.findCountByOrderId(orderId);
    }

    /* Method that returns a list of all orders */
    List<OrderDetail> findAllByOrderId(long orderId) {
        return orderDetailRepository.findAllByOrderId(orderId);
    }




}
