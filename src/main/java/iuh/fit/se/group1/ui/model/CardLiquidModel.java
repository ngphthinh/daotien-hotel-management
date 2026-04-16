/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package iuh.fit.se.group1.ui.model;

/**
 *
 * @author THIS PC
 */
public class CardLiquidModel {
    private String name;
    private int quantity;
    private double percentage;

    public CardLiquidModel(String name, int quantity, double percentage) {
        this.name = name;
        this.quantity = quantity;
        this.percentage = percentage;
    }

    public CardLiquidModel() {
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getPercentage() {
        return percentage;
    }

    public void setPercentage(double percentage) {
        this.percentage = percentage;
    }
    
    
}
