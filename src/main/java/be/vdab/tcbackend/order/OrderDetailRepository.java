package be.vdab.tcbackend.order;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

interface OrderDetailRepository extends JpaRepository<OrderDetail, Long> {

    @Query("select count(od.order.id) FROM OrderDetail od WHERE od.order.id = :orderId")
    long findCountByOrderId(long orderId);

    @Query("FROM OrderDetail od WHERE od.order.id = :orderId")
    List<OrderDetail> findAllByOrderId(long orderId);
}
