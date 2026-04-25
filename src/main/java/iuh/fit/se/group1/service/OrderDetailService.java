package iuh.fit.se.group1.service;

import iuh.fit.se.group1.dto.OrderDetailDTO;
import iuh.fit.se.group1.entity.Amenity;
import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.OrderDetail;
import iuh.fit.se.group1.mapper.OrderDetailMapper;
import iuh.fit.se.group1.repository.interfaces.OrderRepository;
import iuh.fit.se.group1.repository.jpa.OrderDetailRepositoryImpl;
import jakarta.persistence.EntityManager;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.math.BigDecimal;
import java.util.List;
import java.util.stream.Collectors;

public class OrderDetailService extends Service {

    private static final Logger log = LoggerFactory.getLogger(OrderDetailService.class);
    private final OrderDetailRepositoryImpl orderDetailRepository;
    private final AmenityService amenityService = new AmenityService();
    private final OrderDetailMapper orderDetailMapper = new OrderDetailMapper();

    public OrderDetailService() {
        this.orderDetailRepository = new OrderDetailRepositoryImpl();
    }

    public boolean saveOrderDetailsForOrder(EntityManager em, Order savedOrder, List<OrderDetailDTO> orderDetailsDtos) {

        List<OrderDetail> orderDetails = orderDetailsDtos.stream().map(orderDetailMapper::toOrderDetail).collect(Collectors.toList());

        return orderDetailRepository.save(em, savedOrder, orderDetails);
    }

    public List<OrderDetailDTO> getOrderDetailsByOrderId(Long orderId) {
        return doInTransaction(entityManager -> orderDetailRepository.findByOrderId(entityManager, orderId)).stream().map(orderDetailMapper::toDTO).collect(Collectors.toList());
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

    public OrderDetailDTO save(OrderDetailDTO newDetailDTO, Long orderId) {

        OrderDetail newDetail = orderDetailMapper.toOrderDetail(newDetailDTO);


        return doInTransaction(entityManager -> {
            Amenity amenity = amenityService.getAmenityEntityById(entityManager, newDetail.getAmenity().getAmenityId());
            if (amenity == null) {
                log.error("Cannot find amenity with id: {}", newDetail.getAmenity().getAmenityId());
                return null;
            } else {
                newDetail.setUnitPrice(amenity.getPrice());
                return orderDetailMapper.toDTO(orderDetailRepository.save(entityManager, orderId, newDetail));
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
