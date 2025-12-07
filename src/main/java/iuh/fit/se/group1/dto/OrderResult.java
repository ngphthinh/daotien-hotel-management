package iuh.fit.se.group1.dto;

import iuh.fit.se.group1.entity.Order;
import iuh.fit.se.group1.entity.SurchargeDetail;

public class OrderResult {
    private Order order;
    private SurchargeDetail  surchargeDetail;

    public OrderResult(Order order, SurchargeDetail surchargeDetail) {
        this.order = order;
        this.surchargeDetail = surchargeDetail;
    }

    public Order getOrder() {
        return order;
    }

    public void setOrder(Order order) {
        this.order = order;
    }

    public SurchargeDetail getSurchargeDetail() {
        return surchargeDetail;
    }

    public void setSurchargeDetail(SurchargeDetail surchargeDetail) {
        this.surchargeDetail = surchargeDetail;
    }
}
