package com.yedebkid.gamestoreinvoicing.model;

import java.math.BigDecimal;
import java.util.Objects;

public class GameResponseModel {
    private long id;
    private String title;
    private String esrbRating;
    private String description;
    private BigDecimal price;
    private String studio;
    private long quantity;

    public GameResponseModel(long id, String title, String esrbRating, String description, BigDecimal price, String studio, long quantity) {
        this.id = id;
        this.title = title;
        this.esrbRating = esrbRating;
        this.description = description;
        this.price = price;
        this.studio = studio;
        this.quantity = quantity;
    }

    public GameResponseModel(String title, String esrbRating, String description, BigDecimal price, String studio, long quantity) {
        this.title = title;
        this.esrbRating = esrbRating;
        this.description = description;
        this.price = price;
        this.studio = studio;
        this.quantity = quantity;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getEsrbRating() {
        return esrbRating;
    }

    public void setEsrbRating(String esrbRating) {
        this.esrbRating = esrbRating;
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

    public String getStudio() {
        return studio;
    }

    public void setStudio(String studio) {
        this.studio = studio;
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
        if (!(o instanceof GameResponseModel)) return false;
        GameResponseModel gameResponseModel = (GameResponseModel) o;
        return getId() == gameResponseModel.getId() &&
                getQuantity() == gameResponseModel.getQuantity() &&
                Objects.equals(getTitle(), gameResponseModel.getTitle()) &&
                Objects.equals(getEsrbRating(), gameResponseModel.getEsrbRating()) &&
                Objects.equals(getDescription(), gameResponseModel.getDescription()) &&
                Objects.equals(getPrice(), gameResponseModel.getPrice()) &&
                Objects.equals(getStudio(), gameResponseModel.getStudio());
    }

    @Override
    public int hashCode() {
        return Objects.hash(getId(), getTitle(), getEsrbRating(), getDescription(), getPrice(), getStudio(), getQuantity());
    }
}
