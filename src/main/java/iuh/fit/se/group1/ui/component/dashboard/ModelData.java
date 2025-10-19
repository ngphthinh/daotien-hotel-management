package iuh.fit.se.group1.ui.component.dashboard;

/**
 *
 * @author RAVEN
 */
public class ModelData {


    public ModelData(String label, double quantity) {
        this.label = label;
        this.quantity = quantity;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public double getQuantity() {
        return quantity;
    }

    public void setQuantity(double quantity) {
        this.quantity = quantity;
    }

    public ModelData() {
    }

    private String label;
    private double quantity;
}
