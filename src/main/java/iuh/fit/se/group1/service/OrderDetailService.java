package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import iuh.fit.se.group1.repository.jpa.OrderDetailRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailService extends Service {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailService.class);
    private final OrderDetailRepositoryImpl orderDetailRepository;
    private AmenityService amenityService = new AmenityService();

    public OrderDetailService() {
        this.orderDetailRepository = new OrderDetailRepositoryImpl();
    }

    public boolean saveOrderDetailsForOrder(EntityManager em, Order savedOrder, List<OrderDetail> orderDetails) {
        return orderDetailRepository.save(em, savedOrder, orderDetails);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId) {
        return doInTransaction(entityManager -> orderDetailRepository.findByOrderId(entityManager, orderId));
    }

    public void deleteById(Long amenityId, Long orderId) {
        doInTransactionVoid(entityManager -> orderDetailRepository.deleteById(entityManager, amenityId, orderId));
    }

    public void deleteByOrderId(Long orderId) {
        doInTransactionVoid(entityManager -> orderDetailRepository.deleteByOrderId(entityManager, orderId));
    }

    public boolean saveByOrderId(Long orderId, List<OrderDetail> orderDetails) {
        return doInTransaction(entityManager -> orderDetailRepository.saveByOrderId(entityManager, orderId, orderDetails));
    }

    public OrderDetail save(OrderDetail newDetail, Long orderId) {


            return doInTransaction(entityManager -> {
                Amenity amenity = amenityService.getAmenityById(entityManager, newDetail.getAmenity().getAmenityId());
                if (amenity == null) {
                    log.error("Cannot find amenity with id: {}", newDetail.getAmenity().getAmenityId());
                    return null;
                } else {
                    newDetail.setUnitPrice(amenity.getPrice());
                    return orderDetailRepository.save(entityManager, orderId, newDetail);
                }
            });
    }

    public void updateOrderDetailFormOrderId(Long amenityId, BigDecimal unitPrice, int quantity, Long orderId) {
        doInTransactionVoid(entityManager -> orderDetailRepository.updateOrderDetailFormOrderId(entityManager, amenityId, unitPrice, quantity, orderId));
    }

    public void deleteById(Long id) {
        doInTransactionVoid(entityManager -> orderDetailRepository.deleteByOrderId(entityManager, id));
    }
}
