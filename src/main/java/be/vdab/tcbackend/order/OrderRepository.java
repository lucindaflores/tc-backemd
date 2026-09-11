package be.vdab.tcbackend.order;

import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;

interface OrderRepository extends JpaRepository<Order, Long> {

    /* 1.5 Ship */
    @EntityGraph(attributePaths = {"orderDetails", "orderDetails.product"})
    @Query("FROM Order o WHERE o.id = :id")
    Optional<Order> findByIdWithOrderDetailsAndProducts(long id);

}
