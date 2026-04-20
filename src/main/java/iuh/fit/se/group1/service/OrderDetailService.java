package iuh.fit.se.group1.service;

import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import iuh.fit.se.group1.repository.OrderDetailRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;

public class OrderDetailService {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailService.class);
    private final OrderDetailRepository orderDetailRepository;
    private AmenityService amenityService = new AmenityService();
    public OrderDetailService() {
        this.orderDetailRepository = new OrderDetailRepository();
    }

    public boolean saveOrderDetailsForOrder(Order savedOrder, List<OrderDetail> orderDetails) {
        return orderDetailRepository.save(savedOrder, orderDetails);
    }

    public List<OrderDetail> getOrderDetailsByOrderId(Long orderId){
        return orderDetailRepository.findByOrderId(orderId);
    }

    public void deleteById(Long amenityId, Long orderId) {
        orderDetailRepository.deleteById(amenityId, orderId);
    }

    public void deleteByOrderId(Long orderId) {
        orderDetailRepository.deleteByOrderId(orderId);
    }

    public boolean saveByOrderId(Long orderId, List<OrderDetail> orderDetails) {
        return orderDetailRepository.saveByOrderId(orderId, orderDetails);
    }

    public OrderDetail save(OrderDetail newDetail, Long orderId) {
        Amenity amenity = amenityService.getAmenityById(newDetail.getAmenity().getAmenityId());
        if (amenity == null) {
            log.error("Cannot find amenity with id: {}", newDetail.getAmenity().getAmenityId());
            return null;
        } else {
            newDetail.setUnitPrice(amenity.getPrice());
            return orderDetailRepository.save(orderId, newDetail);
        }

    }

    public void updateOrderDetailFormOrderId(Long amenityId, BigDecimal unitPrice, int quantity, Long orderId) {
        orderDetailRepository.updateOrderDetailFormOrderId(amenityId, unitPrice, quantity, orderId);
    }

    public void deleteById(Long id) {
        orderDetailRepository.deleteById(id);
    }
}
