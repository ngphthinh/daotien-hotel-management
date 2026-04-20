package iuh.fit.se.group1.entity;

import iuh.fit.se.group1.enums.OrderBookStatus;

import jakarta.persistence.*;
import lombok.*;
import org.hibernate.proxy.HibernateProxy;

import java.time.LocalDate;
import java.util.Objects;

@AllArgsConstructor
@NoArgsConstructor
@Setter
@Getter
@ToString
@Entity
@Builder
public class OrderType {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)

    private Long orderTypeId;

    @Enumerated(EnumType.STRING)
    private OrderBookStatus name;
    private LocalDate createdAt;

    public OrderType(Long orderTypeId) {
        this.orderTypeId = orderTypeId;
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (o == null) return false;
        Class<?> oEffectiveClass = o instanceof HibernateProxy ? ((HibernateProxy) o).getHibernateLazyInitializer().getPersistentClass() : o.getClass();
        Class<?> thisEffectiveClass = this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass() : this.getClass();
        if (thisEffectiveClass != oEffectiveClass) return false;
        OrderType orderType = (OrderType) o;
        return getOrderTypeId() != null && Objects.equals(getOrderTypeId(), orderType.getOrderTypeId());
    }

    @Override
    public final int hashCode() {
        return this instanceof HibernateProxy ? ((HibernateProxy) this).getHibernateLazyInitializer().getPersistentClass().hashCode() : getClass().hashCode();
    }
}
