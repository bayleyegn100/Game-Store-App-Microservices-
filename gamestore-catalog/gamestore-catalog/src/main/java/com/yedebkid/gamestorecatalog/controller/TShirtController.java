package com.yedebkid.gamestorecatalog.controller;


import com.yedebkid.gamestorecatalog.model.TShirt;
import com.yedebkid.gamestorecatalog.rpository.TShirtRepository;
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
    TShirtRepository tshirtRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public TShirt createTShirt(@RequestBody @Valid TShirt tshirt) {
        tshirt = tshirtRepository.save(tshirt);
        return tshirt;
    }

    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Optional<TShirt> getTShirt(@PathVariable("id") Long tShirtId) {
        Optional<TShirt> tshirt = tshirtRepository.findById(tShirtId);
        if (tshirt == null) {
            throw new IllegalArgumentException("T-Shirt could not be retrieved for id " + tShirtId);
        } else {
            return tshirt;
        }
    }

    @PutMapping
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void updateTShirt(@RequestBody @Valid TShirt tshirt) {
        if (tshirt==null || tshirt.getId() < 1) {
            throw new IllegalArgumentException("Id in path must match id in view model");
        }else if (tshirt.getId() > 0) {
            tshirtRepository.findById(tshirt.getId());
        }
    }

    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteTShirt(@PathVariable("id") Long tShirtId) {
        tshirtRepository.findById(tShirtId);
    }

    @GetMapping("/size/{size}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getTShirtsBySize(@PathVariable("size") String size) {
        List<TShirt> tvmBySize = tshirtRepository.findAllBySize(size);
        if (tvmBySize == null || tvmBySize.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found in size " + size);
        }
        return tvmBySize;
    }

    @GetMapping("/color/{color}")
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getTShirtsByColor(@PathVariable("color") String color) {
        List<TShirt> tvmByColor = tshirtRepository.findAllByColor(color);
        if (tvmByColor == null || tvmByColor.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found in " + color);
        }
        return tvmByColor;
    }

    @GetMapping()
    @ResponseStatus(HttpStatus.OK)
    public List<TShirt> getAllTShirts() {
        List<TShirt> tvmByColor = tshirtRepository.findAll();
        if (tvmByColor == null || tvmByColor.isEmpty()) {
            throw new IllegalArgumentException("No t-shirts were found.");
        }
        return tvmByColor;
    }
}