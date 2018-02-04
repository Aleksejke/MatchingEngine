package main;

import java.io.Serializable;
import java.math.BigDecimal;


public class Orders implements Comparable <Orders>, Serializable {
    private String name;
    private String operation;
    private String stock;
    private int quantity;
    private BigDecimal price;

    public Orders() {
        name = "";
        operation = "";
        stock = "";
        quantity = 0;
        price = new BigDecimal("0.00");
    }

    public Orders(String n, String o, String s, int q, BigDecimal p) {
        name = n;
        operation = o;
        stock = s;
        quantity = q;
        price = p;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getOperation() {
        return operation;
    }

    public void setOperation(String operation) {
        this.operation = operation;
    }

    public String getStock() {
        return stock;
    }

    public void setStock(String stock) {
        this.stock = stock;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    //sorting, to make searching easier
    @Override
    public int compareTo(Orders o) {
        if (operation.equals("SELL"))
            return price.compareTo(o.price);
        else return o.price.compareTo(price);
    }
}


