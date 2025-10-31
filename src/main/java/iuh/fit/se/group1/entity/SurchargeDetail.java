package iuh.fit.se.group1.entity;

import java.time.LocalDate;

public class SurchargeDetail {
    private Order orderId;
    private Surcharge surchargeId;  
    private int quantity;
    private LocalDate createAt;

    public SurchargeDetail() {
    }
     public SurchargeDetail(Order orderId, Surcharge surchargeId, int quantity, LocalDate createAt) {
        this.orderId = orderId;
        this.surchargeId = surchargeId;
        this.quantity = quantity;
        this.createAt = createAt;}
        public Order getOrderId() {
            return orderId;
        }
        public void setOrderId(Order orderId) {
            this.orderId = orderId;
        }
        public Surcharge getSurchargeId() {
            return surchargeId;
        }
        public void setSurchargeId(Surcharge surchargeId) {
            this.surchargeId = surchargeId;
        }
        public int getQuantity() {
            return quantity;
        }
        public void setQuantity(int quantity) {
            this.quantity = quantity;
        }
        public LocalDate getCreateAt() {
            return createAt;
        }
        public void setCreateAt(LocalDate createAt) {
            this.createAt = createAt;
        }
        @Override
        public int hashCode() {
            // TODO Auto-generated method stub
            return super.hashCode();
        }
        @Override
        public boolean equals(Object obj) {
            // TODO Auto-generated method stub
            return super.equals(obj);
        }

        @Override
        public String toString() {
            // TODO Auto-generated method stub
            return super.toString();
        }

}
