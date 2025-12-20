package iuh.fit.se.group1.util;

public class InvoiceItem{
    private int count;
    private String content;
    private String unit;
    private String unitPrice;
    private int quantity;
    private String totalPrice;


    /**
     *
     * @param count
     * @param content
     * @param unit
     * @param unitPrice
     * @param quantity
     * @param totalPrice
     */
    public InvoiceItem(int count, String content, String unit, String unitPrice, int quantity, String totalPrice) {
        this.count = count;
        this.content = content;
        this.unit = unit;
        this.unitPrice = unitPrice;
        this.quantity = quantity;
        this.totalPrice = totalPrice;
    }

    public String getUnit() {
        return unit;
    }

    public void setUnit(String unit) {
        this.unit = unit;
    }

    public Integer getCount() {
        return count;
    }

    public void setCount(int count) {
        this.count = count;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getUnitPrice() {
        return unitPrice;
    }

    public void setUnitPrice(String unitPrice) {
        this.unitPrice = unitPrice;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public String getTotalPrice() {
        return totalPrice;
    }

    public void setTotalPrice(String totalPrice) {
        this.totalPrice = totalPrice;
    }
}