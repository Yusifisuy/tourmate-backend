package az.tourmate.repositories.order;

import az.tourmate.models.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Date;
import java.util.List;

public interface OrderRepository extends JpaRepository<Order,Long> {

    List<Order> findAllByActiveIsTrue();

    List<Order> findAllByExitDateAfterAndActiveIsTrue(Date currentDate);

}
