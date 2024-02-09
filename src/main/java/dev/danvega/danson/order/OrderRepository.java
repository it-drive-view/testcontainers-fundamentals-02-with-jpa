package dev.danvega.danson.order;

import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;

import java.util.List;

public interface OrderRepository extends CrudRepository<Order, Long> {

    @Query(value = """
      SELECT *
      FROM orders
      WHERE items @> '[{"name": "MacBook Pro"}]';
    """, nativeQuery = true)
    List<Order> findAllContainingMacBookPro();

    List<Order> findAllByTrackingNumber(String trackingNumber);






}
