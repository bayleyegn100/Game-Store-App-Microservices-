package com.yedebkid.gamestorecatalog.controller;


import com.yedebkid.gamestorecatalog.serviceLayer.GameStoreCatalogServiceLayer;
import com.yedebkid.gamestorecatalog.viewModel.TShirtViewModel;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.context.config.annotation.RefreshScope;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value="tshirt")
@RefreshScope
public class TShirtController {
    @Autowired
    GameStoreCatalogServiceLayer gameStoreCatalogServiceLayer;
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TShirtViewModel createTShirt(@RequestBody @Valid TShirtViewModel tShirtViewModel) {
        tShirtViewModel = gameStoreCatalogServiceLayer.createTShirt(tShirtViewModel);
        return tShirtViewModel;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public TShirtViewModel getTShirt(@PathVariable("id") int tShirtId) {
        TShirtViewModel tShirtViewModel = gameStoreCatalogServiceLayer.getTShirt(tShirtId);
        if (tShirtViewModel == null) {
            throw new IllegalArgumentException("T-Shirt could not be retrieved for id " + tShirtId);
        } else {
            return tShirtViewModel;
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTShirt(@RequestBody @Valid TShirtViewModel tShirtViewModel) {
        if (tShirtViewModel==null || tShirtViewModel.getId() < 1) {
            throw new IllegalArgumentException("Id in path must match id in view model");
        }else if (tShirtViewModel.getId() > 0) {
            gameStoreCatalogServiceLayer.updateTShirt(tShirtViewModel);
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTShirt(@PathVariable("id") int tShirtId) {
        gameStoreCatalogServiceLayer.deleteTShirt(tShirtId);
    }

    @GetMapping("/size/{size}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirtViewModel> getTShirtsBySize(@PathVariable("size") String size) {
        List<TShirtViewModel> tvmBySize = gameStoreCatalogServiceLayer.getTShirtBySize(size);
        if (tvmBySize == null || tvmBySize.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found in size " + size);
        }
        return tvmBySize;
    }

    @GetMapping("/color/{color}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirtViewModel> getTShirtsByColor(@PathVariable("color") String color) {
        List<TShirtViewModel> tvmByColor = gameStoreCatalogServiceLayer.getTShirtByColor(color);
        if (tvmByColor == null || tvmByColor.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found in " + color);
        }
        return tvmByColor;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TShirtViewModel> getAllTShirts() {
        List<TShirtViewModel> tvmByColor = gameStoreCatalogServiceLayer.getAllTShirts();
        if (tvmByColor == null || tvmByColor.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found.");
        }
        return tvmByColor;
    }
}