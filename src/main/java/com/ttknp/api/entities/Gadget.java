package com.ttknp.api.entities;

import org.springframework.data.relational.core.mapping.Table;

@Table(name = "gadget")
public class Gadget {

    private String gid;
    private String model;
    private String brand;
    private Double price;
    private Long amount;

    public Gadget() { // importance in jdbc
    }

    public Gadget(String gid, String model, String brand, Double price, Long amount) {
        this.gid = gid;
        this.model = model;
        this.brand = brand;
        this.price = price;
        this.amount = amount;
    }

    public String getGid() {
        return gid;
    }

    public void setGid(String gid) {
        this.gid = gid;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getBrand() {
        return brand;
    }

    public void setBrand(String brand) {
        this.brand = brand;
    }

    public Double getPrice() {
        return price;
    }

    public void setPrice(Double price) {
        this.price = price;
    }

    public Long getAmount() {
        return amount;
    }

    public void setAmount(Long amount) {
        this.amount = amount;
    }

    @Override
    public String toString() {
        final StringBuffer sb = new StringBuffer("Gadget{");
        sb.append("gid='").append(gid).append('\'');
        sb.append(", model='").append(model).append('\'');
        sb.append(", brand='").append(brand).append('\'');
        sb.append(", price=").append(price);
        sb.append(", amount=").append(amount);
        sb.append('}');
        return sb.toString();
    }
}
