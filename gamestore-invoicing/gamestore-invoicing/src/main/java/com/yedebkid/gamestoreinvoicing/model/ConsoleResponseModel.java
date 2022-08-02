package com.yedebkid.gamestoreinvoicing.model;

import java.math.BigDecimal;
import java.util.Objects;

public class ConsoleResponseModel {
    private long id;
    private String model;
    private String manufacturer;
    private String memoryAmount;
    private String processor;
    private BigDecimal price;
    private long quantity;

    public ConsoleResponseModel(long id, String model, String manufacturer, String memoryAmount, String processor, BigDecimal price, long quantity) {
                this.id = id;
        this.model = model;
        this.manufacturer = manufacturer;
        this.memoryAmount = memoryAmount;
        this.processor = processor;
        this.price = price;
        this.quantity = quantity;
    }

    public ConsoleResponseModel(String model, String manufacturer, String memoryAmount, String processor, BigDecimal price, long quantity) {
        this.model = model;
        this.manufacturer = manufacturer;
        this.memoryAmount = memoryAmount;
        this.processor = processor;
        this.price = price;
        this.quantity = quantity;
    }

    public ConsoleResponseModel() {
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getModel() {
        return model;
    }

    public void setModel(String model) {
        this.model = model;
    }

    public String getManufacturer() {
        return manufacturer;
    }

    public void setManufacturer(String manufacturer) {
        this.manufacturer = manufacturer;
    }

    public String getMemoryAmount() {
        return memoryAmount;
    }

    public void setMemoryAmount(String memoryAmount) {
        this.memoryAmount = memoryAmount;
    }

    public String getProcessor() {
        return processor;
    }

    public void setProcessor(String processor) {
        this.processor = processor;
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
        if (!(o instanceof ConsoleResponseModel)) return false;
        ConsoleResponseModel consoleResponseModel = (ConsoleResponseModel) o;
        return getId() == consoleResponseModel.getId() &&
                getQuantity() == consoleResponseModel.getQuantity() &&
                Objects.equals(getModel(), consoleResponseModel.getModel()) &&
                Objects.equals(getManufacturer(), consoleResponseModel.getManufacturer()) &&
                Objects.equals(getMemoryAmount(), consoleResponseModel.getMemoryAmount()) &&
                Objects.equals(getProcessor(), consoleResponseModel.getProcessor()) &&
                Objects.equals(getPrice(), consoleResponseModel.getPrice());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getModel(), getManufacturer(), getMemoryAmount(), getProcessor(), getPrice(), getQuantity());
    }
}
