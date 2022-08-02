package com.yedebkid.gamestoreinvoicing.model;

import java.math.BigDecimal;
import java.util.Objects;

public class TShirtResponseModal {
    private long id;
    private String size;
    private String color;
    private String description;
    private BigDecimal price;
    private long quantity;

    public TShirtResponseModal(long id, String size, String color, String description, BigDecimal price, long quantity) {
        this.id = id;
        this.size = size;
        this.color = color;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public TShirtResponseModal(String size, String color, String description, BigDecimal price, long quantity) {
        this.size = size;
        this.color = color;
        this.description = description;
        this.price = price;
        this.quantity = quantity;
    }

    public TShirtResponseModal() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getColor() {
        return color;
    }

    public void setColor(String color) {
        this.color = color;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public BigDecimal getPrice() {
        return price;
    }

    public void setPrice(BigDecimal price) {
        this.price = price;
    }

    public long getQuantity() {
        return quantity;
    }

    public void setQuantity(long quantity) {
        this.quantity = quantity;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof TShirtResponseModal)) return false;
        TShirtResponseModal tShirtResponseModal = (TShirtResponseModal) o;
        return getId() == tShirtResponseModal.getId() &&
                getQuantity() == tShirtResponseModal.getQuantity() &&
                Objects.equals(getSize(), tShirtResponseModal.getSize()) &&
                Objects.equals(getColor(), tShirtResponseModal.getColor()) &&
                Objects.equals(getDescription(), tShirtResponseModal.getDescription()) &&
                Objects.equals(getPrice(), tShirtResponseModal.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getSize(), getColor(), getDescription(), getPrice(), getQuantity());
    }

}
